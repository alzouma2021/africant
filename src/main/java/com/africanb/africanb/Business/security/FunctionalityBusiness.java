package com.africanb.africanb.Business.security;


import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.dao.entity.security.RoleFunctionality;
import com.africanb.africanb.dao.repository.security.FunctionalityRepository;
import com.africanb.africanb.dao.repository.security.RoleFunctionalityRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.FunctionalityDTO;
import com.africanb.africanb.helper.dto.security.RoleDTO;
import com.africanb.africanb.helper.dto.security.RoleFunctionalityDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.security.FunctionalityTransformer;
import com.africanb.africanb.helper.transformer.security.RoleFunctionalityTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log
@Component
public class FunctionalityBusiness implements IBasicBusiness<Request<FunctionalityDTO>, Response<FunctionalityDTO>> {

    private Response<FunctionalityDTO> response;
    @Autowired
    private FunctionalityRepository functionalityRepository;
   // @Autowired
    //private UsersBusiness usersBusiness;
    @Autowired
    private RoleFunctionalityRepository roleFunctionalityRepository;
    @Autowired
    private FunctionalError functionalError;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private EntityManager em;

    @Autowired
    private RoleFunctionalityBusiness roleFunctionalityBusiness;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public FunctionalityBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    @Override
    public Response<FunctionalityDTO> create(Request<FunctionalityDTO> request, Locale locale) throws ParseException {
        log.info("----begin create Functionality-----");
        Response<FunctionalityDTO> response = new Response<FunctionalityDTO>();
        List<Functionality> items = new ArrayList<Functionality>();
        List<FunctionalityDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<FunctionalityDTO>());
        //Verification des permissions
        /*boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_FEATURE_CREATE);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas créer des fonctionalités.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }*/
        Response<FunctionalityDTO> response1 = blockDuplicationData(request, locale, response, itemsDtos);
        for(FunctionalityDTO dto : request.getDatas()){
            Functionality  existingEntity = functionalityRepository.findByCode(dto.getCode(), false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("Functionality code -> " + dto.getCode(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality entityToSave = FunctionalityTransformer.INSTANCE.toEntity(dto);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.userID); //TODO A remettre à jour
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<Functionality> itemsSaved = functionalityRepository.saveAll((Iterable<Functionality>) items);
            if (itemsSaved == null) {
                response.setStatus(functionalError.SAVE_FAIL("Functionality", locale));
                response.setHasError(true);
                return response;
            }
            List<FunctionalityDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? FunctionalityTransformer.INSTANCE.toLiteDtos(itemsSaved) : FunctionalityTransformer.INSTANCE.toDtos(itemsSaved);
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end create Functionality-----");
        return response;
    }

    private Response<FunctionalityDTO> blockDuplicationData(Request<FunctionalityDTO> request, Locale locale, Response<FunctionalityDTO> response, List<FunctionalityDTO> itemsDtos) {
        for(FunctionalityDTO dto: request.getDatas() ) {
            // Definir les parametres obligatoires
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("code", dto.getCode());
            fieldsToVerify.put("libelle", dto.getLibelle());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getCode().equalsIgnoreCase(dto.getCode()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du code '" + dto.getCode() + "' pour les rôles", locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getLibelle().equalsIgnoreCase(dto.getLibelle()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du libelle '" + dto.getLibelle() + "' pour les rôles", locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        return null;
    }

    private Response<FunctionalityDTO> blockDuplicationDataUpdate(Request<FunctionalityDTO> request, Locale locale, Response<FunctionalityDTO> response, List<FunctionalityDTO> itemsDtos) {
        for(FunctionalityDTO dto: request.getDatas() ) {
            // Definir les parametres obligatoires
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de l'id '" + dto.getCode() + "' pour les fonctionnalités", locale));
                response.setHasError(true);
                return response;
            }
            if(Utilities.isNotBlank(dto.getCode())){
                itemsDtos = itemsDtos.stream().filter(a-> Utilities.isNotBlank(a.getCode())).collect(Collectors.toList());
                if(Utilities.isNotEmpty(itemsDtos)){
                    if(itemsDtos.stream().anyMatch(a->a.getCode().equals(dto.getCode()))){
                        response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du code '" + dto.getCode() + "' pour les fonctionnalités", locale));
                        response.setHasError(true);
                        return response;
                    }
                }
            }
            itemsDtos.add(dto);
        }
        return null;
    }

    @Override
    public Response<FunctionalityDTO> update(Request<FunctionalityDTO> request, Locale locale) throws ParseException {

        log.info("----begin update Functionality-----");
        Response<FunctionalityDTO> response = new Response<FunctionalityDTO>();
        List<Functionality> items = new ArrayList<Functionality>();
        List<FunctionalityDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<FunctionalityDTO>());
        //Verification des permissions
        /*boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_FEATURE_UPDATE);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas modifier des fonctionalités.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }*/
        //empêcher la duplication des données
        Response<FunctionalityDTO> response1 = blockDuplicationDataUpdate(request, locale, response, itemsDtos);
        for(FunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality entityToSave = null;
            entityToSave = functionalityRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Functionality id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            Integer entityToSaveId = entityToSave.getId();
            if (Utilities.isNotBlank(dto.getCode()) && !dto.getCode().equals(entityToSave.getCode())) {
                Functionality existingEntity = functionalityRepository.findByCode(dto.getCode(), false);
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Functionality -> " + dto.getCode(), locale));
                    response.setHasError(true);
                    return response;
                }
                if (items.stream().anyMatch(a -> a.getCode().equalsIgnoreCase(dto.getCode()) && !a.getId().equals(entityToSaveId))) {
                    response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du code '" + dto.getCode() + "' pour les Functionality", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setCode(dto.getCode());
            }
            if (Utilities.isNotBlank(dto.getLibelle()) && !dto.getLibelle().equals(entityToSave.getLibelle())) {
                Functionality existingEntity = functionalityRepository.findByLibelle(dto.getLibelle(), false);
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Functionality -> " + dto.getLibelle(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setLibelle(dto.getLibelle());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.userID);
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<Functionality> itemsSaved = null;
            itemsSaved = functionalityRepository.saveAll((Iterable<Functionality>) items);
            if (itemsSaved == null) {
                response.setStatus(functionalError.SAVE_FAIL("Functionality", locale));
                response.setHasError(true);
                return response;
            }
            List<FunctionalityDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? FunctionalityTransformer.INSTANCE.toLiteDtos(itemsSaved) : FunctionalityTransformer.INSTANCE.toDtos(itemsSaved);
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end create Functionality-----");
        return response;
    }

    @Override
    public Response<FunctionalityDTO> delete(Request<FunctionalityDTO> request, Locale locale) {
        log.info("----begin update Functionality-----");
        Response<FunctionalityDTO> response = new Response<FunctionalityDTO>();
        List<Functionality> items = new ArrayList<Functionality>();
        //Verification des permissions
        /*boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_FEATURE_DELETE);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas supprimer des fonctionalités.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }*/
        List<RoleFunctionality> rolesFonc = Collections.synchronizedList(new ArrayList<RoleFunctionality>());
        for(FunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality existingEntity = null;
            existingEntity = functionalityRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Functionality id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true); // element de suppression, ici nous soomme dans un context de persistance donc le set permet d'enregistrer en base
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            //existingEntity.setDeletedBy(request.userID);// a modifier
            List<RoleFunctionality> roleFunctionalities = roleFunctionalityRepository.findByFunctionalityId(dto.getId(), Boolean.FALSE);
            if(Utilities.isNotEmpty(roleFunctionalities)){
                  roleFunctionalities.forEach(rf->{
                           rf.setIsDeleted(Boolean.TRUE);
                      rf.setDeletedAt(Utilities.getCurrentDate());
                      //if(Utilities.isValidID(request.userID)){
                          //   rf.setDeletedBy(request.userID);
                      //}
                  });
                rolesFonc.addAll(roleFunctionalities);
            }
            items.add(existingEntity);
        }
        if(Utilities.isNotEmpty(rolesFonc)){
                 roleFunctionalityRepository.saveAll(rolesFonc);
        }
        if (!items.isEmpty()) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end delete User-----");
        return response;
    }

    @Override
    public Response<FunctionalityDTO> forceDelete(Request<FunctionalityDTO> request, Locale locale) throws ParseException { // cette methode permet de supprimer l'entité ainsi que tout les autres entité rattachées à elle
        log.info("----begin update Functionality-----");
        Response<FunctionalityDTO> response = new Response<FunctionalityDTO>();
        List<Functionality> items = new ArrayList<Functionality>();
        //Verification des permissions
        /*boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_FEATURE_DELETE);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas supprimer des fonctionalités.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }*/
        //Suppresssions forcée
        for(FunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality existingEntity = null;
            existingEntity = functionalityRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Functionality id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            List<RoleFunctionality> listOfRoleFunctionality = roleFunctionalityRepository.findByFunctionalityId(existingEntity.getId(), false);
            if (listOfRoleFunctionality != null && !listOfRoleFunctionality.isEmpty()) {
                Request<RoleFunctionalityDTO> deleteRequest = new Request<RoleFunctionalityDTO>();
                deleteRequest.setDatas(RoleFunctionalityTransformer.INSTANCE.toDtos(listOfRoleFunctionality));
                Response<RoleFunctionalityDTO> deleteResponse = roleFunctionalityBusiness.delete(deleteRequest, locale);
                if (deleteResponse.isHasError()) {
                    response.setStatus(deleteResponse.getStatus());
                    response.setHasError(true);
                    return response;
                }
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            //existingEntity.setDeletedBy(request.userID); //TODO a modifier
            items.add(existingEntity);
        }
        if (!items.isEmpty()) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end delete User-----");
        return response;
    }

    @Override
    public Response<FunctionalityDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<FunctionalityDTO> getFunctionalities(Request<RoleDTO> request, Locale locale) throws Exception {
        Response<FunctionalityDTO> response = new Response<FunctionalityDTO>();
        if(request==null||request.getData()==null){
            response.setStatus(functionalError.FIELD_EMPTY("Aucune donnée", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("code", request.getData().getCode());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String roleCode = request.getData().getCode();
        List<Functionality> functionalities = null;
        functionalities = roleFunctionalityRepository.findFunctionalityByRoleCode(roleCode , false);
        List<FunctionalityDTO> functionalityDtos = FunctionalityTransformer.INSTANCE.toDtos(functionalities);
        response.setItems(functionalityDtos);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("",locale));
        log.info("----end get FUnctionalities by role-----");
        return response;
    }

    @Override
    public Response<FunctionalityDTO> getByCriteria(Request<FunctionalityDTO> request, Locale locale) {

        /*log.info("----begin get Functionality-----");
        Response<FunctionalityDto> response = new Response<FunctionalityDto>();
        //Verification des permissions
        boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_FEATURE_LIST);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas lister les fonctionalités.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }
        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }
        List<Functionality> items = functionalityRepository.getByCriteria(request, em, locale);
        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("Functionality", locale));
            response.setHasError(false);
            return response;
        }
        List<FunctionalityDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? FunctionalityTransformer.INSTANCE.toLiteDtos(items) : FunctionalityTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setCount(functionalityRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Functionality-----");
        return response;*/
        return null;
    }

}
