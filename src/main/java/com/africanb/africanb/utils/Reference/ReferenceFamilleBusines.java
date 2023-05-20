package com.africanb.africanb.utils.Reference;

import com.africanb.africanb.dao.repository.Reference.ReferenceFamilleRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Alzouma Moussa Mahamadou
 */
@Log
@Component
public class ReferenceFamilleBusines implements IBasicBusiness<Request<ReferenceFamilleDTO>, Response<ReferenceFamilleDTO>> {

    private Response<ReferenceFamilleDTO> response;
    @Autowired
    private ReferenceFamilleRepository referenceFamilleRepository;
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

    public ReferenceFamilleBusines() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ReferenceFamilleDTO> create(Request<ReferenceFamilleDTO> request, Locale locale) throws ParseException {
        log.info("========Debut de traitement========"); //TODO A effacer
        Response<ReferenceFamilleDTO> response = new Response<ReferenceFamilleDTO>();
        List<ReferenceFamille> items = new ArrayList<ReferenceFamille>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceFamilleDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<ReferenceFamilleDTO>());
        for(ReferenceFamilleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                //TODO Mise à jour des messages d'erreur
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de la designation'" + dto.getDesignation() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(ReferenceFamilleDTO itemDto : itemsDtos){
            ReferenceFamille existingEntity = null;
            existingEntity = referenceFamilleRepository.findByDesignation(itemDto.getDesignation(),false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("ReferenceFamille ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            ReferenceFamille entityToSave = ReferenceFamilleTransformer.INSTANCE.toEntity(itemDto);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }
        List<ReferenceFamille> itemsSaved = null;
        itemsSaved = referenceFamilleRepository.saveAll((Iterable<ReferenceFamille>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceFamilleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ReferenceFamilleTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : ReferenceFamilleTransformer.INSTANCE.toDtos(itemsSaved);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ReferenceFamilleDTO> update(Request<ReferenceFamilleDTO> request, Locale locale) throws ParseException {

        Response<ReferenceFamilleDTO> response = new Response<ReferenceFamilleDTO>();
        List<ReferenceFamille> items = new ArrayList<ReferenceFamille>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste de données est vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<ReferenceFamilleDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<ReferenceFamilleDTO>());
        for(ReferenceFamilleDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                //TODO Mise à jour des messages d'erreur
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de la designation'" + dto.getDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }

        for(ReferenceFamilleDTO dto: itemsDtos) {
            ReferenceFamille entityToSave = referenceFamilleRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'agence ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                ReferenceFamille existingEntity = referenceFamilleRepository.findByDesignation(dto.getDesignation(), false);
                //Verification de l'identifiant
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("ReferenceFamille -> " + dto.getDesignation(), locale));
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
        List<ReferenceFamilleDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? ReferenceFamilleTransformer.INSTANCE.toLiteDtos(items)
                                : ReferenceFamilleTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update agence-----");
        return response;
    }

    @Override
    public Response<ReferenceFamilleDTO> delete(Request<ReferenceFamilleDTO> request, Locale locale) {

        /* log.info("----begin delete agence-----");
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
    public Response<ReferenceFamilleDTO> forceDelete(Request<ReferenceFamilleDTO> request, Locale locale)  {
        return null ;
    }

    @Override
    public Response<ReferenceFamilleDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ReferenceFamilleDTO> getByCriteria(Request<ReferenceFamilleDTO> request, Locale locale) {

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
