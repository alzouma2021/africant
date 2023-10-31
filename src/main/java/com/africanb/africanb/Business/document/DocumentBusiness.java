package com.africanb.africanb.Business.document;


import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.dao.repository.document.DocumentRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.PaysDTO;
import com.africanb.africanb.helper.dto.document.DocumentDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.document.DocumentTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Cette classe porte sur
 * @author  Alzouma Moussa Mahamadou
 * @date 09/05/2022
 */
@Log
@Component
public class DocumentBusiness implements IBasicBusiness<Request<DocumentDTO>, Response<DocumentDTO>> {


    private Response<DocumentDTO> response;

    private final FunctionalError functionalError;
    private final DocumentRepository documentRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public DocumentBusiness(FunctionalError functionalError, DocumentRepository documentRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.functionalError = functionalError;
        this.documentRepository = documentRepository;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<DocumentDTO> create(Request<DocumentDTO> request, Locale locale) throws ParseException {
        Response<DocumentDTO> response = new Response<DocumentDTO>();
        List<Document> items = new ArrayList<Document>();
        if(request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucun document défini",locale));
            response.setHasError(true);
            return response;
        }
        //Verifications des champs du
        DocumentDTO dto=request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("designation", dto.getDesignation());
        fieldsToVerify.put("path", dto.getPath());
        fieldsToVerify.put("typeMime", dto.getTypeMime());
        fieldsToVerify.put("extension", dto.getExtension());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        //Verification de doublon
        Document existingDocument = null;
        existingDocument = documentRepository.findByDesignation(dto.getDesignation(), false);
        if (existingDocument != null) {
            response.setStatus(functionalError.DATA_EXIST("Document existe dèja", locale));
            response.setHasError(true);
            return response;
        }
        Document entityToSave = DocumentTransformer.INSTANCE.toEntity(dto);
        log.info("_103 PaysDTO transform to Entity :: ="+ entityToSave.toString());

        entityToSave.setIsDeleted(false);
        entityToSave.setCreatedAt(Utilities.getCurrentDate());
        //entityToSave.setCreatedBy(request.user); // à modifier
        Document itemsSaved = null;
        itemsSaved = documentRepository.save(entityToSave);
        if (itemsSaved==null) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de stockage fichier", locale));
            response.setHasError(true);
            return response;
        }
        DocumentDTO itemDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? DocumentTransformer.INSTANCE.toLiteDto(itemsSaved)
                                    : DocumentTransformer.INSTANCE.toDto(itemsSaved);

        response.setItem(itemDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        return response;
    }

    @Override
    public Response<DocumentDTO> update(Request<DocumentDTO> request, Locale locale) throws ParseException {
        /*
        Response<PaysDTO> response = new Response<PaysDTO>();
        List<Pays> items = new ArrayList<Pays>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste de données est vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<PaysDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<PaysDTO>());
        for(PaysDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                //TODO Mise à jour des messages d'erreur
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de la designation'" + dto.getDesignation() + "' pour les pays", locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
            itemsDtos.add(dto);
        }

        for(PaysDTO dto: itemsDtos) {
            Pays entityToSave = paysRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'agence ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                Pays existingEntity = paysRepository.findByDesignation(dto.getDesignation(), false);
                //Verification de l'identifiant
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Pays -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            entityToSave.setDescription(dto.getDescription());
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Modification échouée ",locale));
            response.setHasError(true);
            return response;
        }
        List<PaysDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? PaysTransformer.INSTANCE.toLiteDtos(items)
                                : PaysTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update agence-----");*/
        return response;
    }



    @Override
    public Response<DocumentDTO> delete(Request<DocumentDTO> request, Locale locale) {

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
    public Response<DocumentDTO> forceDelete(Request<DocumentDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<DocumentDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<DocumentDTO> getByCriteria(Request<DocumentDTO> request, Locale locale) {
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<PaysDTO> getAllPays(Request<PaysDTO> request, Locale locale) throws ParseException {
        /*Response<PaysDTO> response = new Response<PaysDTO>();
        List<Pays> items = new ArrayList<Pays>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        items=paysRepository.getAllPays(false );
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucun pays n'existe",locale));
            response.setHasError(true);
            return response;
        }
        List<PaysDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? PaysTransformer.INSTANCE.toLiteDtos(items)
                : PaysTransformer.INSTANCE.toDtos(items);
        //response.setCount(count);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get pays-----");
        return response;*/
        return null;
    }

}
