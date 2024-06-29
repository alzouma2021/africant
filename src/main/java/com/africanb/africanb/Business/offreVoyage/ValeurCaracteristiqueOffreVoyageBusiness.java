package com.africanb.africanb.Business.offreVoyage;


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
            itemDto=Utilities.transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeDeLaPropriete(itemDto);
            ValeurCaracteristiqueOffreVoyageDTO entitySaved=saveValeurCaracteristiqueOffreVoyageDTOEnFonctionDuTypeDeLaPropriete(itemDto,locale);
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
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDto = convertValeurCaracteristiqueOffreVoyagesFilleToValeurCaracteristiqueOffreVoyageDTO(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update l'offre de voyage-----");
        return response;
    }

    @Override
    public  List<ValeurCaracteristiqueOffreVoyageDTO>convertValeurCaracteristiqueOffreVoyagesFilleToValeurCaracteristiqueOffreVoyageDTO(List<ValeurCaracteristiqueOffreVoyage> valeurCaracteristiqueOffreVoyageList) {
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
        for(ValeurCaracteristiqueOffreVoyage valeurCaracteristiqueOffreVoyage:valeurCaracteristiqueOffreVoyageList) {
         if(valeurCaracteristiqueOffreVoyage instanceof ValeurCaracteristiqueOffreVoyageBoolean itemBoolean){
             ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( itemBoolean.getId());
            rtn.setDesignation( itemBoolean.getDesignation());
            rtn.setDescription( itemBoolean.getDescription());
            rtn.setValeurTexte(itemBoolean.getValeur().toString());
            rtn.setDeletedAt( itemBoolean.getDeletedAt()==null?null:itemBoolean.getDeletedAt().toString());
            rtn.setUpdatedAt( itemBoolean.getUpdatedAt()==null?null:itemBoolean.getUpdatedAt().toString());
            rtn.setCreatedAt( itemBoolean.getCreatedAt()==null?null:itemBoolean.getCreatedAt().toString());
            rtn.setCreatedBy( itemBoolean.getCreatedBy());
            rtn.setIsDeleted( itemBoolean.getIsDeleted());
            rtn.setDeletedBy( itemBoolean.getDeletedBy());
            rtn.setUpdatedBy( itemBoolean.getUpdatedBy());
            rtn.setOffreVoyageDesignation(itemBoolean.getOffreVoyage().getDesignation());
            rtn.setProprieteOffreVoyageDesignation(itemBoolean.getProprieteOffreVoyage().getDesignation());
            itemsDTO.add(rtn);
        }
        else if(valeurCaracteristiqueOffreVoyage instanceof ValeurCaracteristiqueOffreVoyageString itemString){
             ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( itemString.getId());
            rtn.setDesignation( itemString.getDesignation());
            rtn.setDescription( itemString.getDescription());
            rtn.setValeurTexte(itemString.getValeur());
            rtn.setDeletedAt( itemString.getDeletedAt()==null?null:itemString.getDeletedAt().toString());
            rtn.setUpdatedAt( itemString.getUpdatedAt()==null?null:itemString.getUpdatedAt().toString());
            rtn.setCreatedAt( itemString.getCreatedAt()==null?null:itemString.getCreatedAt().toString());
            rtn.setCreatedBy( itemString.getCreatedBy());
            rtn.setIsDeleted( itemString.getIsDeleted());
            rtn.setDeletedBy( itemString.getDeletedBy());
            rtn.setUpdatedBy( itemString.getUpdatedBy());
            rtn.setOffreVoyageDesignation(itemString.getOffreVoyage().getDesignation());
            rtn.setProprieteOffreVoyageDesignation(itemString.getProprieteOffreVoyage().getDesignation());
            itemsDTO.add(rtn);
        }
        else if(valeurCaracteristiqueOffreVoyage instanceof ValeurCaracteristiqueOffreVoyageLong itemLong){
             ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( itemLong.getId());
            rtn.setDesignation( itemLong.getDesignation());
            rtn.setDescription( itemLong.getDescription());
            rtn.setValeurTexte(itemLong.getValeur().toString());
            rtn.setDeletedAt( itemLong.getDeletedAt()==null?null:itemLong.getDeletedAt().toString());
            rtn.setUpdatedAt( itemLong.getUpdatedAt()==null?null:itemLong.getDeletedAt().toString());
            rtn.setCreatedAt( itemLong.getCreatedAt()==null?null:itemLong.getDeletedAt().toString());
            rtn.setCreatedBy( itemLong.getCreatedBy());
            rtn.setIsDeleted( itemLong.getIsDeleted());
            rtn.setDeletedBy( itemLong.getDeletedBy());
            rtn.setUpdatedBy( itemLong.getUpdatedBy());
            rtn.setOffreVoyageDesignation(itemLong.getOffreVoyage().getDesignation());
            rtn.setProprieteOffreVoyageDesignation(itemLong.getProprieteOffreVoyage().getDesignation());
            itemsDTO.add(rtn);
        }
        }
        return itemsDTO;
    }

    @Override
    public ValeurCaracteristiqueOffreVoyageDTO saveValeurCaracteristiqueOffreVoyageDTOEnFonctionDuTypeDeLaPropriete(ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO,Locale locale) throws ParseException {
        if(valeurCaracteristiqueOffreVoyageDTO instanceof ValeurCaracteristiqueOffreVoyageLongDTO valeurLongDTO){
            Request<ValeurCaracteristiqueOffreVoyageLongDTO> subRequest = new Request<>();
            List<ValeurCaracteristiqueOffreVoyageLongDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            Long valeur = Utilities.convertStringToLong(valeurLongDTO.getValeurTexte());
            valeurLongDTO.setValeur(valeur);
            itemsDTO.add(valeurLongDTO);
            subRequest.setDatas( itemsDTO);
            Response<ValeurCaracteristiqueOffreVoyageLongDTO> subResponse = valeurCaracteristiqueOffreVoyageLongBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            rtn.setValeurTexte( subResponse.getItems().get(0).getValeur().toString());
            rtn.setDeletedAt( subResponse.getItems().get(0).getDeletedAt());
            rtn.setUpdatedAt( subResponse.getItems().get(0).getUpdatedAt());
            rtn.setCreatedAt( subResponse.getItems().get(0).getCreatedAt());
            rtn.setCreatedBy( subResponse.getItems().get(0).getCreatedBy());
            rtn.setIsDeleted( subResponse.getItems().get(0).getIsDeleted());
            rtn.setDeletedBy( subResponse.getItems().get(0).getDeletedBy());
            rtn.setUpdatedBy( subResponse.getItems().get(0).getUpdatedBy());
            rtn.setIsDeletedParam( subResponse.getItems().get(0).getIsDeletedParam());
            rtn.setUpdatedAtParam( subResponse.getItems().get(0).getUpdatedAtParam());
            rtn.setCreatedAtParam( subResponse.getItems().get(0).getCreatedAtParam());
            rtn.setCreatedByParam( subResponse.getItems().get(0).getCreatedByParam());
            rtn.setUpdatedByParam(subResponse.getItems().get(0).getUpdatedByParam());
            rtn.setOffreVoyageDesignation(subResponse.getItems().get(0).getOffreVoyageDesignation());
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            rtn.setProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getProprieteOffreVoyageDesignation());
            rtn.setTypeProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getTypeProprieteOffreVoyageDesignation());
            return rtn;
        }
        else if(valeurCaracteristiqueOffreVoyageDTO instanceof ValeurCaracteristiqueOffreVoyageStringDTO valeurStringDTO){
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
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            rtn.setValeurTexte( subResponse.getItems().get(0).getValeur());
            rtn.setDeletedAt( subResponse.getItems().get(0).getDeletedAt());
            rtn.setUpdatedAt( subResponse.getItems().get(0).getUpdatedAt());
            rtn.setCreatedAt( subResponse.getItems().get(0).getCreatedAt());
            rtn.setCreatedBy( subResponse.getItems().get(0).getCreatedBy());
            rtn.setIsDeleted( subResponse.getItems().get(0).getIsDeleted());
            rtn.setDeletedBy( subResponse.getItems().get(0).getDeletedBy());
            rtn.setUpdatedBy( subResponse.getItems().get(0).getUpdatedBy());
            rtn.setIsDeletedParam( subResponse.getItems().get(0).getIsDeletedParam());
            rtn.setUpdatedAtParam( subResponse.getItems().get(0).getUpdatedAtParam());
            rtn.setCreatedAtParam( subResponse.getItems().get(0).getCreatedAtParam());
            rtn.setCreatedByParam( subResponse.getItems().get(0).getCreatedByParam());
            rtn.setUpdatedByParam(subResponse.getItems().get(0).getUpdatedByParam());
            rtn.setOffreVoyageDesignation(subResponse.getItems().get(0).getOffreVoyageDesignation());
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            rtn.setProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getProprieteOffreVoyageDesignation());
            rtn.setTypeProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getTypeProprieteOffreVoyageDesignation());
            return rtn;
        }
        else if(valeurCaracteristiqueOffreVoyageDTO instanceof ValeurCaracteristiqueOffreVoyageBooleanDTO valeurBooleanDTO){
            Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> subRequest = new Request<>();
            List<ValeurCaracteristiqueOffreVoyageBooleanDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            Boolean valeur = Utilities.convertStringToBoolean(valeurBooleanDTO.getValeurTexte());
            valeurBooleanDTO.setValeur(valeur);
            itemsDTO.add(valeurBooleanDTO);
            subRequest.setDatas( itemsDTO);
            Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> subResponse = valeurCaracteristiqueOffreVoyageBooleanBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            rtn.setValeurTexte( subResponse.getItems().get(0).getValeur().toString());
            rtn.setDeletedAt( subResponse.getItems().get(0).getDeletedAt());
            rtn.setUpdatedAt( subResponse.getItems().get(0).getUpdatedAt());
            rtn.setCreatedAt( subResponse.getItems().get(0).getCreatedAt());
            rtn.setCreatedBy( subResponse.getItems().get(0).getCreatedBy());
            rtn.setIsDeleted( subResponse.getItems().get(0).getIsDeleted());
            rtn.setDeletedBy( subResponse.getItems().get(0).getDeletedBy());
            rtn.setUpdatedBy( subResponse.getItems().get(0).getUpdatedBy());
            rtn.setIsDeletedParam( subResponse.getItems().get(0).getIsDeletedParam());
            rtn.setUpdatedAtParam( subResponse.getItems().get(0).getUpdatedAtParam());
            rtn.setCreatedAtParam( subResponse.getItems().get(0).getCreatedAtParam());
            rtn.setCreatedByParam( subResponse.getItems().get(0).getCreatedByParam());
            rtn.setUpdatedByParam(subResponse.getItems().get(0).getUpdatedByParam());
            rtn.setOffreVoyageDesignation(subResponse.getItems().get(0).getOffreVoyageDesignation());
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            rtn.setProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getProprieteOffreVoyageDesignation());
            rtn.setTypeProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getTypeProprieteOffreVoyageDesignation());
            return rtn;
        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
