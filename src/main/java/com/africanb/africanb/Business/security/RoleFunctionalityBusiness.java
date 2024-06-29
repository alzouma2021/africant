package com.africanb.africanb.Business.security;


import com.africanb.africanb.dao.entity.security.Functionality;
import com.africanb.africanb.dao.entity.security.Role;
import com.africanb.africanb.dao.entity.security.RoleFunctionality;
import com.africanb.africanb.dao.repository.security.FunctionalityRepository;
import com.africanb.africanb.dao.repository.security.RoleFunctionalityRepository;
import com.africanb.africanb.dao.repository.security.RoleRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.RoleFunctionalityDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.security.RoleFunctionalityTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Log
@Component
public class RoleFunctionalityBusiness implements IBasicBusiness<Request<RoleFunctionalityDTO>, Response<RoleFunctionalityDTO>> {

    private final RoleFunctionalityRepository roleFunctionalityRepository;
    private final RoleRepository roleRepository;
    private final FunctionalityRepository functionalityRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public RoleFunctionalityBusiness(RoleFunctionalityRepository roleFunctionalityRepository, RoleRepository roleRepository, FunctionalityRepository functionalityRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.roleFunctionalityRepository = roleFunctionalityRepository;
        this.roleRepository = roleRepository;
        this.functionalityRepository = functionalityRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<RoleFunctionalityDTO> create(Request<RoleFunctionalityDTO> request, Locale locale) throws ParseException {
        Response<RoleFunctionalityDTO> response = new Response<>();
        List<RoleFunctionality> items = new ArrayList<>();
        for(RoleFunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("roleCode", dto.getRoleCode());
            fieldsToVerify.put("functionalityCode", dto.getFunctionalityCode());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            RoleFunctionality existingEntity = roleFunctionalityRepository.findByRoleAndFunctionalityCode(dto.getRoleCode(),dto.getFunctionalityCode(), false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("RoleFunctionality roleCode -> " + dto.getRoleCode() + "RoleFunctionality FunctionalityCode -> " + dto.getFunctionalityCode(), locale));
                response.setHasError(true);
                return response;
            }
            Role existingRole = roleRepository.findByCode(dto.getRoleCode(), false);
            if (existingRole == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Role roleCode -> " + dto.getRoleCode(), locale));
                response.setHasError(true);
                return response;
            }
            Functionality existingFunctionality = functionalityRepository.findByCode(dto.getFunctionalityCode(), false);
            if (existingFunctionality == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Functionality FunctionalityCode -> " + dto.getFunctionalityCode() , locale));
                response.setHasError(true);
                return response;
            }
            RoleFunctionality entityToSave = RoleFunctionalityTransformer.INSTANCE.toEntity(dto, existingFunctionality, existingRole);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if (!items.isEmpty()) {
            List<RoleFunctionality> itemsSaved = roleFunctionalityRepository.saveAll(items);
            List<RoleFunctionalityDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading())) ? RoleFunctionalityTransformer.INSTANCE.toLiteDtos(itemsSaved) : RoleFunctionalityTransformer.INSTANCE.toDtos(itemsSaved);
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end create Role-----");
        return response;
    }

    @Override
    public Response<RoleFunctionalityDTO> update(Request<RoleFunctionalityDTO> request, Locale locale) throws ParseException {
        Response<RoleFunctionalityDTO> response = new Response<>();
        List<RoleFunctionality> items = new ArrayList<>();
        for(RoleFunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            RoleFunctionality entityToSave = roleFunctionalityRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("RoleFunctionality id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            RoleFunctionalityDTO entityToSaveDto = RoleFunctionalityTransformer.INSTANCE.toDto(entityToSave);
            Role existingRole = entityToSave.getRole();
            if (Utilities.isValidID(dto.getRoleId()) && !entityToSave.getRole().getId().equals(dto.getRoleId())) {
                existingRole = roleRepository.findOne(dto.getRoleId(), false);
                if (existingRole == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("role roleId -> " + dto.getRoleId(), locale));
                    response.setHasError(true);
                    return response;
                }
            }
            Functionality existingFunctionality = entityToSave.getFunctionality();
            if (Utilities.isValidID(dto.getFunctionalityId()) && !entityToSave.getFunctionality().getId().equals(dto.getFunctionalityId())) {
                existingFunctionality = functionalityRepository.findOne(dto.getFunctionalityId(), false);
                if (existingFunctionality == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("functionality functionalityId -> " + dto.getFunctionalityId(), locale));
                    response.setHasError(true);
                    return response;
                }
            }
            RoleFunctionality roleFunctionalityEntity = RoleFunctionalityTransformer.INSTANCE.toEntity(entityToSaveDto, existingFunctionality, existingRole);
            roleFunctionalityEntity.setUpdatedAt(Utilities.getCurrentDate());
            items.add(roleFunctionalityEntity);
        }
        if (!items.isEmpty()) {
            List<RoleFunctionality> itemsSaved = roleFunctionalityRepository.saveAll(items);
            List<RoleFunctionalityDTO> itemsDto =  RoleFunctionalityTransformer.INSTANCE.toDtos(itemsSaved);
            response.setItems(itemsDto);
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("end RoleFunctionality update");
        return response;
    }

    @Override
    public Response<RoleFunctionalityDTO> delete(Request<RoleFunctionalityDTO> request, Locale locale) {
        Response<RoleFunctionalityDTO> response = new Response<>();
        List<RoleFunctionality> items = new ArrayList<>();
        for(RoleFunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            RoleFunctionality existingEntity = roleFunctionalityRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("RoleFunctionality id -> " + dto.getId(), locale));
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
        return response;
    }

    @Override
    public Response<RoleFunctionalityDTO> forceDelete(Request<RoleFunctionalityDTO> request, Locale locale) {
        Response<RoleFunctionalityDTO> response = new Response<>();
        List<RoleFunctionality> items = new ArrayList<>();
        for(RoleFunctionalityDTO dto : request.getDatas()){
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            RoleFunctionality existingEntity = roleFunctionalityRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("RoleFunctionality id -> " + dto.getId(), locale));
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
        log.info("----end forcedelete RoleFunctionality-----");
        return response;
    }

    @Override
    public Response<RoleFunctionalityDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<RoleFunctionalityDTO> getByCriteria(Request<RoleFunctionalityDTO> request, Locale locale) {
        return null;
    }
}
