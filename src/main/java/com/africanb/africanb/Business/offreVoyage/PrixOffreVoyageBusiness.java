
package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
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
import com.africanb.africanb.helper.transformer.offrreVoyage.OffreVoyageTransformer;
import com.africanb.africanb.helper.transformer.offrreVoyage.PrixOffreVoyageTransformer;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Reference.Reference;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ALZOUMA MOUSSA MAHAAMADOU
 */
@Log
@Component
public class PrixOffreVoyageBusiness implements IBasicBusiness<Request<PrixOffreVoyageDTO>, Response<PrixOffreVoyageDTO>> {


    private Response<PrixOffreVoyageDTO> response;
    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private PrixOffreVoyageRepository prixOffreVoyageRepository;
    @Autowired
    private OffreVoyageRepository offreVoyageRepository;
    @Autowired
    private FunctionalError functionalError;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public PrixOffreVoyageBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<PrixOffreVoyageDTO> create(Request<PrixOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<PrixOffreVoyageDTO> response = new Response<PrixOffreVoyageDTO>();
        List<PrixOffreVoyage> items = new ArrayList<PrixOffreVoyage>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<PrixOffreVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<PrixOffreVoyageDTO>());
        for(PrixOffreVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
            PrixOffreVoyage existingPrixOffreVoyage = null;
            existingPrixOffreVoyage = prixOffreVoyageRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingPrixOffreVoyage != null) {
                response.setStatus(functionalError.DATA_EXIST("PrixOffreVoyage ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            OffreVoyage existingOffreVoyage = null;
            existingOffreVoyage= offreVoyageRepository.findByDesignation(itemDto.getOffreVoyageDesignation(),false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("L'offre de voyage ayant  pour identifiant -> " + itemDto.getOffreVoyageDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingMode = null;
            existingMode= referenceRepository.findByDesignation(itemDto.getModeDesignation(),false);
            if (existingMode == null) {
                response.setStatus(functionalError.DATA_EXIST("Mode ayant  pour identifiant -> " + itemDto.getModeDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingCategorieVoyageur = null;
            existingCategorieVoyageur= referenceRepository.findByDesignation(itemDto.getCategorieVoyageurDesignation(),false);
            if (existingCategorieVoyageur == null) {
                response.setStatus(functionalError.DATA_EXIST("CategorieVoyageur ayant  pour identifiant -> " + itemDto.getCategorieVoyageurDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            PrixOffreVoyage entityToSave = PrixOffreVoyageTransformer.INSTANCE.toEntity(itemDto,existingMode,existingOffreVoyage,existingCategorieVoyageur);
            log.info("_105 VilleDTO transform to Entity :: ="+ entityToSave.toString());
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }
        List<PrixOffreVoyage> itemsSaved = null;
        itemsSaved = prixOffreVoyageRepository.saveAll((Iterable<PrixOffreVoyage>) items);
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
        Response<PrixOffreVoyageDTO> response = new Response<PrixOffreVoyageDTO>();
        List<PrixOffreVoyage> items = new ArrayList<PrixOffreVoyage>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<PrixOffreVoyageDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<PrixOffreVoyageDTO>());
        for(PrixOffreVoyageDTO dto: request.getDatas() ) {
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
            //Mode
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
            //Categorie voyageur
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
            //OffreVoyage
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
            //Autres
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDescription().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(Utilities.isValidID(dto.getPrix())&&!dto.getPrix().equals(entityToSave.getPrix())){
                entityToSave.setPrix(dto.getPrix());
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
    public Response<PrixOffreVoyageDTO> forceDelete(Request<PrixOffreVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<PrixOffreVoyageDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<PrixOffreVoyageDTO> getByCriteria(Request<PrixOffreVoyageDTO> request, Locale locale) {
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
    public Response<PrixOffreVoyageDTO> getPrixTravelOfferByOffreVoyageDesignation(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<PrixOffreVoyageDTO> response = new Response<PrixOffreVoyageDTO>();
        List<PrixOffreVoyage> items = new ArrayList<PrixOffreVoyage>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
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
        items =prixOffreVoyageRepository.findAllByOffreVoyageDesignation(offreVoyageDesignation,false);
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