package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageLong;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProprieteOffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ValeurCaracteristiqueOffreVoyageLongRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageLongDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.offrreVoyage.ValeurCaracteristiqueOffreVoyageLongTransformer;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Log
@Component
public class ValeurCaracteristiqueOffreVoyageLongBusiness implements IBasicBusiness<Request<ValeurCaracteristiqueOffreVoyageLongDTO>, Response<ValeurCaracteristiqueOffreVoyageLongDTO>> {


    private final ValeurCaracteristiqueOffreVoyageLongRepository valeurCaracteristiqueOffreVoyageLongRepository;
    private final OffreVoyageRepository offreVoyageRepository;
    private final ProprieteOffreVoyageRepository proprieteOffreVoyageRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ValeurCaracteristiqueOffreVoyageLongBusiness(ValeurCaracteristiqueOffreVoyageLongRepository valeurCaracteristiqueOffreVoyageLongRepository, OffreVoyageRepository offreVoyageRepository, ProprieteOffreVoyageRepository proprieteOffreVoyageRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.valeurCaracteristiqueOffreVoyageLongRepository = valeurCaracteristiqueOffreVoyageLongRepository;
        this.offreVoyageRepository = offreVoyageRepository;
        this.proprieteOffreVoyageRepository = proprieteOffreVoyageRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageLongDTO> create(Request<ValeurCaracteristiqueOffreVoyageLongDTO> request, Locale locale) throws ParseException {
        Response<ValeurCaracteristiqueOffreVoyageLongDTO> response = new Response<>();
        List<ValeurCaracteristiqueOffreVoyageLong> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(ValeurCaracteristiqueOffreVoyageLongDTO itemDto : request.getDatas() ){
            OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(itemDto.getOffreVoyageDesignation(),false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("Offre de voyage n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ProprieteOffreVoyage existingProprieteOffreVoyage = proprieteOffreVoyageRepository.findByDesignation(itemDto.getProprieteOffreVoyageDesignation(),false);
            if (existingProprieteOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("ProprieteOffre de voyage n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ValeurCaracteristiqueOffreVoyageLong entityToSave = ValeurCaracteristiqueOffreVoyageLongTransformer.INSTANCE.toEntity(itemDto,existingOffreVoyage,existingProprieteOffreVoyage);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            ValeurCaracteristiqueOffreVoyageLong entitySaved=valeurCaracteristiqueOffreVoyageLongRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ValeurCaracteristiqueOffreVoyageLongDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ValeurCaracteristiqueOffreVoyageLongTransformer.INSTANCE.toLiteDtos(items)
                                    : ValeurCaracteristiqueOffreVoyageLongTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageLongDTO> update(Request<ValeurCaracteristiqueOffreVoyageLongDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageLongDTO> delete(Request<ValeurCaracteristiqueOffreVoyageLongDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageLongDTO> forceDelete(Request<ValeurCaracteristiqueOffreVoyageLongDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageLongDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageLongDTO> getByCriteria(Request<ValeurCaracteristiqueOffreVoyageLongDTO> request, Locale locale) {
        return null;
    }
}
