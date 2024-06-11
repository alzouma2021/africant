package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.FamilleStatusUtil;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.repository.compagnie.FamilleStatusUtilRepository;
import com.africanb.africanb.dao.repository.compagnie.StatusUtilRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.StatusUtilDTO;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
import com.africanb.africanb.helper.transformer.compagnie.StatusUtilTransformer;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
@Component
public class StatusUtilBusiness implements IBasicBusiness<Request<StatusUtilDTO>, Response<StatusUtilDTO>> {


    private Response<VilleDTO> response;

    private final StatusUtilRepository statusUtilRepository;
    private final FamilleStatusUtilRepository familleStatusUtilRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public StatusUtilBusiness(StatusUtilRepository statusUtilRepository, FamilleStatusUtilRepository familleStatusUtilRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.statusUtilRepository = statusUtilRepository;
        this.familleStatusUtilRepository = familleStatusUtilRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<StatusUtilDTO> create(Request<StatusUtilDTO> request, Locale locale) throws ParseException {
        Response<StatusUtilDTO> response = new Response<StatusUtilDTO>();
        List<StatusUtil> items = new ArrayList<StatusUtil>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<StatusUtilDTO>());
        for(StatusUtilDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("familleStatusUtilId", dto.getFamilleStatusUtilId());
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
        for(StatusUtilDTO itemDto : itemsDtos){
            StatusUtil existingStatusUtil = null;
            existingStatusUtil = statusUtilRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingStatusUtil != null) {
                response.setStatus(functionalError.DATA_EXIST("StatusUtil ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            FamilleStatusUtil existingFamilleStatusUtil = null;
            existingFamilleStatusUtil=familleStatusUtilRepository.findOne(itemDto.getFamilleStatusUtilId(),false);
            if (existingFamilleStatusUtil == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("FamilleStatusUtil ayant pour identifiant -> " + itemDto.getFamilleStatusUtilId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            StatusUtil entityToSave = StatusUtilTransformer.INSTANCE.toEntity(itemDto,existingFamilleStatusUtil);
            log.info("_110 StatusUtilDTO transform to Entity :: ="+ entityToSave.toString());
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }
        List<StatusUtil> itemsSaved = null;
        itemsSaved = statusUtilRepository.saveAll((Iterable<StatusUtil>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? StatusUtilTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : StatusUtilTransformer.INSTANCE.toDtos(itemsSaved);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<StatusUtilDTO> update(Request<StatusUtilDTO> request, Locale locale) throws ParseException {

        Response<StatusUtilDTO> response = new Response<StatusUtilDTO>();
        List<StatusUtil> items = new ArrayList<StatusUtil>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<StatusUtilDTO>());
        for(StatusUtilDTO dto: request.getDatas() ) {
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
        for(StatusUtilDTO dto: itemsDtos) {
            StatusUtil entityToSave = statusUtilRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtil ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                StatusUtil existingStatusUtil = statusUtilRepository.findByDesignation(dto.getDesignation(), false);
                if (existingStatusUtil != null && !existingStatusUtil.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("statusUtil -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String familleStatusUtilDesignation=entityToSave.getFamilleStatusUtil()!=null&&entityToSave.getFamilleStatusUtil().getDesignation()!=null
                                       ?entityToSave.getFamilleStatusUtil().getDesignation()
                                       :null;
            if(familleStatusUtilDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtil n'est rattaché à auncune famille", locale));
                response.setHasError(true);
                return response;
            }
            FamilleStatusUtil existingFamilleStatusUtil = familleStatusUtilRepository.findByDesignation(familleStatusUtilDesignation,false);
            if (existingFamilleStatusUtil == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La famille du status n'existe pas-> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getFamilleStatusUtilDesignation()) && !dto.getFamilleStatusUtilDesignation().equals(existingFamilleStatusUtil.getDesignation())) {
                FamilleStatusUtil familleStatusUtilToSave = familleStatusUtilRepository.findByDesignation(dto.getFamilleStatusUtilDesignation(), false);
                if (familleStatusUtilToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le pays de la ville n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setFamilleStatusUtil(familleStatusUtilToSave);
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
        List<StatusUtilDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? StatusUtilTransformer.INSTANCE.toLiteDtos(items)
                                : StatusUtilTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update ville-----");
        return response;
    }

    @Override
    public Response<StatusUtilDTO> delete(Request<StatusUtilDTO> request, Locale locale) {
        return new Response<>();
    }

    @Override
    public Response<StatusUtilDTO> forceDelete(Request<StatusUtilDTO> request, Locale locale) {
        return new Response<>();
    }

    @Override
    public Response<StatusUtilDTO> getAll(Locale locale) throws ParseException {
        return new Response<>();
    }

    @Override
    public Response<StatusUtilDTO> getByCriteria(Request<StatusUtilDTO> request, Locale locale) {
        return new Response<>();
    }
}
