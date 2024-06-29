package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.StatusUtilCompagnieTransport;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.StatusUtilCompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.StatusUtilRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.StatusUtilCompagnieTransportDTO;
import com.africanb.africanb.helper.transformer.compagnie.StatusUtilCompagnieTransportTransformer;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
@Component
public class StatusUtilCompagnieTransportBusiness implements IBasicBusiness<Request<StatusUtilCompagnieTransportDTO>, Response<StatusUtilCompagnieTransportDTO>> {

    private Response<StatusUtilCompagnieTransportDTO> response;

    private final StatusUtilCompagnieTransportRepository statusUtilCompagnieTransportRepository;
    private final StatusUtilRepository statusUtilRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public StatusUtilCompagnieTransportBusiness(StatusUtilCompagnieTransportRepository statusUtilCompagnieTransportRepository, StatusUtilRepository statusUtilRepository, CompagnieTransportRepository compagnieTransportRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.statusUtilCompagnieTransportRepository = statusUtilCompagnieTransportRepository;
        this.statusUtilRepository = statusUtilRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    @Override
    public Response<StatusUtilCompagnieTransportDTO> create(Request<StatusUtilCompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<StatusUtilCompagnieTransportDTO> response = new Response<>();
        List<StatusUtilCompagnieTransport> items = new ArrayList<>();
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilCompagnieTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(StatusUtilCompagnieTransportDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("compagnieTransportId", dto.getCompagnieTransportId());
            fieldsToVerify.put("statusUtilId", dto.getStatusUtilId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getCompagnieTransportId().equals(dto.getCompagnieTransportId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de compagnie '" + dto.getCompagnieTransportId() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(StatusUtilCompagnieTransportDTO dto : itemsDtos){
            StatusUtilCompagnieTransport existingEntity = statusUtilCompagnieTransportRepository.findByCompagnieTransportAndStatusUtil(dto.getCompagnieTransportId(),dto.getStatusUtilId(),false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("StatusUtilCompagnieTransport ", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieTransport existingCompagnieTransport = null;
            if (Utilities.isValidID(dto.getCompagnieTransportId())) {
                existingCompagnieTransport = compagnieTransportRepository.findOne(dto.getCompagnieTransportId(), false);
                if (existingCompagnieTransport == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("CompagnieTransport CompagnieTransportID -> " + dto.getCompagnieTransportId(), locale));
                    response.setHasError(true);
                    return response;
                }
            }
            StatusUtil existingStatusUtil = null;
            if (Utilities.isValidID(dto.getStatusUtilId())) {
                existingStatusUtil = statusUtilRepository.findOne(dto.getStatusUtilId(), false);
                if (existingStatusUtil == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtil StatusUtilID -> " + dto.getStatusUtilId(), locale));
                    response.setHasError(true);
                    return response;
                }
            }
            StatusUtilCompagnieTransport entityToSave = StatusUtilCompagnieTransportTransformer
                                        .INSTANCE.toEntity(dto, existingCompagnieTransport, existingStatusUtil);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste de données est vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilCompagnieTransport> itemsSaved = statusUtilCompagnieTransportRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("StatusUtilCompagnieTransport", locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilCompagnieTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? StatusUtilCompagnieTransportTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                : StatusUtilCompagnieTransportTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<StatusUtilCompagnieTransportDTO> update(Request<StatusUtilCompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<StatusUtilCompagnieTransportDTO> response = new Response<>();
        List<StatusUtilCompagnieTransport> items = new ArrayList<>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilCompagnieTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(StatusUtilCompagnieTransportDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de StatusUtilCompagnieTransport '" + dto.getCompagnieTransportId() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(StatusUtilCompagnieTransportDTO dto : itemsDtos){
            StatusUtilCompagnieTransport entityToSave = statusUtilCompagnieTransportRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilCompagnieTransport id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isValidID(dto.getCompagnieTransportId()) && !entityToSave.getCompagnieTransport().getId().equals(dto.getCompagnieTransportId())) {
                CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findOne(dto.getCompagnieTransportId(), false);
                if (existingCompagnieTransport == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("CompagnieTRansport CompagnieTransportId -> " + dto.getCompagnieTransportId(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setCompagnieTransport(existingCompagnieTransport);
            }
            if (Utilities.isValidID(dto.getStatusUtilId()) && !entityToSave.getStatusUtil().getId().equals(dto.getStatusUtilId())) {
                StatusUtil existingStatusUtil = statusUtilRepository.findOne(dto.getStatusUtilId(), false);
                if (existingStatusUtil == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtil StatusUtilId -> " + dto.getStatusUtilId(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setStatusUtil(existingStatusUtil);
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification ",locale));
            response.setHasError(true);
        }
        List<StatusUtilCompagnieTransport> itemsSaved = statusUtilCompagnieTransportRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
                response.setStatus(functionalError.SAVE_FAIL("StatusUtilCompagnieTransport", locale));
                response.setHasError(true);
                return response;
        }

        List<StatusUtilCompagnieTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? StatusUtilCompagnieTransportTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : StatusUtilCompagnieTransportTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<StatusUtilCompagnieTransportDTO> delete(Request<StatusUtilCompagnieTransportDTO> request, Locale locale) {
        Response<StatusUtilCompagnieTransportDTO> response = new Response<>();
        List<StatusUtilCompagnieTransport> items = new ArrayList<>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        for(StatusUtilCompagnieTransportDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        for(StatusUtilCompagnieTransportDTO dto : request.getDatas()){
            StatusUtilCompagnieTransport existingEntity = statusUtilCompagnieTransportRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilCompagnieTransport id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            items.add(existingEntity);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<StatusUtilCompagnieTransportDTO> forceDelete(Request<StatusUtilCompagnieTransportDTO> request, Locale locale) {
        return new Response<>();
    }

    @Override
    public Response<StatusUtilCompagnieTransportDTO> getAll(Locale locale) throws ParseException {
        return new Response<>();
    }

    @Override
    public Response<StatusUtilCompagnieTransportDTO> getByCriteria(Request<StatusUtilCompagnieTransportDTO> request, Locale locale) {
        return new Response<>();
    }
}
