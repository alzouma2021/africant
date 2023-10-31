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

import javax.persistence.EntityManager;
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
            //fieldsToVerify.put("referenceFamilleId", dto.getReferenceFamilleId());
            fieldsToVerify.put("referenceFamilleDesignation", dto.getReferenceFamilleDesignation());
            //fieldsToVerify.put("familleStatusUtilDesignation", dto.getFamilleStatusUtilDesignation());
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
            Reference existingStatusUtil = null;
            existingStatusUtil = referenceRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingStatusUtil != null) {
                response.setStatus(functionalError.DATA_EXIST("Reference ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            ReferenceFamille existingReferenceFamille = null;
            existingReferenceFamille= referenceFamilleRepository.findByDesignation(itemDto.getReferenceFamilleDesignation(),false);
            if (existingReferenceFamille == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("ReferencFamille ayant pour identifiant -> " + itemDto.getReferenceFamilleId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference entityToSave = ReferenceTransformer.INSTANCE.toEntity(itemDto,existingReferenceFamille);
            log.info("_110 StatusUtilDTO transform to Entity :: ="+ entityToSave.toString());
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }
        List<Reference> itemsSaved = null;
        itemsSaved = referenceRepository.saveAll((Iterable<Reference>) items);
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

        Response<ReferenceDTO> response = new Response<ReferenceDTO>();
        List<Reference> items = new ArrayList<Reference>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<ReferenceDTO>());
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
            //entityToSave.setUpdatedBy(request.user);
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
       log.info("_200 Debut de traitement");
        Response<ReferenceDTO> response = new Response<ReferenceDTO>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<RechercherReferenceDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<RechercherReferenceDTO>());
        for(RechercherReferenceDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("referenceFamilleDesignation", dto.getReferenceFamilleDesignation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        List<Reference> items = null;
        for(RechercherReferenceDTO dto: request.getDatas()) {
            log.info("_Affichage de la designation de la reference famille="+dto.getReferenceFamilleDesignation());
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

/*        log.info("----begin delete agence-----");

        Response<AgenceDto> response = new Response<AgenceDto>();
        List<Agence> items = new ArrayList<Agence>();

        //Verification
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste de données est vide ",locale));
            response.setHasError(true);
            return response;
        }

        //Verification des champs obligatoires
        for(AgenceDto dto : request.getDatas()) {

            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }

        }

        //Parcourir la liste
        for(AgenceDto dto : request.getDatas()){

            // Verification du parametre identifiant
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }

            // Verify if Functionality  exist
            Agence existingEntity = null;

            existingEntity = agenceRepository.findOne(dto.getId(), false);

            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'agence ayant  id -> " + dto.getId() + ",n'existe pas", locale));
                response.setHasError(true);
                return response;
            }

            log.info("_413 Verification d'existence de l'objet"+existingEntity.toString()); //TODO A effacer

            //Suppression logique
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            existingEntity.setDeletedBy(request.user);// a modifier

            items.add(existingEntity);

        }

        //Verificatioon de la liste de données recues
        if(items == null  || items.isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste de données est vide ",locale));
            response.setHasError(true);
            return response;
        }

        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        return response;*/
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
       /*
        log.info("----begin get agence-----");

        Response<AgenceDto> response = new Response<AgenceDto>();

        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }

        List<Agence> items = agenceRepository.getByCriteria(request, em, locale);

        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("Aucune agence ne correspond aux critères de recherche definis", locale));
            response.setHasError(false);
            return response;
        }

        List<AgenceDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                 ? AgenceTransformer.INSTANCE.toLiteDtos(items)
                                 : AgenceTransformer.INSTANCE.toDtos(items);


        response.setItems(itemsDto);
        response.setCount(agenceRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        log.info("----end get agence-----");

        return response;
*/
        return null;
    }

}
