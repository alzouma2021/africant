package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.Pays;
import com.africanb.africanb.dao.repository.compagnie.PaysRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.PaysDTO;
import com.africanb.africanb.helper.transformer.compagnie.PaysTransformer;
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

/**
 * Cette classe traite les operations portant sur les agences
 * @author  Alzouma Moussa Mahamadou
 * @date 09/05/2022
 */
@Log
@Component
public class PaysBusiness implements IBasicBusiness<Request<PaysDTO>, Response<PaysDTO>> {


    private Response<PaysDTO> response;

    private final PaysRepository paysRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public PaysBusiness(PaysRepository paysRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.paysRepository = paysRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<PaysDTO> create(Request<PaysDTO> request, Locale locale) throws ParseException {
        log.info("_66 Traitement des pays"); //TODO A effacer
        Response<PaysDTO> response = new Response<PaysDTO>();
        List<Pays> items = new ArrayList<Pays>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            //TODO Mise à jour des messages derreur
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<PaysDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<PaysDTO>());
        for(PaysDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
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
        }
        for(PaysDTO itemDto : itemsDtos){
            Pays existingEntity = null;
            existingEntity = paysRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("Pays ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            Pays entityToSave = PaysTransformer.INSTANCE.toEntity(itemDto);
            log.info("_103 PaysDTO transform to Entity :: ="+ entityToSave.toString());

            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }
        List<Pays> itemsSaved = null;
        itemsSaved = paysRepository.saveAll((Iterable<Pays>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<PaysDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? PaysTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : PaysTransformer.INSTANCE.toDtos(itemsSaved);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        return response;
    }

    @Override
    public Response<PaysDTO> update(Request<PaysDTO> request, Locale locale) throws ParseException {

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
        log.info("----end update agence-----");
        return response;
    }



    @Override
    public Response<PaysDTO> delete(Request<PaysDTO> request, Locale locale) {

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
    public Response<PaysDTO> forceDelete(Request<PaysDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<PaysDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<PaysDTO> getByCriteria(Request<PaysDTO> request, Locale locale) {
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
        Response<PaysDTO> response = new Response<PaysDTO>();
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
        return response;
    }

}
