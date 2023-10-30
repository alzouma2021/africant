package com.africanb.africanb.Business.reservationBilletVoyage;


import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiement;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.HistoriquePaiement;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.ModePaiement.*;
import com.africanb.africanb.dao.repository.reservationBilletVoyage.ReservationBilletVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.HistoriquePaiementDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.reservationBilletVoyage.HistoriquePaiementTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private ModePaiementMoovMoneyRepository modePaiementMoovMoneyRepository;
    @Autowired
    private ModePaiementEnEspeceRepository modePaiementEnEspeceRepository;
    @Autowired
    private ModePaiementMtnMoneyRepository modePaiementMtnMoneyRepository;
    @Autowired
    private ModePaiementOrangeMoneyRepository modePaiementOrangeMoneyRepository;
    @Autowired
    private ModePaiementRepository modePaiementRepository;

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private ReservationBilletVoyageRepository reservationBilletVoyageRepository;


    @Autowired
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
        dto.setIdentifiantUnique(RandomStringUtils.randomAlphabetic(10,20));
        HistoriquePaiement entityToSave = HistoriquePaiementTransformer.INSTANCE.toEntity(dto,existinModePaiement,existingReservationBilletVoyage);
        if(entityToSave==null){
            response.setStatus(functionalError.SAVE_FAIL("Erreur creation",locale));
            response.setHasError(true);
            return response;
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
