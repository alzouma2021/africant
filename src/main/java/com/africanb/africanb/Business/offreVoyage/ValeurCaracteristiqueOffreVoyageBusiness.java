package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage.ValeurCaracteristiqueOffreVoyageDTOCreator;
import com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage.ValeurCaracteristiqueOffreVoyageEntityCreator;
import com.africanb.africanb.Business.design.factory.valeurCaracteristiqueOffreVoyage.ValeurCaracteristiqueOffreVoyageUtils;
import com.africanb.africanb.dao.entity.offreVoyage.*;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProprieteOffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ValeurCaracteristiqueOffreVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.*;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Log
@Component
public class ValeurCaracteristiqueOffreVoyageBusiness implements IBasicBusiness<Request<ValeurCaracteristiqueOffreVoyageDTO>, Response<ValeurCaracteristiqueOffreVoyageDTO>>,ValeurCaracteristiqueOffreVoyageInterface {

    private Response<ValeurCaracteristiqueOffreVoyageDTO> response;

    private final FunctionalError functionalError;
    private final OffreVoyageRepository offreVoyageRepository;
    private final ProprieteOffreVoyageRepository proprieteOffreVoyageRepository;
    private final ValeurCaracteristiqueOffreVoyageRepository valeurCaracteristiqueOffreVoyageRepository;
    private final ValeurCaracteristiqueOffreVoyageBooleanBusiness valeurCaracteristiqueOffreVoyageBooleanBusiness;
    private final ValeurCaracteristiqueOffreVoyageLongBusiness valeurCaracteristiqueOffreVoyageLongBusiness;
    private final ValeurCaracteristiqueOffreVoyageStringBusiness valeurCaracteristiqueOffreVoyageStringBusiness;
    private final TechnicalError technicalError;
    private final  ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ValeurCaracteristiqueOffreVoyageBusiness(FunctionalError functionalError, OffreVoyageRepository offreVoyageRepository, ProprieteOffreVoyageRepository proprieteOffreVoyageRepository, ValeurCaracteristiqueOffreVoyageRepository valeurCaracteristiqueOffreVoyageRepository, ValeurCaracteristiqueOffreVoyageBooleanBusiness valeurCaracteristiqueOffreVoyageBooleanBusiness, ValeurCaracteristiqueOffreVoyageLongBusiness valeurCaracteristiqueOffreVoyageLongBusiness, ValeurCaracteristiqueOffreVoyageStringBusiness valeurCaracteristiqueOffreVoyageStringBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.functionalError = functionalError;
        this.offreVoyageRepository = offreVoyageRepository;
        this.proprieteOffreVoyageRepository = proprieteOffreVoyageRepository;
        this.valeurCaracteristiqueOffreVoyageRepository = valeurCaracteristiqueOffreVoyageRepository;
        this.valeurCaracteristiqueOffreVoyageBooleanBusiness = valeurCaracteristiqueOffreVoyageBooleanBusiness;
        this.valeurCaracteristiqueOffreVoyageLongBusiness = valeurCaracteristiqueOffreVoyageLongBusiness;
        this.valeurCaracteristiqueOffreVoyageStringBusiness = valeurCaracteristiqueOffreVoyageStringBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> create(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = new Response<>();
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDto= new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(ValeurCaracteristiqueOffreVoyageDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<>();
                fieldsToVerify.put("designation", dto.getDesignation());
                fieldsToVerify.put("offfreVoyageDesignation", dto.getOffreVoyageDesignation());
                fieldsToVerify.put("proprieteOffreVoyageDesignation", dto.getProprieteOffreVoyageDesignation());
                if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                    response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                    response.setHasError(true);
                    return response;
                }
                if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                    response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de la designation'" + dto.getDesignation() + "' pour les pays", locale));
                    response.setHasError(true);
                    return response;
                }
                itemsDtos.add(dto);
            }
        }
        for(ValeurCaracteristiqueOffreVoyageDTO itemDto : itemsDtos){
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
            if(existingProprieteOffreVoyage.getEstObligatoire()){
                if(itemDto.getValeurTexte()==null){
                    response.setStatus(functionalError.DATA_EXIST("La propriete offre de voyage " +existingProprieteOffreVoyage.getDesignation()+ ", est obligatoire.Sa valeur doit être définie", locale));
                    response.setHasError(true);
                    return response;
                }
            }
            itemDto.setTypeProprieteOffreVoyageDesignation(existingProprieteOffreVoyage.getTypeProprieteOffreVoyage().getDesignation());
            ValeurCaracteristiqueOffreVoyageDTO entitySaved = saveValeurCaracteristiqueOffreVoyage(ValeurCaracteristiqueOffreVoyageUtils.transformAbstractClassIntoChildClass(itemDto),locale);
            itemsDto.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(itemsDto)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> update(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> delete(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> forceDelete(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getByCriteria(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getAllValeurCaracteristiqueOffreVoyageByOffreVoyageDesignation(Request<OffreVoyageDTO> request, Locale locale) {
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = new Response<>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("offreVoyageDesignation", request.getData().getDesignation());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String offreVoyageDesignation=request.getData().getDesignation();
        OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(offreVoyageDesignation,false);
        if (existingOffreVoyage == null) {
            response.setStatus(functionalError.DATA_EXIST("Offre de voyage n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        List<ValeurCaracteristiqueOffreVoyage> items = valeurCaracteristiqueOffreVoyageRepository.findAllByOffreVoyageDesignation(offreVoyageDesignation,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie ne dipose d'aucune offre de voyage", locale));
            response.setHasError(true);
            return response;
        }
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDto = convertClassIntoValeurCaracteristiqueOffreVoyageDTO(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update l'offre de voyage-----");
        return response;
    }

    @Override
    public  List<ValeurCaracteristiqueOffreVoyageDTO> convertClassIntoValeurCaracteristiqueOffreVoyageDTO(List<ValeurCaracteristiqueOffreVoyage> valeurCaracteristiqueOffreVoyageList) {
        return valeurCaracteristiqueOffreVoyageList.stream()
                .filter(Objects::nonNull)
                .map(valeurCaracteristiqueOffreVoyage -> ValeurCaracteristiqueOffreVoyageEntityCreator.createValeurOffreVoyageDTO(valeurCaracteristiqueOffreVoyage))
                .collect(Collectors.toList());
    }

    @Override
    public ValeurCaracteristiqueOffreVoyageDTO saveValeurCaracteristiqueOffreVoyage(ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO, Locale locale) throws ParseException {
        if(valeurCaracteristiqueOffreVoyageDTO instanceof ValeurCaracteristiqueOffreVoyageLongDTO valeurLongDTO){
            Request<ValeurCaracteristiqueOffreVoyageLongDTO> subRequest = new Request<>();
            List<ValeurCaracteristiqueOffreVoyageLongDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            valeurLongDTO.setValeur(Utilities.convertStringToLong(valeurLongDTO.getValeurTexte()));
            itemsDTO.add(valeurLongDTO);
            subRequest.setDatas( itemsDTO);
            Response<ValeurCaracteristiqueOffreVoyageLongDTO> subResponse = valeurCaracteristiqueOffreVoyageLongBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            return ValeurCaracteristiqueOffreVoyageDTOCreator.createValeurOffreVoyageDTO(subResponse.getItems().get(0));
        }
         if(valeurCaracteristiqueOffreVoyageDTO instanceof ValeurCaracteristiqueOffreVoyageStringDTO valeurStringDTO){
            Request<ValeurCaracteristiqueOffreVoyageStringDTO> subRequest = new Request<>();
            List<ValeurCaracteristiqueOffreVoyageStringDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            valeurStringDTO.setValeur(valeurStringDTO.getValeurTexte());
            itemsDTO.add(valeurStringDTO);
            subRequest.setDatas( itemsDTO);
            Response<ValeurCaracteristiqueOffreVoyageStringDTO> subResponse = valeurCaracteristiqueOffreVoyageStringBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            return ValeurCaracteristiqueOffreVoyageDTOCreator.createValeurOffreVoyageDTO(subResponse.getItems().get(0));
        }
         if(valeurCaracteristiqueOffreVoyageDTO instanceof ValeurCaracteristiqueOffreVoyageBooleanDTO valeurBooleanDTO){
            Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> subRequest = new Request<>();
            List<ValeurCaracteristiqueOffreVoyageBooleanDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            valeurBooleanDTO.setValeur(Utilities.convertStringToBoolean(valeurBooleanDTO.getValeurTexte()));
            itemsDTO.add(valeurBooleanDTO);
            subRequest.setDatas(itemsDTO);
            Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> subResponse = valeurCaracteristiqueOffreVoyageBooleanBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            return ValeurCaracteristiqueOffreVoyageDTOCreator.createValeurOffreVoyageDTO(subResponse.getItems().get(0));
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
