package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.Pays;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.repository.compagnie.PaysRepository;
import com.africanb.africanb.dao.repository.compagnie.VilleRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
import com.africanb.africanb.helper.transformer.compagnie.VilleTransformer;
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
public class VilleBusiness implements IBasicBusiness<Request<VilleDTO>, Response<VilleDTO>> {


    private Response<VilleDTO> response;

    private final PaysRepository paysRepository;
    private final VilleRepository villeRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public VilleBusiness(PaysRepository paysRepository, VilleRepository villeRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.paysRepository = paysRepository;
        this.villeRepository = villeRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<VilleDTO> create(Request<VilleDTO> request, Locale locale) throws ParseException {
        Response<VilleDTO> response = new Response<>();
        List<Ville> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<VilleDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(VilleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("paysId", dto.getPaysId());
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
        for(VilleDTO itemDto : itemsDtos){
            Ville existingVille = villeRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingVille != null) {
                response.setStatus(functionalError.DATA_EXIST("Ville ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            Pays existingPays = paysRepository.findOne(itemDto.getPaysId(),false);
            if (existingPays == null) {
                response.setStatus(functionalError.DATA_EXIST("Pays ayant  pour identifiant -> " + itemDto.getPaysId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Ville entityToSave = VilleTransformer.INSTANCE.toEntity(itemDto,existingPays);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        List<Ville> itemsSaved = villeRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<VilleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? VilleTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : VilleTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<VilleDTO> update(Request<VilleDTO> request, Locale locale) throws ParseException {
        Response<VilleDTO> response = new Response<>();
        List<Ville> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<VilleDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(VilleDTO dto: request.getDatas() ) {
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
        for(VilleDTO dto: itemsDtos) {
            Ville entityToSave = villeRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La ville ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                Ville existingVille = villeRepository.findByDesignation(dto.getDesignation(), false);
                if (existingVille != null && !existingVille.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Ville -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String paysDesignation=entityToSave.getPays()!=null&&entityToSave.getPays().getDesignation()!=null
                                       ?entityToSave.getPays().getDesignation()
                                       :null;
            if(paysDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("La ville n'est rattachée à aucun pays", locale));
                response.setHasError(true);
                return response;
            }
            Pays existingPays = paysRepository.findByDesignation(paysDesignation,false);
            if (existingPays == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le pays de la ville n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getPaysDesignation()) && !dto.getPaysDesignation().equals(existingPays.getDesignation())) {
                Pays paysToSave = paysRepository.findByDesignation(dto.getPaysDesignation(), false);
                if (paysToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le pays de la ville n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setPays(paysToSave);
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
        List<VilleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? VilleTransformer.INSTANCE.toLiteDtos(items)
                                : VilleTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update ville-----");
        return response;
    }

    @Override
    public Response<VilleDTO> delete(Request<VilleDTO> request, Locale locale) {
        return new Response<>();
    }

    @Override
    public Response<VilleDTO> forceDelete(Request<VilleDTO> request, Locale locale) {
        return new Response<>();
    }

    @Override
    public Response<VilleDTO> getAll(Locale locale) throws ParseException {
        return new Response<>();
    }

    @Override
    public Response<VilleDTO> getByCriteria(Request<VilleDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<VilleDTO> getAllCities(Request<VilleDTO> request, Locale locale) throws ParseException {
        Response<VilleDTO> response = new Response<>();
        Long count = villeRepository.countAllCities(false);
        List<Ville> items = villeRepository.getAllCities(false );
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune ville n'est trouvée",locale));
            response.setHasError(true);
            return response;
        }
        List<VilleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? VilleTransformer.INSTANCE.toLiteDtos(items)
                                : VilleTransformer.INSTANCE.toDtos(items);
        response.setCount(count);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update ville-----");
        return response;
    }
}
