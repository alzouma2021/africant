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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log
@Component
public class FunctionalityBusiness implements IBasicBusiness<Request<FunctionalityDTO>, Response<FunctionalityDTO>> {

    private Response<FunctionalityDTO> response;

    private final FunctionalityRepository functionalityRepository;

    private final RoleFunctionalityRepository roleFunctionalityRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final RoleFunctionalityBusiness roleFunctionalityBusiness;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public FunctionalityBusiness(FunctionalityRepository functionalityRepository, RoleFunctionalityRepository roleFunctionalityRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em, RoleFunctionalityBusiness roleFunctionalityBusiness) {
        this.functionalityRepository = functionalityRepository;
        this.roleFunctionalityRepository = roleFunctionalityRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        this.roleFunctionalityBusiness = roleFunctionalityBusiness;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    @Override
    public Response<FunctionalityDTO> create(Request<FunctionalityDTO> request, Locale locale) throws ParseException {
        log.info("----begin create Functionality-----");
        Response<FunctionalityDTO> response = new Response<>();
        List<Functionality> items = new ArrayList<>();
        List<FunctionalityDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        blockDuplicationData(request, locale, response, itemsDtos);
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
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<Functionality> itemsSaved = functionalityRepository.saveAll(items);
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
            Map<String, Object> fieldsToVerify = new HashMap<>();
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
            Map<String, Object> fieldsToVerify = new HashMap<>();
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
        Response<FunctionalityDTO> response = new Response<>();
        List<Functionality> items = new ArrayList<>();
        List<FunctionalityDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        blockDuplicationDataUpdate(request, locale, response, itemsDtos);
        for(FunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality entityToSave = functionalityRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Functionality id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            Long entityToSaveId = entityToSave.getId();
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
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<Functionality> itemsSaved = functionalityRepository.saveAll(items);
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
        Response<FunctionalityDTO> response = new Response<>();
        List<Functionality> items = new ArrayList<>();
        List<RoleFunctionality> rolesFonc = Collections.synchronizedList(new ArrayList<>());
        for(FunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality existingEntity = functionalityRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Functionality id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            List<RoleFunctionality> roleFunctionalities = roleFunctionalityRepository.findByFunctionalityId(dto.getId(), Boolean.FALSE);
            if(Utilities.isNotEmpty(roleFunctionalities)){
                  roleFunctionalities.forEach(rf->{
                      rf.setIsDeleted(Boolean.TRUE);
                      rf.setDeletedAt(Utilities.getCurrentDate());
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
        Response<FunctionalityDTO> response = new Response<>();
        List<Functionality> items = new ArrayList<>();
        for(FunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality existingEntity = functionalityRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Functionality id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            List<RoleFunctionality> listOfRoleFunctionality = roleFunctionalityRepository.findByFunctionalityId(existingEntity.getId(), false);
            if (listOfRoleFunctionality != null && !listOfRoleFunctionality.isEmpty()) {
                Request<RoleFunctionalityDTO> deleteRequest = new Request<>();
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
        Response<FunctionalityDTO> response = new Response<>();
        List<Functionality> functionalities = functionalityRepository.findByIsDeleted(false);
        List<FunctionalityDTO> functionalityDtos = FunctionalityTransformer.INSTANCE.toDtos(functionalities);
        response.setItems(functionalityDtos);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("",locale));
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<FunctionalityDTO> getFunctionalitiesByRole(Request<RoleDTO> request, Locale locale) throws Exception {
        Response<FunctionalityDTO> response = new Response<>();
        if(request==null||request.getData()==null){
            response.setStatus(functionalError.FIELD_EMPTY("Aucune donnée", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("code", request.getData().getCode());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String roleCode = request.getData().getCode();
        List<Functionality> functionalities = roleFunctionalityRepository.findFunctionalityByRoleCode(roleCode , false);
        List<FunctionalityDTO> functionalityDtos = FunctionalityTransformer.INSTANCE.toDtos(functionalities);
        response.setItems(functionalityDtos);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("",locale));
        return response;
    }

    @Override
    public Response<FunctionalityDTO> getByCriteria(Request<FunctionalityDTO> request, Locale locale) {
        return null;
    }

}
