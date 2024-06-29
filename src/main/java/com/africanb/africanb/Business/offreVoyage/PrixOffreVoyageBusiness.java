
package com.africanb.africanb.Business.offreVoyage;



import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.PrixOffreVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
import com.africanb.africanb.helper.transformer.offrreVoyage.PrixOffreVoyageTransformer;
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
public class PrixOffreVoyageBusiness implements IBasicBusiness<Request<PrixOffreVoyageDTO>, Response<PrixOffreVoyageDTO>> {



    private final ReferenceRepository referenceRepository;
    private final PrixOffreVoyageRepository prixOffreVoyageRepository;
    private final OffreVoyageRepository offreVoyageRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public PrixOffreVoyageBusiness(ReferenceRepository referenceRepository, PrixOffreVoyageRepository prixOffreVoyageRepository, OffreVoyageRepository offreVoyageRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.referenceRepository = referenceRepository;
        this.prixOffreVoyageRepository = prixOffreVoyageRepository;
        this.offreVoyageRepository = offreVoyageRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<PrixOffreVoyageDTO> create(Request<PrixOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<PrixOffreVoyageDTO> response = new Response<>();
        List<PrixOffreVoyage> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<PrixOffreVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(PrixOffreVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("modeDesignation", dto.getModeDesignation());
            fieldsToVerify.put("categorieVoyageur", dto.getCategorieVoyageurDesignation());
            fieldsToVerify.put("prix", dto.getPrix());
            fieldsToVerify.put("offreVoyageDesignation", dto.getOffreVoyageDesignation());
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
        for(PrixOffreVoyageDTO itemDto : itemsDtos){
            PrixOffreVoyage existingPrixOffreVoyage = prixOffreVoyageRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingPrixOffreVoyage != null) {
                response.setStatus(functionalError.DATA_EXIST("PrixOffreVoyage ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(itemDto.getOffreVoyageDesignation(),false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("L'offre de voyage ayant  pour identifiant -> " + itemDto.getOffreVoyageDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingMode = referenceRepository.findByDesignation(itemDto.getModeDesignation(),false);
            if (existingMode == null) {
                response.setStatus(functionalError.DATA_EXIST("Mode ayant  pour identifiant -> " + itemDto.getModeDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingCategorieVoyageur = referenceRepository.findByDesignation(itemDto.getCategorieVoyageurDesignation(),false);
            if (existingCategorieVoyageur == null) {
                response.setStatus(functionalError.DATA_EXIST("CategorieVoyageur ayant  pour identifiant -> " + itemDto.getCategorieVoyageurDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            PrixOffreVoyage entityToSave = PrixOffreVoyageTransformer.INSTANCE.toEntity(itemDto,existingMode,existingOffreVoyage,existingCategorieVoyageur);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        List<PrixOffreVoyage> itemsSaved = prixOffreVoyageRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<PrixOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? PrixOffreVoyageTransformer.INSTANCE.toLiteDtos(itemsSaved)
                : PrixOffreVoyageTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<PrixOffreVoyageDTO> update(Request<PrixOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<PrixOffreVoyageDTO> response = new Response<>();
        List<PrixOffreVoyage> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<PrixOffreVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(PrixOffreVoyageDTO dto: request.getDatas() ) {
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
        for(PrixOffreVoyageDTO dto: itemsDtos) {
            PrixOffreVoyage entityToSave = prixOffreVoyageRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le prix de l'offre de voyage ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                PrixOffreVoyage existingPrixOffreVoyage = prixOffreVoyageRepository.findByDesignation(dto.getDesignation(), false);
                if (existingPrixOffreVoyage != null && !existingPrixOffreVoyage.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("PrixOffreVoyage -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String modeDesignation=entityToSave.getMode()!=null&&entityToSave.getMode().getDesignation()!=null
                    ?entityToSave.getMode().getDesignation()
                    :null;
            if(modeDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Le prix offre de voyage ne comporte aucun mode", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingMode = referenceRepository.findByDesignation(modeDesignation,false);
            if (existingMode == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le mode du prix de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getModeDesignation()) && !dto.getModeDesignation().equals(existingMode.getDesignation())) {
                Reference modeToSave = referenceRepository.findByDesignation(dto.getModeDesignation(), false);
                if (modeToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le nouveau mode du prix de l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setMode(modeToSave);
            }
            String categorieVoyageurDesignation=entityToSave.getCategorieVoyageur()!=null&&entityToSave.getCategorieVoyageur().getDesignation()!=null
                    ?entityToSave.getCategorieVoyageur().getDesignation()
                    :null;
            if(categorieVoyageurDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Le prix offre de voyage ne comporte aucune categorie de voyage", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingCategorieVoyageur = referenceRepository.findByDesignation(categorieVoyageurDesignation,false);
            if (existingCategorieVoyageur == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La categorie du voyageur du prix de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getCategorieVoyageurDesignation()) && !dto.getCategorieVoyageurDesignation().equals(existingCategorieVoyageur.getDesignation())) {
                Reference categorieVoyageurToSave = referenceRepository.findByDesignation(dto.getCategorieVoyageurDesignation(), false);
                if (categorieVoyageurToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle categorie du prix de l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setCategorieVoyageur(categorieVoyageurToSave);
            }
            String offreVoyageDesignation=entityToSave.getOffreVoyage()!=null&&entityToSave.getOffreVoyage().getDesignation()!=null
                    ?entityToSave.getOffreVoyage().getDesignation()
                    :null;
            if(offreVoyageDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Le prix offre de voyage n'est relié à aucune offre de voyage", locale));
                response.setHasError(true);
                return response;
            }
            OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(offreVoyageDesignation,false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage du prix de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (existingOffreVoyage.getIsActif()!=null && existingOffreVoyage.getIsActif() == true) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Desactivez l'offre de voyage avant de proceder au changement du prix", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getOffreVoyageDesignation()) && !dto.getOffreVoyageDesignation().equals(existingOffreVoyage.getDesignation())) {
                Reference offreVoyageToSave = referenceRepository.findByDesignation(dto.getOffreVoyageDesignation(), false);
                if (offreVoyageToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle categorie du prix de l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setCategorieVoyageur(offreVoyageToSave);
            }
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDescription().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(Utilities.isValidID(dto.getPrix())&&!dto.getPrix().equals(entityToSave.getPrix())){
                entityToSave.setPrix(dto.getPrix());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Modification échouée ",locale));
            response.setHasError(true);
            return response;
        }

        List<PrixOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? PrixOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                : PrixOffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update priix de l'offre de voyage-----");
        return response;
    }

    @Override
    public Response<PrixOffreVoyageDTO> delete(Request<PrixOffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<PrixOffreVoyageDTO> forceDelete(Request<PrixOffreVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<PrixOffreVoyageDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<PrixOffreVoyageDTO> getByCriteria(Request<PrixOffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<PrixOffreVoyageDTO> getPrixTravelOfferByOffreVoyageDesignation(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<PrixOffreVoyageDTO> response = new Response<>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("offrVoyageDesigntaion", request.getData().getDesignation());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String offreVoyageDesignation=request.getData().getDesignation();
        OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(offreVoyageDesignation,false);
        if (existingOffreVoyage == null) {
            response.setStatus(functionalError.DATA_EXIST("L'offre de voyage n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        List<PrixOffreVoyage> items = prixOffreVoyageRepository.findAllByOffreVoyageDesignation(offreVoyageDesignation,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voayage ne dispose d'aucun prix", locale));
            response.setHasError(true);
            return response;
        }

        List<PrixOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? PrixOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                : PrixOffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end Listing prix offre voyage-----");
        return response;
    }
}