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

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
@Component
public class StatusUtilRservationBilletVoyageBusiness implements IBasicBusiness<Request<StatusUtilReservationBilletVoyageDTO>, Response<StatusUtilReservationBilletVoyageDTO>> {


    private Response<StatusUtilReservationBilletVoyageDTO> response;

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
        Response<StatusUtilReservationBilletVoyageDTO> response = new Response<StatusUtilReservationBilletVoyageDTO>();
        List<StatusUtilReservationBilletVoyage> items = new ArrayList<StatusUtilReservationBilletVoyage>();
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilReservationBilletVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<StatusUtilReservationBilletVoyageDTO>());
        for(StatusUtilReservationBilletVoyageDTO dto: request.getDatas() ) {
            Response<StatusUtilReservationBilletVoyageDTO> responseCheckDTO = checkStatusUtilReservationBilletVoyageDTO(locale, response, itemsDtos, dto);
            if (responseCheckDTO != null) return responseCheckDTO;
            itemsDtos.add(dto);
        }
        for(StatusUtilReservationBilletVoyageDTO dto : itemsDtos){
            StatusUtilReservationBilletVoyage existingEntity = null;
            existingEntity = statusUtilReservationBilletVoyageRepository.findByReservationBilletVoyageAndStatusUtil(dto.getReservationBilletVoyageDesignation(),dto.getStatusUtilDesignation());
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
        List<StatusUtilReservationBilletVoyage> itemsSaved = null;
        itemsSaved = statusUtilReservationBilletVoyageRepository.saveAll((Iterable<StatusUtilReservationBilletVoyage>) items);
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
        Response<StatusUtilReservationBilletVoyageDTO> response = new Response<StatusUtilReservationBilletVoyageDTO>();
        List<StatusUtilReservationBilletVoyage> items = new ArrayList<StatusUtilReservationBilletVoyage>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilReservationBilletVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<StatusUtilReservationBilletVoyageDTO>());
        for(StatusUtilReservationBilletVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
            StatusUtilReservationBilletVoyage entityToSave = null;
            entityToSave = statusUtilReservationBilletVoyageRepository.findOne(dto.getId());
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilReservationBilletVoyage id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            ReservationBilletVoyage existingReservationBilletVoyage = null;
            if (Utilities.isBlank(dto.getReservationBilletVoyageDesignation()) && !entityToSave.getReservationBilletVoyage().getDesignation().equalsIgnoreCase(dto.getReservationBilletVoyageDesignation())) {
                existingReservationBilletVoyage = reservationBilletVoyageRepository.findByDesignation(dto.getReservationBilletVoyageDesignation(), false);
                if (existingReservationBilletVoyage == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("ReservationBilletVoyage -> " + dto.getReservationBilletVoyageDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setReservationBilletVoyage(existingReservationBilletVoyage);
            }
            StatusUtil existingStatusUtil = null;
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
        List<StatusUtilReservationBilletVoyage> itemsSaved = null;
        itemsSaved = statusUtilReservationBilletVoyageRepository.saveAll((Iterable<StatusUtilReservationBilletVoyage>) items);
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
        Response<StatusUtilReservationBilletVoyageDTO> response = new Response<StatusUtilReservationBilletVoyageDTO>();
        List<StatusUtilReservationBilletVoyage> items = new ArrayList<StatusUtilReservationBilletVoyage>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        for(StatusUtilReservationBilletVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        for(StatusUtilReservationBilletVoyageDTO dto : request.getDatas()){
            StatusUtilReservationBilletVoyage existingEntity = null;
            existingEntity = statusUtilReservationBilletVoyageRepository.findOne(dto.getId());
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

    /*    log.info("----begin get HistoriqueDemande-----");

        Response<StatusUtilCompagnieTransportDTO> response = new Response<StatusUtilCompagnieTransportDTO>();

        //verification si le parametre d'ordre à été fourni, sinon nous mettons le paramètre à vide
        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }

        //verification si le parametre direction à été fourni, sinon nous mettons le paramètre ascendant( du plus ancien au plus ressent)
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }

        //recuperation des entités en base
        List<HistoriqueDemande> items = statusUtilCompagnieTransportRepository.getByCriteria(request, em, locale);

        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("HistoriqueDemande", locale));
            response.setHasError(false);
            return response;
        }

        //Transformation
        List<HistoriqueDemandeDto> itemsDto = HistoriqueDemandeTransformer.INSTANCE.toDtos(items);

        //Envoie de la reponse
        response.setItems(itemsDto);
        response.setCount(statusUtilCompagnieTransportRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        log.info("----end get HistoriqueDemande-----");

        return response;
        */
        return null;
    }
}
