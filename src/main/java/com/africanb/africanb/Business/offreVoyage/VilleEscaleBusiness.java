package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.VilleEscale;
import com.africanb.africanb.dao.repository.compagnie.VilleRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.VilleEscaleRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.VilleEscaleDTO;
import com.africanb.africanb.helper.transformer.compagnie.VilleTransformer;
import com.africanb.africanb.helper.transformer.offrreVoyage.VilleEscaleTransformer;
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
public class VilleEscaleBusiness implements IBasicBusiness<Request<VilleEscaleDTO>, Response<VilleEscaleDTO>> {

    private Response<VilleEscaleDTO> response;

    private final OffreVoyageRepository offreVoyageRepository;
    private final VilleRepository villeRepository;
    private final VilleEscaleRepository villeEscaleRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public VilleEscaleBusiness(OffreVoyageRepository offreVoyageRepository, VilleRepository villeRepository, VilleEscaleRepository villeEscaleRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.offreVoyageRepository = offreVoyageRepository;
        this.villeRepository = villeRepository;
        this.villeEscaleRepository = villeEscaleRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    @Override
    public Response<VilleEscaleDTO> create(Request<VilleEscaleDTO> request, Locale locale) throws ParseException {
        Response<VilleEscaleDTO> response = new Response<VilleEscaleDTO>();
        List<VilleEscale> items = new ArrayList<VilleEscale>();
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<VilleEscaleDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<VilleEscaleDTO>());
        for(VilleEscaleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("villeDesignation", dto.getVilleDesignation());
            fieldsToVerify.put("position", dto.getPosition());
            fieldsToVerify.put("offreVoyageDesignation", dto.getOffreVoyageDesignation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            /*if(itemsDtos.stream().anyMatch(a->a.getOffreVoyageDesignation().equals(dto.getOffreVoyageDesignation()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de l'offre de voyage'" + dto.getOffreVoyageDesignation() , locale));
                response.setHasError(true);
                return response;
            }*/
            itemsDtos.add(dto);
        }
        for(VilleEscaleDTO dto : itemsDtos){
            VilleEscale existingEntity = null;
            existingEntity = villeEscaleRepository.findByOffreVoyageAndVille(dto.getOffreVoyageDesignation(),dto.getVilleDesignation(),false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("VilleEscale ", locale));
                response.setHasError(true);
                return response;
            }
            OffreVoyage existingOffreVoyage = null;
            existingOffreVoyage = offreVoyageRepository.findByDesignation(dto.getOffreVoyageDesignation(), false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("offreVoyage offreVoyageID -> " + dto.getOffreVoyageDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVille = null;
            existingVille = villeRepository.findByDesignation(dto.getVilleDesignation(), false);
            if (existingVille == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("ville villeID -> " + dto.getVilleDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            VilleEscale entityToSave = VilleEscaleTransformer
                                        .INSTANCE.toEntity(dto, existingOffreVoyage, existingVille);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user);
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur creation ",locale));
            response.setHasError(true);
            return response;
        }
        List<VilleEscale> itemsSaved = null;
        itemsSaved = villeEscaleRepository.saveAll((Iterable<VilleEscale>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur creation", locale));
            response.setHasError(true);
            return response;
        }
        List<VilleEscaleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? VilleEscaleTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                : VilleEscaleTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<VilleEscaleDTO> update(Request<VilleEscaleDTO> request, Locale locale) throws ParseException {
        Response<VilleEscaleDTO> response = new Response<VilleEscaleDTO>();
        List<VilleEscale> items = new ArrayList<VilleEscale>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<VilleEscaleDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<VilleEscaleDTO>());
        for(VilleEscaleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication offreVoyage '" + dto.getOffreVoyageDesignation() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(VilleEscaleDTO dto : itemsDtos){
            VilleEscale entityToSave = null;
            entityToSave = villeEscaleRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("villeEscale id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            OffreVoyage existingOffreVoyage = null;
            if (Utilities.isNotBlank(dto.getOffreVoyageDesignation()) && !entityToSave.getOffreVoyage().getDesignation().equalsIgnoreCase(dto.getOffreVoyageDesignation())) {
                existingOffreVoyage = offreVoyageRepository.findByDesignation(dto.getOffreVoyageDesignation(), false);
                if (existingOffreVoyage == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("OffreVoyageDesignation -> " + dto.getOffreVoyageDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setOffreVoyage(existingOffreVoyage);
            }
            Ville existingVille = null;
            if (Utilities.isNotBlank(dto.getVilleDesignation()) && !entityToSave.getVille().getDesignation().equalsIgnoreCase(dto.getVilleDesignation())) {
                existingVille = villeRepository.findByDesignation(dto.getVilleDesignation(), false);
                if (existingVille == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Ville villeDesignation -> " + dto.getVilleDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setVille(existingVille);
            }
            if(Utilities.isValidID(dto.getPosition()) && !dto.getPosition().equals(entityToSave.getPosition())){
                entityToSave.setPosition(dto.getPosition());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            items.add(entityToSave);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification ",locale));
            response.setHasError(true);
        }
        List<VilleEscale> itemsSaved = null;
        itemsSaved = villeEscaleRepository.saveAll((Iterable<VilleEscale>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
                response.setStatus(functionalError.SAVE_FAIL("VilleEscale", locale));
                response.setHasError(true);
                return response;
        }
        //Transformation
        List<VilleEscaleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? VilleEscaleTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : VilleEscaleTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<VilleEscaleDTO> delete(Request<VilleEscaleDTO> request, Locale locale) {
        Response<VilleEscaleDTO> response = new Response<VilleEscaleDTO>();
        List<VilleEscale> items = new ArrayList<VilleEscale>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        for(VilleEscaleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        for(VilleEscaleDTO dto : request.getDatas()){
            VilleEscale existingEntity = null;
            existingEntity = villeEscaleRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("VillEsclae id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            //existingEntity.setDeletedBy(request.user);
            items.add(existingEntity);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<VilleEscaleDTO> forceDelete(Request<VilleEscaleDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<VilleEscaleDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<VilleEscaleDTO> getByCriteria(Request<VilleEscaleDTO> request, Locale locale) {

    /*    log.info("----begin get HistoriqueDemande-----");

        Response<StatusUtilCompagnieTransportDTO> response = new Response<StatusUtilCompagnieTransportDTO>();

        //verification si le parametre d'ordre à été fourni, sinon nous mettons le paramètre à vide
        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }

        //verification si le parametre direction à été fourni, sinon nous mettons le paramètre ascendant( du plus ancien au plus ressent)
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }

        //recuperation des entités en base
        List<HistoriqueDemande> items = statusUtilCompagnieTransportRepository.getByCriteria(request, em, locale);

        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("HistoriqueDemande", locale));
            response.setHasError(false);
            return response;
        }

        //Transformation
        List<HistoriqueDemandeDto> itemsDto = HistoriqueDemandeTransformer.INSTANCE.toDtos(items);

        //Envoie de la reponse
        response.setItems(itemsDto);
        response.setCount(statusUtilCompagnieTransportRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        log.info("----end get HistoriqueDemande-----");

        return response;
        */
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<VilleDTO> getVilleByOffreVoyageDesignation(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<VilleDTO> response = new Response<VilleDTO>();
        List<Ville> items = Collections.synchronizedList(new ArrayList<Ville>());
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("offrVoyageDesigntaion", request.getData().getDesignation());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String offreVoyageDesignation=request.getData().getDesignation();
        OffreVoyage existingOffreVoyage = null;
        existingOffreVoyage= offreVoyageRepository.findByDesignation(offreVoyageDesignation,false);
        if (existingOffreVoyage == null) {
            response.setStatus(functionalError.DATA_EXIST("L'offre de voyage n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        items =villeEscaleRepository.getVilleByOffreVoyageDesignation(offreVoyageDesignation,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voayage ne dispose d'aucune ville escale", locale));
            response.setHasError(true);
            return response;
        }
        List<VilleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? VilleTransformer.INSTANCE.toLiteDtos(items)
                : VilleTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end Ville Escale Offre Voyage-----");
        return response;
    }
}
