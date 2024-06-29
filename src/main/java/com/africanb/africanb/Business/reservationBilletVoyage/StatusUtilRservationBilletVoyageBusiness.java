package com.africanb.africanb.Business.reservationBilletVoyage;


import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.StatusUtilReservationBilletVoyage;
import com.africanb.africanb.dao.repository.compagnie.StatusUtilRepository;
import com.africanb.africanb.dao.repository.reservationBilletVoyage.ReservationBilletVoyageRepository;
import com.africanb.africanb.dao.repository.reservationBilletVoyage.StatusUtilReservationBilletVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.StatusUtilReservationBilletVoyageDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.reservationBilletVoyage.StatusUtilReservationBilletVoyageTransformer;
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
public class StatusUtilRservationBilletVoyageBusiness implements IBasicBusiness<Request<StatusUtilReservationBilletVoyageDTO>, Response<StatusUtilReservationBilletVoyageDTO>> {

    private final StatusUtilReservationBilletVoyageRepository statusUtilReservationBilletVoyageRepository;
    private final StatusUtilRepository statusUtilRepository;
    private final ReservationBilletVoyageRepository reservationBilletVoyageRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;


    public StatusUtilRservationBilletVoyageBusiness(StatusUtilReservationBilletVoyageRepository statusUtilReservationBilletVoyageRepository, StatusUtilRepository statusUtilRepository, ReservationBilletVoyageRepository reservationBilletVoyageRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.statusUtilReservationBilletVoyageRepository = statusUtilReservationBilletVoyageRepository;
        this.statusUtilRepository = statusUtilRepository;
        this.reservationBilletVoyageRepository = reservationBilletVoyageRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    @Override
    public Response<StatusUtilReservationBilletVoyageDTO> create(Request<StatusUtilReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        Response<StatusUtilReservationBilletVoyageDTO> response = new Response<>();
        List<StatusUtilReservationBilletVoyage> items = new ArrayList<>();
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilReservationBilletVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(StatusUtilReservationBilletVoyageDTO dto: request.getDatas() ) {
            Response<StatusUtilReservationBilletVoyageDTO> responseCheckDTO = checkStatusUtilReservationBilletVoyageDTO(locale, response, itemsDtos, dto);
            if (responseCheckDTO != null) return responseCheckDTO;
            itemsDtos.add(dto);
        }
        for(StatusUtilReservationBilletVoyageDTO dto : itemsDtos){
            StatusUtilReservationBilletVoyage existingEntity = statusUtilReservationBilletVoyageRepository.findByReservationBilletVoyageAndStatusUtil(dto.getReservationBilletVoyageDesignation(),dto.getStatusUtilDesignation());
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("StatusUtilReservationBilletVoyage  ", locale));
                response.setHasError(true);
                return response;
            }
            ReservationBilletVoyage existingReservationBilletVoyage = null;
            if (Utilities.isBlank(dto.getReservationBilletVoyageDesignation())) {
                existingReservationBilletVoyage = reservationBilletVoyageRepository.findByDesignation(dto.getReservationBilletVoyageDesignation(), false);
                if (existingReservationBilletVoyage == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("ReservationBilletTransport inexistante -> " + dto.getReservationBilletVoyageDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
            }
            StatusUtil existingStatusUtil = null;
            if (Utilities.isBlank(dto.getStatusUtilDesignation())) {
                existingStatusUtil = statusUtilRepository.findByDesignation(dto.getStatusUtilDesignation(), false);
                if (existingStatusUtil == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtil inexistant-> " + dto.getStatusUtilDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
            }
            StatusUtilReservationBilletVoyage entityToSave = StatusUtilReservationBilletVoyageTransformer
                                        .INSTANCE.toEntity(dto, existingReservationBilletVoyage, existingStatusUtil);
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilReservationBilletVoyage> itemsSaved = statusUtilReservationBilletVoyageRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("StatusUtilReservationVilleVoyage", locale));
            response.setHasError(true);
            return response;
        }

        List<StatusUtilReservationBilletVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? StatusUtilReservationBilletVoyageTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                : StatusUtilReservationBilletVoyageTransformer.INSTANCE.toDtos(itemsSaved);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<StatusUtilReservationBilletVoyageDTO> update(Request<StatusUtilReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        Response<StatusUtilReservationBilletVoyageDTO> response = new Response<>();
        List<StatusUtilReservationBilletVoyage> items = new ArrayList<>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilReservationBilletVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(StatusUtilReservationBilletVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de StatusUtilReservationBilletVoyage '" + dto.getId() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(StatusUtilReservationBilletVoyageDTO dto : itemsDtos){
            StatusUtilReservationBilletVoyage entityToSave = statusUtilReservationBilletVoyageRepository.findOne(dto.getId());
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilReservationBilletVoyage id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isBlank(dto.getReservationBilletVoyageDesignation()) && !entityToSave.getReservationBilletVoyage().getDesignation().equalsIgnoreCase(dto.getReservationBilletVoyageDesignation())) {
                ReservationBilletVoyage existingReservationBilletVoyage = reservationBilletVoyageRepository.findByDesignation(dto.getReservationBilletVoyageDesignation(), false);
                if (existingReservationBilletVoyage == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("ReservationBilletVoyage -> " + dto.getReservationBilletVoyageDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setReservationBilletVoyage(existingReservationBilletVoyage);
            }
            StatusUtil existingStatusUtil;
            if (Utilities.isBlank(dto.getStatusUtilDesignation()) && !entityToSave.getStatusUtil().getDesignation().equalsIgnoreCase(dto.getStatusUtilDesignation())) {
                existingStatusUtil = statusUtilRepository.findByDesignation(dto.getStatusUtilDesignation(), false);
                if (existingStatusUtil == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtil StatusUtilDesignation -> " + dto.getStatusUtilDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setStatusUtil(existingStatusUtil);
            }
            items.add(entityToSave);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification ",locale));
            response.setHasError(true);
        }
        List<StatusUtilReservationBilletVoyage> itemsSaved = statusUtilReservationBilletVoyageRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
                response.setStatus(functionalError.SAVE_FAIL("StatusUtilReservationBilletVoyage", locale));
                response.setHasError(true);
                return response;
        }
        List<StatusUtilReservationBilletVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? StatusUtilReservationBilletVoyageTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : StatusUtilReservationBilletVoyageTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<StatusUtilReservationBilletVoyageDTO> delete(Request<StatusUtilReservationBilletVoyageDTO> request, Locale locale) {
        Response<StatusUtilReservationBilletVoyageDTO> response = new Response<>();
        List<StatusUtilReservationBilletVoyage> items = new ArrayList<>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        for(StatusUtilReservationBilletVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        for(StatusUtilReservationBilletVoyageDTO dto : request.getDatas()){
            StatusUtilReservationBilletVoyage existingEntity = statusUtilReservationBilletVoyageRepository.findOne(dto.getId());
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilReservationBillet id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
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

    private Response<StatusUtilReservationBilletVoyageDTO> checkStatusUtilReservationBilletVoyageDTO(Locale locale, Response<StatusUtilReservationBilletVoyageDTO> response, List<StatusUtilReservationBilletVoyageDTO> itemsDtos, StatusUtilReservationBilletVoyageDTO dto) {
        Map<String, Object> fieldsToVerify = createFieldsToVerifyMap(dto);
        if (!validateFields(fieldsToVerify, response, locale)) {
            return response;
        }
        if (containsDuplicateReservation(itemsDtos, dto)) {
            response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de reservation '" + dto.getReservationBilletVoyageDesignation(), locale));
            response.setHasError(true);
            return response;
        }
        return null;
    }

    private Map<String, Object> createFieldsToVerifyMap(StatusUtilReservationBilletVoyageDTO dto) {
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("reservationBilletDesignation", dto.getReservationBilletVoyageDesignation());
        fieldsToVerify.put("statusUtilDesignation", dto.getStatusUtilDesignation());
        return fieldsToVerify;
    }

    private boolean validateFields(Map<String, Object> fieldsToVerify, Response<StatusUtilReservationBilletVoyageDTO> response, Locale locale) {
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return false;
        }
        return true;
    }

    private boolean containsDuplicateReservation(List<StatusUtilReservationBilletVoyageDTO> itemsDtos, StatusUtilReservationBilletVoyageDTO dto) {
        return itemsDtos.stream().anyMatch(a -> a.getReservationBilletVoyageDesignation().equals(dto.getReservationBilletVoyageDesignation()));
    }

    @Override
    public Response<StatusUtilReservationBilletVoyageDTO> forceDelete(Request<StatusUtilReservationBilletVoyageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<StatusUtilReservationBilletVoyageDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<StatusUtilReservationBilletVoyageDTO> getByCriteria(Request<StatusUtilReservationBilletVoyageDTO> request, Locale locale) {
        return null;
    }
}
