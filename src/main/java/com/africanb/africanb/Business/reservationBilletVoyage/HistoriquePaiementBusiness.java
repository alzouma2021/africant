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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ModePaiementRepository modePaiementRepository;
    @Autowired
    private ProgrammeRepository programmeRepository;
    @Autowired
    private ReservationBilletVoyageRepository reservationBilletVoyageRepository;
    @Autowired
    private HistoriquePaiementRepository historiquePaiementRepository;
    @Autowired
    private StatusUtilRepository statusUtilRepository;
    @Autowired
    private StatusUtilRservationBilletVoyageBusiness statusUtilReservationBilletVoyageBusiness;

    private FunctionalError functionalError;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public HistoriquePaiementBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<HistoriquePaiementDTO> create(Request<HistoriquePaiementDTO> request, Locale locale) throws ParseException {
        Response<HistoriquePaiementDTO> response = new Response<HistoriquePaiementDTO>();
        List<HistoriquePaiement> items = new ArrayList<HistoriquePaiement>();
        if(Optional.of(request.getData()).isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        HistoriquePaiementDTO dto = request.getData();
        List<HistoriquePaiementDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<HistoriquePaiementDTO>());
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        //fieldsToVerify.put("", dto.getGareDesignation());
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
        ModePaiement existinModePaiement=null;
        existinModePaiement = modePaiementRepository.findByDesignation(dto.getModePaiementDesignation(),false);
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
            programme.setNombrePlaceDisponible(programme.getNombrePlaceDisponible()-nombreDePlace);
            existingReservationBilletVoyage.setDateReservation(Utilities.getCurrentDate());
            programme=programmeRepository.save(programme);

            StatusUtil existingStatusUtilActual = null;
            existingStatusUtilActual = statusUtilRepository.findByDesignation(ProjectConstants.REF_ELEMENT_RESERVATION_PAYEE_ET_NON_EFFECTIVE,false);
            if (existingStatusUtilActual==null) {
                response.setStatus(functionalError.SAVE_FAIL("Status inexistant !!!!", locale));
                response.setHasError(true);
                return response;
            }
            existingReservationBilletVoyage.setStatusUtilActual(existingStatusUtilActual);
            List<StatusUtilReservationBilletVoyageDTO> itemsDatas =  Collections.synchronizedList(new ArrayList<StatusUtilReservationBilletVoyageDTO>());
            StatusUtilReservationBilletVoyageDTO statusUtilReservationBilletVoyageDTO= new StatusUtilReservationBilletVoyageDTO();
            statusUtilReservationBilletVoyageDTO.setStatusUtilDesignation(existingStatusUtilActual.getDesignation());
            statusUtilReservationBilletVoyageDTO.setReservationBilletVoyageDesignation(existingReservationBilletVoyage.getDesignation());
            itemsDatas.add(statusUtilReservationBilletVoyageDTO);
            Request<StatusUtilReservationBilletVoyageDTO> subRequest = new Request<StatusUtilReservationBilletVoyageDTO>();
            subRequest.setDatas(itemsDatas);
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


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<HistoriquePaiementDTO> getHistoriquePaiementByIdentifiantUnique(Request<HistoriquePaiementDTO> request, Locale locale) throws ParseException {
        Response<HistoriquePaiementDTO> response = new Response<HistoriquePaiementDTO>();
        HistoriquePaiementDTO dto=null;
        List<HistoriquePaiement> items = new ArrayList<HistoriquePaiement>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        if(request==null || request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée !!!",locale));
            response.setHasError(true);
            return response;
        }
        if(request.getData().getIdentifiantUnique()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucun identifiant unique !!!",locale));
            response.setHasError(true);
            return response;
        }else{
            dto=request.getData();
        }
        HistoriquePaiement existingHistoriquePaiement=historiquePaiementRepository.findByIdentifiantUnique(dto.getIdentifiantUnique());
        if(existingHistoriquePaiement==null){
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
        Response<HistoriquePaiementDTO> response = new Response<HistoriquePaiementDTO>();
        ReservationBilletVoyageDTO dto=null;
        List<HistoriquePaiement> items = new ArrayList<HistoriquePaiement>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        if(request==null || request.getData()==null || request.getData().getDesignation()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée !!!",locale));
            response.setHasError(true);
            return response;
        }
        dto=request.getData();
        ReservationBilletVoyage existingReservationBilletVoyage = reservationBilletVoyageRepository.findByDesignation(dto.getDesignation(),false);
        if (existingReservationBilletVoyage==null) {
            response.setStatus(functionalError.SAVE_FAIL("reservationBilletVoyage inexistante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        HistoriquePaiement existingHistoriquePaiement=historiquePaiementRepository.findByReservationBilletVoyage(dto.getDesignation());
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
