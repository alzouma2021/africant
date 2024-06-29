package com.africanb.africanb.utils.Reference;


import com.africanb.africanb.dao.repository.Reference.ReferenceFamilleRepository;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
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
public class ReferenceBusines implements IBasicBusiness<Request<ReferenceDTO>, Response<ReferenceDTO>> {


    private final ReferenceRepository referenceRepository;
    private final ReferenceFamilleRepository referenceFamilleRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ReferenceBusines(ReferenceRepository referenceRepository, ReferenceFamilleRepository referenceFamilleRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.referenceRepository = referenceRepository;
        this.referenceFamilleRepository = referenceFamilleRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ReferenceDTO> create(Request<ReferenceDTO> request, Locale locale) throws ParseException {
        Response<ReferenceDTO> response = new Response<ReferenceDTO>();
        List<Reference> items = new ArrayList<Reference>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<ReferenceDTO>());
        for(ReferenceDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("referenceFamilleDesignation", dto.getReferenceFamilleDesignation());
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
        for(ReferenceDTO itemDto : itemsDtos){
            Reference existingStatusUtil = referenceRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingStatusUtil != null) {
                response.setStatus(functionalError.DATA_EXIST("Reference ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            ReferenceFamille existingReferenceFamille= referenceFamilleRepository.findByDesignation(itemDto.getReferenceFamilleDesignation(),false);
            if (existingReferenceFamille == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("ReferencFamille ayant pour identifiant -> " + itemDto.getReferenceFamilleId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference entityToSave = ReferenceTransformer.INSTANCE.toEntity(itemDto,existingReferenceFamille);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        List<Reference> itemsSaved = referenceRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ReferenceTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : ReferenceTransformer.INSTANCE.toDtos(itemsSaved);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ReferenceDTO> update(Request<ReferenceDTO> request, Locale locale) throws ParseException {
        Response<ReferenceDTO> response = new Response<>();
        List<Reference> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(ReferenceDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
        for(ReferenceDTO dto: itemsDtos) {
            Reference entityToSave = referenceRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Reference ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                Reference existingReference = referenceRepository.findByDesignation(dto.getDesignation(), false);
                if (existingReference != null && !existingReference.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("reference -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String referenceFamilleDesignation=entityToSave.getReferenceFamille()!=null&&entityToSave.getReferenceFamille().getDesignation()!=null
                                       ?entityToSave.getReferenceFamille().getDesignation()
                                       :null;
            if(referenceFamilleDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Reference n'est rattachée à auncune famille", locale));
                response.setHasError(true);
                return response;
            }
            ReferenceFamille existingReferenceFamille = referenceFamilleRepository.findByDesignation(referenceFamilleDesignation,false);
            if (existingReferenceFamille == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La famille de la reference n'existe pas-> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDesignation().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Modification échouée ",locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? ReferenceTransformer.INSTANCE.toLiteDtos(items)
                                : ReferenceTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update ville-----");
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ReferenceDTO> getReferenceByReferenceFamilleDesignation(Request<RechercherReferenceDTO> request, Locale locale) throws ParseException {
        Response<ReferenceDTO> response = new Response<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(RechercherReferenceDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("referenceFamilleDesignation", dto.getReferenceFamilleDesignation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        List<Reference> items = null;
        for(RechercherReferenceDTO dto: request.getDatas()) {
            items=referenceRepository.findByReferenceFamilleDesignation(dto.getReferenceFamilleDesignation(),false);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune reference trouvée ",locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ReferenceTransformer.INSTANCE.toLiteDtos(items)
                : ReferenceTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update ville-----");
        return response;
    }


    @Override
    public Response<ReferenceDTO> delete(Request<ReferenceDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ReferenceDTO> forceDelete(Request<ReferenceDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ReferenceDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ReferenceDTO> getByCriteria(Request<ReferenceDTO> request, Locale locale) {
        return null;
    }

}
