package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.repository.offreVoyage.JourSemaineRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProgrammeRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.ProgrammeDTO;
import com.africanb.africanb.helper.transformer.offrreVoyage.ProgrammeTransformer;
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
 * @Author ALZOUMA MOUSSA MAHAAMADOU
 */
@Log
@Component
public class ProgrammeBusiness implements IBasicBusiness<Request<ProgrammeDTO>, Response<ProgrammeDTO>> {

    private Response<ProgrammeDTO> response;
    @Autowired
    private JourSemaineRepository jourSemaineRepository;
    @Autowired
    private ProgrammeRepository programmeRepository;
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

    public ProgrammeBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ProgrammeDTO> create(Request<ProgrammeDTO> request, Locale locale) throws ParseException {
        Response<ProgrammeDTO> response = new Response<ProgrammeDTO>();

        List<Programme> items = new ArrayList<Programme>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }

        List<ProgrammeDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<ProgrammeDTO>());
        for(ProgrammeDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            //fieldsToVerify.put("dateArrivee", dto.getDateArrivee());
            fieldsToVerify.put("nombrePlaceDisponible", dto.getNombrePlaceDisponible());
            fieldsToVerify.put("heureArrivee", dto.getHeureArrivee());
            fieldsToVerify.put("heureDepart", dto.getHeureDepart());
            fieldsToVerify.put("jourSemaineDesignation", dto.getJourSemaineDesignation());
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

        for(ProgrammeDTO itemDto : itemsDtos){
            Programme existingProgramme = null;
            existingProgramme = programmeRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingProgramme != null) {
                response.setStatus(functionalError.DATA_EXIST("Programme ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            JourSemaine existingJourSemaine = null;
            existingJourSemaine= jourSemaineRepository.findByDesignation(itemDto.getJourSemaineDesignation(),false);
            if (existingJourSemaine == null) {
                response.setStatus(functionalError.DATA_EXIST("Jour semaine ayant  pour identifiant -> " + itemDto.getJourSemaineDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Programme entityToSave = ProgrammeTransformer.INSTANCE.toEntity(itemDto,existingJourSemaine);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }

        List<Programme> itemsSaved = null;
        itemsSaved = programmeRepository.saveAll((Iterable<Programme>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }

        List<ProgrammeDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ProgrammeTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : ProgrammeTransformer.INSTANCE.toDtos(itemsSaved);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ProgrammeDTO> update(Request<ProgrammeDTO> request, Locale locale) throws ParseException {
        Response<ProgrammeDTO> response = new Response<ProgrammeDTO>();
        List<Programme> items = new ArrayList<Programme>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ProgrammeDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<ProgrammeDTO>());
        for(ProgrammeDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de la designation'" + dto.getDesignation() + "' pour les programmes", locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(ProgrammeDTO dto: itemsDtos) {
            Programme entityToSave = programmeRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le programme ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                Programme existingProgramme = programmeRepository.findByDesignation(dto.getDesignation(), false);
                if (existingProgramme != null && !existingProgramme.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Programme -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String jourSemaineDesignation=entityToSave.getJourSemaine()!=null&&entityToSave.getJourSemaine().getDesignation()!=null
                                       ?entityToSave.getJourSemaine().getDesignation()
                                       :null;
            if(jourSemaineDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Jour semaine n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Mode
            JourSemaine existingJourSemaine = jourSemaineRepository.findByDesignation(jourSemaineDesignation,false);
            if (existingJourSemaine == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le mode du prix de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getJourSemaineDesignation()) && !dto.getJourSemaineDesignation().equals(existingJourSemaine.getDesignation())) {
                JourSemaine jourSemaineToSave = jourSemaineRepository.findByDesignation(dto.getJourSemaineDesignation(), false);
                if (jourSemaineToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Jour semaine->" + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setJourSemaine(jourSemaineToSave);
            }
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDesignation().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(Utilities.isNotBlank(dto.getDateDepart()) && (!dto.getDateDepart().equalsIgnoreCase(Utilities.convertDateToString(entityToSave.getDateDepart())))){
                entityToSave.setDateDepart(Utilities.convertStringToDate(dto.getDateDepart()));
            }
            if(Utilities.isNotBlank(dto.getDateArrivee()) &&  !dto.getDateArrivee().equalsIgnoreCase(Utilities.convertDateToString(entityToSave.getDateArrivee()))){
                entityToSave.setDateArrivee(Utilities.convertStringToDate(dto.getDateArrivee()));
            }
            if(Utilities.isNotBlank(dto.getHeureArrivee()) && !dto.getHeureArrivee().equalsIgnoreCase(entityToSave.getHeureArrivee())){
                entityToSave.setHeureArrivee(dto.getHeureArrivee());
            }
            if(Utilities.isNotBlank(dto.getHeureDepart()) && !dto.getHeureDepart().equalsIgnoreCase(entityToSave.getHeureDepart())){
                entityToSave.setHeureDepart(dto.getHeureDepart());
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
        List<ProgrammeDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? ProgrammeTransformer.INSTANCE.toLiteDtos(items)
                                : ProgrammeTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Programme-----");
        return response;
    }

    @Override
    public Response<ProgrammeDTO> delete(Request<ProgrammeDTO> request, Locale locale) {

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
    public Response<ProgrammeDTO> forceDelete(Request<ProgrammeDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ProgrammeDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ProgrammeDTO> getByCriteria(Request<ProgrammeDTO> request, Locale locale) {
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
