package com.africanb.africanb.Business.offreVoyage;



import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.ValeurCaracteristiqueOffreVoyageString;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProprieteOffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ValeurCaracteristiqueOffreVoyageStringRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageStringDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.offrreVoyage.ValeurCaracteristiqueOffreVoyageStringTransformer;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
@Component
public class ValeurCaracteristiqueOffreVoyageStringBusiness implements IBasicBusiness<Request<ValeurCaracteristiqueOffreVoyageStringDTO>, Response<ValeurCaracteristiqueOffreVoyageStringDTO>> {


    private Response<ValeurCaracteristiqueOffreVoyageStringDTO> response;


    private final ValeurCaracteristiqueOffreVoyageStringRepository valeurCaracteristiqueOffreVoyageStringRepository;
    private final FunctionalError functionalError;
    private final OffreVoyageRepository offreVoyageRepository;
    private final ProprieteOffreVoyageRepository proprieteOffreVoyageRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ValeurCaracteristiqueOffreVoyageStringBusiness(ValeurCaracteristiqueOffreVoyageStringRepository valeurCaracteristiqueOffreVoyageStringRepository, FunctionalError functionalError, OffreVoyageRepository offreVoyageRepository, ProprieteOffreVoyageRepository proprieteOffreVoyageRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.valeurCaracteristiqueOffreVoyageStringRepository = valeurCaracteristiqueOffreVoyageStringRepository;
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
    public Response<ValeurCaracteristiqueOffreVoyageStringDTO> create(Request<ValeurCaracteristiqueOffreVoyageStringDTO> request, Locale locale) throws ParseException {
        Response<ValeurCaracteristiqueOffreVoyageStringDTO> response = new Response<>();
        List<ValeurCaracteristiqueOffreVoyageString> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(ValeurCaracteristiqueOffreVoyageStringDTO itemDto : request.getDatas() ){
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
            ValeurCaracteristiqueOffreVoyageString entityToSave = ValeurCaracteristiqueOffreVoyageStringTransformer.INSTANCE.toEntity(itemDto,existingOffreVoyage,existingProprieteOffreVoyage);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            ValeurCaracteristiqueOffreVoyageString entitySaved = valeurCaracteristiqueOffreVoyageStringRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ValeurCaracteristiqueOffreVoyageStringDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ValeurCaracteristiqueOffreVoyageStringTransformer.INSTANCE.toLiteDtos(items)
                                    : ValeurCaracteristiqueOffreVoyageStringTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageStringDTO> update(Request<ValeurCaracteristiqueOffreVoyageStringDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageStringDTO> delete(Request<ValeurCaracteristiqueOffreVoyageStringDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageStringDTO> forceDelete(Request<ValeurCaracteristiqueOffreVoyageStringDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageStringDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageStringDTO> getByCriteria(Request<ValeurCaracteristiqueOffreVoyageStringDTO> request, Locale locale) {
        return null;
    }
}
