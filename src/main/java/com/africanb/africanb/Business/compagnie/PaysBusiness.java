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
        Response<PaysDTO> response = new Response<PaysDTO>();
        List<Pays> items = new ArrayList<Pays>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
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
            Pays existingEntity = paysRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("Pays ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            Pays entityToSave = PaysTransformer.INSTANCE.toEntity(itemDto);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        List<Pays> itemsSaved = paysRepository.saveAll(items);
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
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Pays -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            entityToSave.setDescription(dto.getDescription());
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
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
        return new Response<>();
    }

    @Override
    public Response<PaysDTO> forceDelete(Request<PaysDTO> request, Locale locale) {
        return new Response<>() ;
    }

    @Override
    public Response<PaysDTO> getAll(Locale locale) throws ParseException {
        return new Response<>();
    }

    @Override
    public Response<PaysDTO> getByCriteria(Request<PaysDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<PaysDTO> getAllPays(Request<PaysDTO> request, Locale locale) throws ParseException {
        Response<PaysDTO> response = new Response<>();

        List<Pays>  items =paysRepository.getAllPays(false );
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucun pays n'existe",locale));
            response.setHasError(true);
            return response;
        }

        List<PaysDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? PaysTransformer.INSTANCE.toLiteDtos(items)
                : PaysTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get pays-----");
        return response;
    }

}
