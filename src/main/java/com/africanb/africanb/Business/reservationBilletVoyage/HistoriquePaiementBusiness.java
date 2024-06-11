package com.africanb.africanb.Business.reservationBilletVoyage;


import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiement;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.HistoriquePaiement;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.dao.repository.compagnie.ModePaiement.*;
import com.africanb.africanb.dao.repository.compagnie.StatusUtilRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProgrammeRepository;
import com.africanb.africanb.dao.repository.reservationBilletVoyage.HistoriquePaiementRepository;
import com.africanb.africanb.dao.repository.reservationBilletVoyage.ReservationBilletVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.HistoriquePaiementDTO;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.ReservationBilletVoyageDTO;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.StatusUtilReservationBilletVoyageDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.reservationBilletVoyage.HistoriquePaiementTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ALZOUMA MOUSSA MAHAAMADOU
 */
@Log
@Component
public class HistoriquePaiementBusiness implements IBasicBusiness<Request<HistoriquePaiementDTO>, Response<HistoriquePaiementDTO>> {


    private Response<HistoriquePaiementDTO> response;

    private final ModePaiementRepository modePaiementRepository;
    private final ProgrammeRepository programmeRepository;
    private final ReservationBilletVoyageRepository reservationBilletVoyageRepository;
    private final HistoriquePaiementRepository historiquePaiementRepository;
    private final StatusUtilRepository statusUtilRepository;
    private final StatusUtilRservationBilletVoyageBusiness statusUtilReservationBilletVoyageBusiness;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public HistoriquePaiementBusiness(ModePaiementRepository modePaiementRepository, ProgrammeRepository programmeRepository, ReservationBilletVoyageRepository reservationBilletVoyageRepository, HistoriquePaiementRepository historiquePaiementRepository, StatusUtilRepository statusUtilRepository, StatusUtilRservationBilletVoyageBusiness statusUtilReservationBilletVoyageBusiness, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.modePaiementRepository = modePaiementRepository;
        this.programmeRepository = programmeRepository;
        this.reservationBilletVoyageRepository = reservationBilletVoyageRepository;
        this.historiquePaiementRepository = historiquePaiementRepository;
        this.statusUtilRepository = statusUtilRepository;
        this.statusUtilReservationBilletVoyageBusiness = statusUtilReservationBilletVoyageBusiness;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Transactional
    @Override
    public Response<HistoriquePaiementDTO> create(Request<HistoriquePaiementDTO> request, Locale locale) throws ParseException {
        Response<HistoriquePaiementDTO> response = new Response<>();
        List<HistoriquePaiement> items = new ArrayList<>();

        if(Optional.of(request.getData()).isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        HistoriquePaiementDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("modePaiementDesignation", dto.getModePaiementDesignation());
        fieldsToVerify.put("reservationBilletVoyage", dto.getReservationBilletVoyageDesignation());
        fieldsToVerify.put("dateTime", dto.getDateTimePayment());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }

        ReservationBilletVoyage existingReservationBilletVoyage = reservationBilletVoyageRepository.findByDesignation(dto.getReservationBilletVoyageDesignation(),false);
        if (existingReservationBilletVoyage==null) {
            response.setStatus(functionalError.SAVE_FAIL("reservationBilletVoyage inexistante !!!!", locale));
            response.setHasError(true);
            return response;
        }

        ModePaiement existinModePaiement = modePaiementRepository.findByDesignation(dto.getModePaiementDesignation(),false);
        if (existinModePaiement==null) {
            response.setStatus(functionalError.SAVE_FAIL("Mode paiement inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        dto.setIdentifiantUnique(RandomStringUtils.randomAlphanumeric(10,30));
        HistoriquePaiement entityToSave = HistoriquePaiementTransformer.INSTANCE.toEntity(dto,existinModePaiement,existingReservationBilletVoyage);
        HistoriquePaiement entitySaved =  historiquePaiementRepository.save(entityToSave);
        if(entitySaved==null){
            response.setStatus(functionalError.SAVE_FAIL("Erreur creation",locale));
            response.setHasError(true);
            return response;
        }
        if(existingReservationBilletVoyage.getProgramme()==null){
            response.setStatus(functionalError.SAVE_FAIL("Programme reservation non trouvé",locale));
            response.setHasError(true);
            return response;
        }else{
            Integer nombreDePlace = existingReservationBilletVoyage.getNombrePlace();
            Programme programme = existingReservationBilletVoyage.getProgramme();
            programme.setNombrePlaceDisponible(programme.getNombrePlaceDisponible() - nombreDePlace);
            existingReservationBilletVoyage.setDateReservation(Utilities.getCurrentDate());
            programmeRepository.save(programme);

            StatusUtil existingStatusUtilActual = statusUtilRepository.findByDesignation(ProjectConstants.REF_ELEMENT_RESERVATION_PAYEE_ET_NON_EFFECTIVE,false);
            if (existingStatusUtilActual==null) {
                response.setStatus(functionalError.SAVE_FAIL("Status inexistant !!!!", locale));
                response.setHasError(true);
                return response;
            }

            existingReservationBilletVoyage.setStatusUtilActual(existingStatusUtilActual);
            Request<StatusUtilReservationBilletVoyageDTO> subRequest = new Request<>();
            subRequest.setDatas(getStatusUtilReservationBilletVoyageDTOS(existingReservationBilletVoyage, existingStatusUtilActual));
            Response<StatusUtilReservationBilletVoyageDTO> subResponse = statusUtilReservationBilletVoyageBusiness.create(subRequest, locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return response;
            }
            reservationBilletVoyageRepository.save(existingReservationBilletVoyage);
        }

        items.add(entityToSave);
        List<HistoriquePaiementDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? HistoriquePaiementTransformer.INSTANCE.toLiteDtos(items)
                : HistoriquePaiementTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    private  List<StatusUtilReservationBilletVoyageDTO> getStatusUtilReservationBilletVoyageDTOS(ReservationBilletVoyage existingReservationBilletVoyage, StatusUtil existingStatusUtilActual) {
        List<StatusUtilReservationBilletVoyageDTO> itemsDatas =  Collections.synchronizedList(new ArrayList<>());
        StatusUtilReservationBilletVoyageDTO statusUtilReservationBilletVoyageDTO= new StatusUtilReservationBilletVoyageDTO();
        statusUtilReservationBilletVoyageDTO.setStatusUtilDesignation(existingStatusUtilActual.getDesignation());
        statusUtilReservationBilletVoyageDTO.setReservationBilletVoyageDesignation(existingReservationBilletVoyage.getDesignation());
        itemsDatas.add(statusUtilReservationBilletVoyageDTO);
        return itemsDatas;
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<HistoriquePaiementDTO> getHistoriquePaiementByIdentifiantUnique(Request<HistoriquePaiementDTO> request, Locale locale) throws ParseException {
        Response<HistoriquePaiementDTO> response = new Response<>();
        List<HistoriquePaiement> items = new ArrayList<>();
        if(request==null || request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée !!!",locale));
            response.setHasError(true);
            return response;
        }
        if(request.getData().getIdentifiantUnique()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucun identifiant unique !!!",locale));
            response.setHasError(true);
            return response;
        }

        HistoriquePaiementDTO dto = request.getData();
        HistoriquePaiement existingHistoriquePaiement = historiquePaiementRepository.findByIdentifiantUnique(dto.getIdentifiantUnique());
        if(existingHistoriquePaiement == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Historique paiement inexistant !!!",locale));
            response.setHasError(true);
            return response;
        }
        items.add(existingHistoriquePaiement);

        List<HistoriquePaiementDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? HistoriquePaiementTransformer.INSTANCE.toLiteDtos(items)
                : HistoriquePaiementTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Historique paiement-----");
        return response;
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<HistoriquePaiementDTO> getHistoriquePaiementByReservationBilletVoyage(Request<ReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        Response<HistoriquePaiementDTO> response = new Response<>();
        List<HistoriquePaiement> items = new ArrayList<>();
        if(request==null || request.getData()==null || request.getData().getDesignation()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée !!!",locale));
            response.setHasError(true);
            return response;
        }
        ReservationBilletVoyageDTO dto = request.getData();
        ReservationBilletVoyage existingReservationBilletVoyage = reservationBilletVoyageRepository.findByDesignation(dto.getDesignation(),false);
        if (existingReservationBilletVoyage==null) {
            response.setStatus(functionalError.SAVE_FAIL("reservationBilletVoyage inexistante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        HistoriquePaiement existingHistoriquePaiement = historiquePaiementRepository.findByReservationBilletVoyage(dto.getDesignation());
        if(existingHistoriquePaiement==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Reservation non payée !!!",locale));
            response.setHasError(true);
            return response;
        }
        items.add(existingHistoriquePaiement);

        List<HistoriquePaiementDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? HistoriquePaiementTransformer.INSTANCE.toLiteDtos(items)
                : HistoriquePaiementTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Historique paiement-----");
        return response;
    }


    @Override
    public Response<HistoriquePaiementDTO> update(Request<HistoriquePaiementDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<HistoriquePaiementDTO> delete(Request<HistoriquePaiementDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<HistoriquePaiementDTO> forceDelete(Request<HistoriquePaiementDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<HistoriquePaiementDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<HistoriquePaiementDTO> getByCriteria(Request<HistoriquePaiementDTO> request, Locale locale) throws ParseException {
        return null;
    }

}
