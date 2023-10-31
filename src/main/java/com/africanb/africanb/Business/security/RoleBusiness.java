package com.africanb.africanb.Business.security;


import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.RoleFunctionality;
import com.africanb.africanb.dao.repository.security.RoleFunctionalityRepository;
import com.africanb.africanb.dao.repository.security.RoleRepository;
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
import com.africanb.africanb.helper.transformer.security.RoleTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Alzouma Moussa Mahamadou
 */

@Log
@Component
public class RoleBusiness implements IBasicBusiness<Request<RoleDTO>, Response<RoleDTO>> {

    private Response<RoleDTO> response;

    private final RoleRepository roleRepository;
    private final RoleFunctionalityRepository roleFunctionalityRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final RoleFunctionalityBusiness roleFunctionalityBusiness;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public RoleBusiness(RoleRepository roleRepository, RoleFunctionalityRepository roleFunctionalityRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em, RoleFunctionalityBusiness roleFunctionalityBusiness) {
        this.roleRepository = roleRepository;
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
    public Response<RoleDTO> create(Request<RoleDTO> request, Locale locale) throws ParseException {
        log.info("----begin create Role-----");
        Response<RoleDTO> response = new Response<RoleDTO>();
        List<Role> items = new ArrayList<Role>();
        List<RoleDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<RoleDTO>());
        //Verification des permissions
        /*boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_PROFILE_CREATE);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas créer un rôle.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }*/
        Response<RoleDTO> response1 = blockDuplicationData(request, locale, response, itemsDtos);
        if (response1 != null) return response1;
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("code", dto.getCode());
            fieldsToVerify.put("libelle", dto.getLibelle());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role existingEntity = roleRepository.findByCode(dto.getCode(), false); //verification en base
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("Role code -> " + dto.getCode(), locale));
                response.setHasError(true);
                return response;
            }
            Role entityToSave = RoleTransformer.INSTANCE.toEntity(dto);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //if(Utilities.isValidID(request.userID)){
            //entityToSave.setCreatedBy(request.userID);
            //} //TODO A mettre à jour
            Role entitySaved = roleRepository.save(entityToSave);
            items.add(entitySaved);
            if(CollectionUtils.isEmpty(dto.getDatasFunctionalities())){
                response.setStatus(functionalError.FIELD_EMPTY("aucune fonctionnalité pour le rôle", locale));
                response.setHasError(true);
                return response;
            }
            else {
                List<RoleFunctionalityDTO> datasProfilFunctionality = new ArrayList<RoleFunctionalityDTO>();
                dto.getDatasFunctionalities().forEach(f -> {
                    RoleFunctionalityDTO roleFunctionalityDto = new RoleFunctionalityDTO();
                    roleFunctionalityDto.setRoleCode(entitySaved.getCode());
                    roleFunctionalityDto.setFunctionalityCode(f.getCode());
                    datasProfilFunctionality.add(roleFunctionalityDto);
                });
                Request<RoleFunctionalityDTO> subRequest = new Request<RoleFunctionalityDTO>();
                subRequest.setDatas(datasProfilFunctionality);
                Response<RoleFunctionalityDTO> subResponse = roleFunctionalityBusiness.create(subRequest, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(true);
                    return response;
                }
            }
        }
        if (!items.isEmpty()) {
            List<Role>  itemsSaved = roleRepository.saveAll((Iterable<Role>) items);
            if (itemsSaved == null) {
                response.setStatus(functionalError.SAVE_FAIL("Role", locale));
                response.setHasError(true);
                return response;
            }
            List<RoleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? RoleTransformer.INSTANCE.toLiteDtos(itemsSaved) : RoleTransformer.INSTANCE.toDtos(itemsSaved);
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end create Role-----");
        return response;
    }

    private Response<RoleDTO> blockDuplicationData(Request<RoleDTO> request, Locale locale, Response<RoleDTO> response, List<RoleDTO> itemsDtos) {
        for(RoleDTO dto: request.getDatas() ) {
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
            itemsDtos.add(dto);
        }
        return null;
    }

    @Override
    public Response<RoleDTO> update(Request<RoleDTO> request, Locale locale) throws ParseException {
        Response<RoleDTO> response = new Response<RoleDTO>();
        List<Role> items = new ArrayList<Role>();
        List<RoleDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<RoleDTO>());
       /* boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_PROFILE_UPDATE);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas modifier rôle.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }*/
        Response<RoleDTO> response1 = blockDuplicationDataUpdate(request, locale, response, itemsDtos);
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role entityToSave = null;
            entityToSave = roleRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Role id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            Long entityToSaveId = entityToSave.getId();
            if (Utilities.isNotBlank(dto.getCode()) && !dto.getCode().equals(entityToSave.getCode())) { //verify code
                Role existingEntity = roleRepository.findByCode(dto.getCode(), false);
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Role code -> " + dto.getCode(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setCode(dto.getCode());
            }
            if (Utilities.isNotBlank(dto.getLibelle()) && !dto.getLibelle().equals(entityToSave.getLibelle())) { //verify libelle
                Role existingEntity = roleRepository.findByLibelle(dto.getLibelle(), false);
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Role -> " + dto.getLibelle(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setLibelle(dto.getLibelle());
            }
            if (Utilities.isNotEmpty(dto.getDatasFunctionalities())) {
                List<RoleFunctionality> list = roleFunctionalityRepository
                        .findByRoleId(entityToSaveId, false);
                if(Utilities.isNotEmpty(list)) {
                    for (RoleFunctionality rf : list) {
                        RoleFunctionalityDTO itemsData = RoleFunctionalityTransformer.INSTANCE.toDto(rf);
                        Request<RoleFunctionalityDTO> subRequest = new Request<RoleFunctionalityDTO>();
                        subRequest.setDatas(Arrays.asList(itemsData));
                        Response<RoleFunctionalityDTO> subResponse = roleFunctionalityBusiness.delete(subRequest, locale);
                        if (subResponse.isHasError()) {
                            response.setStatus(subResponse.getStatus());
                            response.setHasError(true);
                            return response;
                        }
                    }
                }
                for (FunctionalityDTO f : dto.getDatasFunctionalities()) {
                    RoleFunctionalityDTO roleFunctionalityDto = new RoleFunctionalityDTO();
                    roleFunctionalityDto.setRoleId(Long.valueOf(entityToSaveId));
                    roleFunctionalityDto.setFunctionalityId(f.getId());
                    Request<RoleFunctionalityDTO> subRequests = new Request<RoleFunctionalityDTO>();
                    subRequests.setDatas(Arrays.asList(roleFunctionalityDto));
                    Response<RoleFunctionalityDTO> subResponses = roleFunctionalityBusiness.create(subRequests,
                            locale);
                    if (subResponses.isHasError()) {
                        response.setStatus(subResponses.getStatus());
                        response.setHasError(true);
                        return response;
                    }
                }
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.userID); //TODO A mettre à jour
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<Role> itemsSaved = null;
            itemsSaved = roleRepository.saveAll((Iterable<Role>) items);
            if (itemsSaved == null) {
                response.setStatus(functionalError.SAVE_FAIL("Role", locale));
                response.setHasError(true);
                return response;
            }
            List<RoleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? RoleTransformer.INSTANCE.toLiteDtos(itemsSaved) : RoleTransformer.INSTANCE.toDtos(itemsSaved);
            for(RoleDTO roleDto : itemsDto){
                try{
                    roleDto = getFullInfos(roleDto,1,Boolean.FALSE,locale);
                }catch (Exception e){
                     e.printStackTrace();
                }
            }
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end update Role-----");
        return response;
    }

    @Override
    public Response<RoleDTO> delete(Request<RoleDTO> request, Locale locale) {
        log.info("----begin delete Role-----");
        Response<RoleDTO> response = new Response<RoleDTO>();
        List<Role> items = new ArrayList<Role>();
        //Verification des permissions
       /* boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_PROFILE_DELETE);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas modifier rôle.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }*/
        //Suppression
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role existingEntity = null;
            existingEntity = roleRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Role id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            //existingEntity.setDeletedBy(request.userID); //TODO A mettre à jour
            items.add(existingEntity);
        }
        if (!items.isEmpty()) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end delete Role-----");
        return response;
    }

    @Override
    public Response<RoleDTO> forceDelete(Request<RoleDTO> request, Locale locale) throws ParseException {
        Response<RoleDTO> response = new Response<RoleDTO>();
        List<Role> items = new ArrayList<Role>();
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role existingEntity = null;
            existingEntity = roleRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Role id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            List<RoleFunctionality> listOfRoleFunctionality = roleFunctionalityRepository.findByRoleId(existingEntity.getId(), false);
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
            //existingEntity.setDeletedBy(request.userID); //TODO A mettre à jour
            items.add(existingEntity);
        }
        if (!items.isEmpty()) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end Forcedelete Role-----");
        return response;
    }

    @Override
    public Response<RoleDTO> getAll(Locale locale) throws ParseException {
        Response<RoleDTO> response = new Response<RoleDTO>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        List<Role> roles = null;
        roles = roleRepository.findByIsDeleted(false);
        List<RoleDTO> roleDtos = RoleTransformer.INSTANCE.toDtos(roles);
        response.setItems(roleDtos);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("",locale));
        log.info("----end get role -----");
        return response;
    }

    private Response<RoleDTO> blockDuplicationDataUpdate(Request<RoleDTO> request, Locale locale, Response<RoleDTO> response, List<RoleDTO> itemsDtos) {
        for(RoleDTO dto: request.getDatas() ) {
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
    public Response<RoleDTO> getByCriteria(Request<RoleDTO> request, Locale locale) {
        log.info("----begin get Role-----");
        /*Response<RoleDto> response = new Response<RoleDto>();
        //Verification des permissions
        boolean isUserAuthenticatedHaveFunctinality=usersBusiness.checkIfUserAuthenticatedHasThisFunctionnality(Request.userID, SecurityConstants.PERMISSION_PROFILE_LIST);
        if(isUserAuthenticatedHaveFunctinality==false){
            response.setStatus(functionalError.SAVE_FAIL("Vous ne pouvez pas lister les rôles.Car, vous n'avez pas les permissions nécessaires", locale));
            response.setHasError(true);
            return response;
        }
        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }
        List<Role> items = roleRepository.getByCriteria(request, em, locale);
        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("Role", locale));
            response.setHasError(false);
            return response;
        }
        List<RoleDto> itemsDto = Collections.synchronizedList(new ArrayList<RoleDto>());
         itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? RoleTransformer.INSTANCE.toLiteDtos(items) : RoleTransformer.INSTANCE.toDtos(items);
         itemsDto.parallelStream().forEach(i->{
             try {
                 i=getFullInfos(i,items.size(), Boolean.FALSE,locale);
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }
         });
        response.setItems(itemsDto);
        response.setCount(roleRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Role-----");
        return response;*/
        return null;
    }

    private RoleDTO getFullInfos(RoleDTO dto, Integer size, Boolean isSimpleLoading,Locale locale) throws Exception {
        // put code here
          List<Functionality> functionalities = roleFunctionalityRepository.findFunctionalityByRoleId(dto.getId(), Boolean.FALSE);
          if(Utilities.isNotEmpty(functionalities)){
                  List<FunctionalityDTO> functionalityDtos = FunctionalityTransformer.INSTANCE.toDtos(functionalities);
                  dto.setDatasFunctionalities(functionalityDtos);
          }
          if (Utilities.isTrue(isSimpleLoading)) {
             return dto;
          }
          if (size > 1) {
            return dto;
          }
        return dto;
    }
}