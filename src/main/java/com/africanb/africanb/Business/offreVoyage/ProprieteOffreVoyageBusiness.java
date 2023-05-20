package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.ProprieteOffreVoyage;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProprieteOffreVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.ProprieteOffreVoyageDTO;
import com.africanb.africanb.helper.transformer.offrreVoyage.ProprieteOffreVoyageTransformer;
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
public class ProprieteOffreVoyageBusiness implements IBasicBusiness<Request<ProprieteOffreVoyageDTO>, Response<ProprieteOffreVoyageDTO>> {


    private Response<ProprieteOffreVoyageDTO> response;
    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private ProprieteOffreVoyageRepository proprieteOffreVoyageRepository;
    @Autowired
    private ProgrammeBusiness programmeBusiness;
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

    public ProprieteOffreVoyageBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> create(Request<ProprieteOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ProprieteOffreVoyageDTO> response = new Response<ProprieteOffreVoyageDTO>();
        List<ProprieteOffreVoyage> items = new ArrayList<ProprieteOffreVoyage>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<ProprieteOffreVoyageDTO>());
        for(ProprieteOffreVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("typeProprieteOffreVoyageDesignationDesignation", dto.getTypeProprieteOffreVoyageDesignation());
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
        for(ProprieteOffreVoyageDTO itemDto : itemsDtos){
            ProprieteOffreVoyage existingProprieteOffreVoyage = null;
            existingProprieteOffreVoyage = proprieteOffreVoyageRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingProprieteOffreVoyage != null) {
                response.setStatus(functionalError.DATA_EXIST("ProprieteOffreVoyage ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeProprieteOffreVoyage = null;
            existingTypeProprieteOffreVoyage= referenceRepository.findByDesignation(itemDto.getTypeProprieteOffreVoyageDesignation(),false);
            if (existingTypeProprieteOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de propriété de l'offre de voyage ayant  pour identifiant -> " + itemDto.getTypeProprieteOffreVoyageDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ProprieteOffreVoyage entityToSave = ProprieteOffreVoyageTransformer.INSTANCE.toEntity(itemDto,existingTypeProprieteOffreVoyage);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            ProprieteOffreVoyage entitySaved=null;
            entitySaved=proprieteOffreVoyageRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ProprieteOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                                    : ProprieteOffreVoyageTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> update(Request<ProprieteOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ProprieteOffreVoyageDTO> response = new Response<ProprieteOffreVoyageDTO>();
        List<ProprieteOffreVoyage> items = new ArrayList<ProprieteOffreVoyage>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<ProprieteOffreVoyageDTO>());
        for(ProprieteOffreVoyageDTO dto: request.getDatas() ) {
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
        for(ProprieteOffreVoyageDTO dto: itemsDtos) {
            ProprieteOffreVoyage entityToSave = proprieteOffreVoyageRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La propriété de l'offre de voyage ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                ProprieteOffreVoyage existingProprieteOffrrVoyage = proprieteOffreVoyageRepository.findByDesignation(dto.getDesignation(), false);
                if (existingProprieteOffrrVoyage != null && !existingProprieteOffrrVoyage.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("ProprieteOffreVoyage -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            //JourSemaineReference
            String typeProprieteOffreVoyage=entityToSave.getTypeProprieteOffreVoyage()!=null&&entityToSave.getTypeProprieteOffreVoyage().getDesignation()!=null
                                       ?entityToSave.getTypeProprieteOffreVoyage().getDesignation()
                                       :null;
            if(typeProprieteOffreVoyage==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Aucun type défini pour la propriété", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeProprieteOffreVoyage = referenceRepository.findByDesignation(typeProprieteOffreVoyage,false);
            if (existingTypeProprieteOffreVoyage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le type de propriété de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getTypeProprieteOffreVoyageDesignation()) && !dto.getTypeProprieteOffreVoyageDesignation().equals(existingTypeProprieteOffreVoyage.getDesignation())) {
                Reference typeProprieteOffreVoyageToSave = referenceRepository.findByDesignation(dto.getTypeProprieteOffreVoyageDesignation(), false);
                if (typeProprieteOffreVoyageToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le nouveau type ayant l'identifiant " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setTypeProprieteOffreVoyage(typeProprieteOffreVoyageToSave);
            }
            //Autres
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDesignation().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(dto.getEstObligatoire() != entityToSave.getEstObligatoire()){
                entityToSave.setEstObligatoire(dto.getEstObligatoire());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            ProprieteOffreVoyage entityUpdated=null;
            entityUpdated=proprieteOffreVoyageRepository.save(entityToSave);
            if (entityUpdated == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification", locale));
                response.setHasError(true);
                return response;
            }
            items.add(entityUpdated);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification, ",locale));
            response.setHasError(true);
            return response;
        }
        List<ProprieteOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? ProprieteOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                                : ProprieteOffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Jour semaine-----");
        return response;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> delete(Request<ProprieteOffreVoyageDTO> request, Locale locale) {

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
    public Response<ProprieteOffreVoyageDTO> forceDelete(Request<ProprieteOffreVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ProprieteOffreVoyageDTO> getByCriteria(Request<ProprieteOffreVoyageDTO> request, Locale locale) {
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
    public  Response<ProprieteOffreVoyageDTO> getAllProprieteOffreVoyage(Request<ProprieteOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ProprieteOffreVoyageDTO> response = new Response<ProprieteOffreVoyageDTO>();
        List<ProprieteOffreVoyage> items = new ArrayList<ProprieteOffreVoyage>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        items=proprieteOffreVoyageRepository.findAll(false);
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune propriété offre voyage n'est définie",locale));
            response.setHasError(true);
            return response;
        }

        List<ProprieteOffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ProprieteOffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                : ProprieteOffreVoyageTransformer.INSTANCE.toDtos(items);

       // response.setCount(count);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

}
