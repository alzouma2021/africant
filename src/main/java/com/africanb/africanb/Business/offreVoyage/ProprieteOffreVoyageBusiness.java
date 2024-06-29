package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProprieteOffreVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.ProprieteOffreVoyageDTO;
import com.africanb.africanb.helper.transformer.offrreVoyage.ProprieteOffreVoyageTransformer;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Reference.Reference;
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
public class ProprieteOffreVoyageBusiness implements IBasicBusiness<Request<ProprieteOffreVoyageDTO>, Response<ProprieteOffreVoyageDTO>> {


    private ReferenceRepository referenceRepository;
    private final ProprieteOffreVoyageRepository proprieteOffreVoyageRepository;
    private final ProgrammeBusiness programmeBusiness;
    private final OffreVoyageRepository offreVoyageRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ProprieteOffreVoyageBusiness(ReferenceRepository referenceRepository, ProprieteOffreVoyageRepository proprieteOffreVoyageRepository, ProgrammeBusiness programmeBusiness, OffreVoyageRepository offreVoyageRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.referenceRepository = referenceRepository;
        this.proprieteOffreVoyageRepository = proprieteOffreVoyageRepository;
        this.programmeBusiness = programmeBusiness;
        this.offreVoyageRepository = offreVoyageRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> create(Request<ProprieteOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ProprieteOffreVoyageDTO> response = new Response<>();
        List<ProprieteOffreVoyage> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(ProprieteOffreVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("typeProprieteOffreVoyageDesignationDesignation", dto.getTypeProprieteOffreVoyageDesignation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de la designation'" + dto.getDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(ProprieteOffreVoyageDTO itemDto : itemsDtos){
            ProprieteOffreVoyage existingProprieteOffreVoyage = proprieteOffreVoyageRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingProprieteOffreVoyage != null) {
                response.setStatus(functionalError.DATA_EXIST("ProprieteOffreVoyage ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeProprieteOffreVoyage = referenceRepository.findByDesignation(itemDto.getTypeProprieteOffreVoyageDesignation(),false);
            if (existingTypeProprieteOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de propriété de l'offre de voyage ayant  pour identifiant -> " + itemDto.getTypeProprieteOffreVoyageDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ProprieteOffreVoyage entityToSave = ProprieteOffreVoyageTransformer.INSTANCE.toEntity(itemDto,existingTypeProprieteOffreVoyage);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            ProprieteOffreVoyage entitySaved = proprieteOffreVoyageRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ProprieteOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                                    : ProprieteOffreVoyageTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> update(Request<ProprieteOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ProprieteOffreVoyageDTO> response = new Response<>();
        List<ProprieteOffreVoyage> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(ProprieteOffreVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
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
        for(ProprieteOffreVoyageDTO dto: itemsDtos) {
            ProprieteOffreVoyage entityToSave = proprieteOffreVoyageRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La propriété de l'offre de voyage ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                ProprieteOffreVoyage existingProprieteOffrrVoyage = proprieteOffreVoyageRepository.findByDesignation(dto.getDesignation(), false);
                if (existingProprieteOffrrVoyage != null && !existingProprieteOffrrVoyage.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("ProprieteOffreVoyage -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String typeProprieteOffreVoyage=entityToSave.getTypeProprieteOffreVoyage()!=null&&entityToSave.getTypeProprieteOffreVoyage().getDesignation()!=null
                                       ?entityToSave.getTypeProprieteOffreVoyage().getDesignation()
                                       :null;
            if(typeProprieteOffreVoyage==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Aucun type défini pour la propriété", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeProprieteOffreVoyage = referenceRepository.findByDesignation(typeProprieteOffreVoyage,false);
            if (existingTypeProprieteOffreVoyage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le type de propriété de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getTypeProprieteOffreVoyageDesignation()) && !dto.getTypeProprieteOffreVoyageDesignation().equals(existingTypeProprieteOffreVoyage.getDesignation())) {
                Reference typeProprieteOffreVoyageToSave = referenceRepository.findByDesignation(dto.getTypeProprieteOffreVoyageDesignation(), false);
                if (typeProprieteOffreVoyageToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le nouveau type ayant l'identifiant " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setTypeProprieteOffreVoyage(typeProprieteOffreVoyageToSave);
            }
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDesignation().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(dto.getEstObligatoire() != entityToSave.getEstObligatoire()){
                entityToSave.setEstObligatoire(dto.getEstObligatoire());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            ProprieteOffreVoyage entityUpdated = proprieteOffreVoyageRepository.save(entityToSave);
            items.add(entityUpdated);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification, ",locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? ProprieteOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                                : ProprieteOffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Jour semaine-----");
        return response;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> delete(Request<ProprieteOffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> forceDelete(Request<ProprieteOffreVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> getByCriteria(Request<ProprieteOffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<ProprieteOffreVoyageDTO> getAllProprieteOffreVoyage(Request<ProprieteOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ProprieteOffreVoyageDTO> response = new Response<>();
        List<ProprieteOffreVoyage> items=proprieteOffreVoyageRepository.findAll(false);
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune propriété offre voyage n'est définie",locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ProprieteOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                : ProprieteOffreVoyageTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

}
