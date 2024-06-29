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

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;



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
        Response<RoleDTO> response = new Response<>();
        List<Role> items = new ArrayList<>();
        List<RoleDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        blockDuplicationData(request, locale, response, itemsDtos);
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("code", dto.getCode());
            fieldsToVerify.put("libelle", dto.getLibelle());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role existingEntity = roleRepository.findByCode(dto.getCode(), false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("Role code -> " + dto.getCode(), locale));
                response.setHasError(true);
                return response;
            }
            Role entityToSave = RoleTransformer.INSTANCE.toEntity(dto);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            Role entitySaved = roleRepository.save(entityToSave);
            items.add(entitySaved);
            if(CollectionUtils.isEmpty(dto.getDatasFunctionalities())){
                response.setStatus(functionalError.FIELD_EMPTY("aucune fonctionnalité pour le rôle", locale));
                response.setHasError(true);
                return response;
            }
            else {
                List<RoleFunctionalityDTO> datasProfilFunctionality = new ArrayList<>();
                dto.getDatasFunctionalities().forEach(f -> {
                    RoleFunctionalityDTO roleFunctionalityDto = new RoleFunctionalityDTO();
                    roleFunctionalityDto.setRoleCode(entitySaved.getCode());
                    roleFunctionalityDto.setFunctionalityCode(f.getCode());
                    datasProfilFunctionality.add(roleFunctionalityDto);
                });
                Request<RoleFunctionalityDTO> subRequest = new Request<>();
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
            List<Role>  itemsSaved = roleRepository.saveAll(items);
            List<RoleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? RoleTransformer.INSTANCE.toLiteDtos(itemsSaved) : RoleTransformer.INSTANCE.toDtos(itemsSaved);
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end create Role-----");
        return response;
    }

    private void blockDuplicationData(Request<RoleDTO> request, Locale locale, Response<RoleDTO> response, List<RoleDTO> itemsDtos) {
        for(RoleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("code", dto.getCode());
            fieldsToVerify.put("libelle", dto.getLibelle());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return;
            }
            if(itemsDtos.stream().anyMatch(a->a.getCode().equalsIgnoreCase(dto.getCode()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du code '" + dto.getCode() + "' pour les rôles", locale));
                response.setHasError(true);
                return;
            }
            itemsDtos.add(dto);
        }
    }

    @Override
    public Response<RoleDTO> update(Request<RoleDTO> request, Locale locale) throws ParseException {
        Response<RoleDTO> response = new Response<>();
        List<Role> items = new ArrayList<>();
        List<RoleDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        blockDuplicationDataUpdate(request, locale, response, itemsDtos);
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role entityToSave = roleRepository.findOne(dto.getId(), false);
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
                List<RoleFunctionality> list = roleFunctionalityRepository.findByRoleId(entityToSaveId, false);
                if(Utilities.isNotEmpty(list)) {
                    for (RoleFunctionality rf : list) {
                        RoleFunctionalityDTO itemsData = RoleFunctionalityTransformer.INSTANCE.toDto(rf);
                        Request<RoleFunctionalityDTO> subRequest = new Request<>();
                        subRequest.setDatas(Collections.singletonList(itemsData));
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
                    roleFunctionalityDto.setRoleId(entityToSaveId);
                    roleFunctionalityDto.setFunctionalityId(f.getId());
                    Request<RoleFunctionalityDTO> subRequests = new Request<>();
                    subRequests.setDatas(List.of(roleFunctionalityDto));
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
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<Role> itemsSaved = roleRepository.saveAll(items);
            List<RoleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? RoleTransformer.INSTANCE.toLiteDtos(itemsSaved) : RoleTransformer.INSTANCE.toDtos(itemsSaved);
            for(RoleDTO roleDto : itemsDto){
                try{
                    getFullInfos(roleDto);
                }catch (Exception e){
                     e.printStackTrace();
                }
            }
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        return response;
    }

    @Override
    public Response<RoleDTO> delete(Request<RoleDTO> request, Locale locale) {
        log.info("----begin delete Role-----");
        Response<RoleDTO> response = new Response<>();
        List<Role> items = new ArrayList<>();
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role existingEntity = roleRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Role id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
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
        Response<RoleDTO> response = new Response<>();
        List<Role> items = new ArrayList<>();
        for(RoleDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            Role existingEntity = roleRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Role id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            List<RoleFunctionality> listOfRoleFunctionality = roleFunctionalityRepository.findByRoleId(existingEntity.getId(), false);
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
        log.info("----end Forcedelete Role-----");
        return response;
    }

    @Override
    public Response<RoleDTO> getAll(Locale locale) throws ParseException {
        Response<RoleDTO> response = new Response<>();
        List<Role> roles = roleRepository.findByIsDeleted(false);
        response.setItems(RoleTransformer.INSTANCE.toDtos(roles));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("",locale));
        return response;
    }

    private void blockDuplicationDataUpdate(Request<RoleDTO> request, Locale locale, Response<RoleDTO> response, List<RoleDTO> itemsDtos) {
        for(RoleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de l'id '" + dto.getCode() + "' pour les fonctionnalités", locale));
                response.setHasError(true);
                return;
            }
            if(Utilities.isNotBlank(dto.getCode())){
                itemsDtos = itemsDtos.stream().filter(a-> Utilities.isNotBlank(a.getCode())).collect(Collectors.toList());
                if(Utilities.isNotEmpty(itemsDtos)){
                    if(itemsDtos.stream().anyMatch(a->a.getCode().equals(dto.getCode()))){
                        response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du code '" + dto.getCode() + "' pour les fonctionnalités", locale));
                        response.setHasError(true);
                        return;
                    }
                }
            }
            itemsDtos.add(dto);
        }
    }

    @Override
    public Response<RoleDTO> getByCriteria(Request<RoleDTO> request, Locale locale) {
        return null;
    }

    private void getFullInfos(RoleDTO dto) throws Exception {
          List<Functionality> functionalities = roleFunctionalityRepository.findFunctionalityByRoleId(dto.getId(), Boolean.FALSE);
          if(Utilities.isNotEmpty(functionalities)){
                  List<FunctionalityDTO> functionalityDtos = FunctionalityTransformer.INSTANCE.toDtos(functionalities);
                  dto.setDatasFunctionalities(functionalityDtos);
          }
    }
}