package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageBoolean;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProprieteOffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageBooleanDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.offrreVoyage.ValeurCaracteristiqueOffreVoyageBooleanTransformer;
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
public class ValeurCaracteristiqueOffreVoyageBooleanBusiness implements IBasicBusiness<Request<ValeurCaracteristiqueOffreVoyageBooleanDTO>, Response<ValeurCaracteristiqueOffreVoyageBooleanDTO>> {


    private Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> response;

    private final ValeurCaracteristiqueOffreVoyageBooleanRepository valeurCaracteristiqueOffreVoyageBooleanRepository;
    private final FunctionalError functionalError;
    private final OffreVoyageRepository offreVoyageRepository;
    private final ProprieteOffreVoyageRepository proprieteOffreVoyageRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ValeurCaracteristiqueOffreVoyageBooleanBusiness(ValeurCaracteristiqueOffreVoyageBooleanRepository valeurCaracteristiqueOffreVoyageBooleanRepository, FunctionalError functionalError, OffreVoyageRepository offreVoyageRepository, ProprieteOffreVoyageRepository proprieteOffreVoyageRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.valeurCaracteristiqueOffreVoyageBooleanRepository = valeurCaracteristiqueOffreVoyageBooleanRepository;
        this.functionalError = functionalError;
        this.offreVoyageRepository = offreVoyageRepository;
        this.proprieteOffreVoyageRepository = proprieteOffreVoyageRepository;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> create(Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> request, Locale locale) throws ParseException {
        Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> response = new Response<>();
        List<ValeurCaracteristiqueOffreVoyageBoolean> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(ValeurCaracteristiqueOffreVoyageBooleanDTO itemDto : request.getDatas() ){
            ValeurCaracteristiqueOffreVoyageBoolean exitingEntity = valeurCaracteristiqueOffreVoyageBooleanRepository.findByDesignationByOffreVoyageDesignation(itemDto.getDesignation(),itemDto.getProprieteOffreVoyageDesignation(),false);
            if (exitingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("ValeurCaracteristiqueOffreVoyage ayant la designation"+itemDto.getDesignation()+"existe", locale));
                response.setHasError(true);
                return response;
            }
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
            ValeurCaracteristiqueOffreVoyageBoolean entityToSave = ValeurCaracteristiqueOffreVoyageBooleanTransformer.INSTANCE.toEntity(itemDto,existingOffreVoyage,existingProprieteOffreVoyage);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            ValeurCaracteristiqueOffreVoyageBoolean entitySaved=valeurCaracteristiqueOffreVoyageBooleanRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ValeurCaracteristiqueOffreVoyageBooleanDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ValeurCaracteristiqueOffreVoyageBooleanTransformer.INSTANCE.toLiteDtos(items)
                                    : ValeurCaracteristiqueOffreVoyageBooleanTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> update(Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> delete(Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> forceDelete(Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> getByCriteria(Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> request, Locale locale) {
        return null;
    }

}
