package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPeriodique;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.AbonnementPeriodiqueRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.AbonnementPeriodiqueTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Reference.Reference;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
public class AbonnementPeriodiqueBusiness implements IBasicBusiness<Request<AbonnementPeriodiqueDTO>, Response<AbonnementPeriodiqueDTO>> {


    private Response<AbonnementPeriodiqueDTO> response;

    @Autowired
    AbonnementPeriodiqueRepository abonnementPeriodiqueRepository;
    @Autowired
    private FunctionalError functionalError;
    @Autowired
    CompagnieTransportRepository compagnieTransportRepository;
    @Autowired
    ReferenceRepository periodiciteAbonnementRepository;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public AbonnementPeriodiqueBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<AbonnementPeriodiqueDTO> create(Request<AbonnementPeriodiqueDTO> request, Locale locale) throws ParseException {
        Response<AbonnementPeriodiqueDTO> response = new Response<AbonnementPeriodiqueDTO>();
        List<AbonnementPeriodique> items = new ArrayList<AbonnementPeriodique>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(AbonnementPeriodiqueDTO itemDto : request.getDatas() ){
            //Chech If CompagnieTransport exists
            CompagnieTransport exitingCompagnieTransport=null;
            exitingCompagnieTransport=compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(), false);
            if (exitingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("Compagnie de transport ayant la raison sociale"+itemDto.getCompagnieTransportRaisonSociale()+" n'existe pas", locale)) ;
                response.setHasError(true);
                return response;
            }
            //Check if periodiciteAbonnement exists
            Reference existingPeriodiciteAbonnement = null;
            existingPeriodiciteAbonnement = periodiciteAbonnementRepository.findByDesignation(itemDto.getPeriodiciteAbonnementDesignation(),false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("La periodicite abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            AbonnementPeriodique entityToSave = AbonnementPeriodiqueTransformer.INSTANCE.toEntity(itemDto,exitingCompagnieTransport,existingPeriodiciteAbonnement);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            AbonnementPeriodique entitySaved=null;
            entitySaved=abonnementPeriodiqueRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<AbonnementPeriodiqueDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? AbonnementPeriodiqueTransformer.INSTANCE.toLiteDtos(items)
                                    : AbonnementPeriodiqueTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<AbonnementPeriodiqueDTO> update(Request<AbonnementPeriodiqueDTO> request, Locale locale) throws ParseException {
        Response<AbonnementPeriodiqueDTO> response = new Response<AbonnementPeriodiqueDTO>();
        List<AbonnementPeriodique> items = new ArrayList<AbonnementPeriodique>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<AbonnementPeriodiqueDTO>itemsDtos = Collections.synchronizedList(new ArrayList<AbonnementPeriodiqueDTO>());
        for(AbonnementPeriodiqueDTO dto: request.getDatas() ) {
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
        for(AbonnementPeriodiqueDTO dto: itemsDtos) {
            AbonnementPeriodique entityToSave = abonnementPeriodiqueRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'abonnement suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            String periodiciteAbonnementDesignation=entityToSave.getPeriodiciteAbonnement()!=null&&entityToSave.getPeriodiciteAbonnement().getDesignation()!=null
                                 ?  entityToSave.getPeriodiciteAbonnement().getDesignation()
                                 :  null;
            if (Utilities.isNotBlank(dto.getPeriodiciteAbonnementDesignation()) && !dto.getPeriodiciteAbonnementDesignation().equals(periodiciteAbonnementDesignation)) {
                Reference existingPeriodiciteAbonnement = periodiciteAbonnementRepository.findByDesignation(dto.getPeriodiciteAbonnementDesignation(), false);
                if (existingPeriodiciteAbonnement == null) {
                    response.setStatus(functionalError.DATA_EXIST("AbonnementPeriodique -> " + dto.getPeriodiciteAbonnementDesignation()+" n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setPeriodiciteAbonnement(existingPeriodiciteAbonnement);
            }
            if(dto.getDateDebutAbonnement()!=null){
                Date dateDebutAbonnement=Utilities.convertStringToDate(dto.getDateDebutAbonnement());
                if (dateDebutAbonnement.compareTo(entityToSave.getDateDebutAbonnement()) != 0) {
                    entityToSave.setDateDebutAbonnement(dateDebutAbonnement);
                }
            }
            if(dto.getDateFinAbonnement()!=null){
                Date dateFinAbonnement=Utilities.convertStringToDate(dto.getDateFinAbonnement());
                if (dateFinAbonnement.compareTo(entityToSave.getDateFinAbonnement()) != 0) {
                    entityToSave.setDateFinAbonnement(dateFinAbonnement);
                }
            }
            if(Utilities.isValidID(dto.getRedevance()) && (dto.getRedevance()!=entityToSave.getRedevance())){
                entityToSave.setRedevance(dto.getRedevance());
            }
            if(Utilities.isValidID(dto.getRedevancePublicite()) && (dto.getRedevancePublicite()!=entityToSave.getRedevancePublicite())){
                entityToSave.setRedevancePublicite(dto.getRedevancePublicite());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            AbonnementPeriodique entityUpdated=null;
            entityUpdated=abonnementPeriodiqueRepository.save(entityToSave);
            items.add(entityUpdated);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification, ",locale));
            response.setHasError(true);
            return response;
        }
        List<AbonnementPeriodiqueDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? AbonnementPeriodiqueTransformer.INSTANCE.toLiteDtos(items)
                                : AbonnementPeriodiqueTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Jour semaine-----");
        return response;
    }

    @Override
    public Response<AbonnementPeriodiqueDTO> delete(Request<AbonnementPeriodiqueDTO> request, Locale locale) {

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
    public Response<AbonnementPeriodiqueDTO> forceDelete(Request<AbonnementPeriodiqueDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<AbonnementPeriodiqueDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<AbonnementPeriodiqueDTO> getByCriteria(Request<AbonnementPeriodiqueDTO> request, Locale locale) {
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
