package com.africanb.africanb.Business.offreVoyage;


import ch.qos.logback.core.util.StringCollectionUtil;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.offreVoyage.*;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProprieteOffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ValeurCaracteristiqueOffreVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.*;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.offrreVoyage.OffreVoyageTransformer;
import com.africanb.africanb.helper.transformer.offrreVoyage.ValeurCaracteristiqueOffreVoyageBooleanTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Constants.ProjectConstants;
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
public class ValeurCaracteristiqueOffreVoyageBusiness implements IBasicBusiness<Request<ValeurCaracteristiqueOffreVoyageDTO>, Response<ValeurCaracteristiqueOffreVoyageDTO>>,ValeurCaracteristiqueOffreVoyageInterface {


    private Response<ValeurCaracteristiqueOffreVoyageDTO> response;

    @Autowired
    private FunctionalError functionalError;
    @Autowired
    private OffreVoyageRepository offreVoyageRepository;
    @Autowired
    private ProprieteOffreVoyageRepository proprieteOffreVoyageRepository;
    @Autowired
    private ValeurCaracteristiqueOffreVoyageRepository valeurCaracteristiqueOffreVoyageRepository;
    @Autowired
    private ValeurCaracteristiqueOffreVoyageBooleanBusiness valeurCaracteristiqueOffreVoyageBooleanBusiness;
    @Autowired
    private ValeurCaracteristiqueOffreVoyageLongBusiness valeurCaracteristiqueOffreVoyageLongBusiness;
    @Autowired
    private ValeurCaracteristiqueOffreVoyageStringBusiness valeurCaracteristiqueOffreVoyageStringBusiness;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ValeurCaracteristiqueOffreVoyageBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> create(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = new Response<ValeurCaracteristiqueOffreVoyageDTO>();
        List<ValeurCaracteristiqueOffreVoyage> items = new ArrayList<ValeurCaracteristiqueOffreVoyage>();
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDto= new ArrayList<ValeurCaracteristiqueOffreVoyageDTO>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ValeurCaracteristiqueOffreVoyageDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<ValeurCaracteristiqueOffreVoyageDTO>());
        for(ValeurCaracteristiqueOffreVoyageDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
                fieldsToVerify.put("designation", dto.getDesignation());
                fieldsToVerify.put("offfreVoyageDesignation", dto.getOffreVoyageDesignation());
                fieldsToVerify.put("proprieteOffreVoyageDesignation", dto.getProprieteOffreVoyageDesignation());
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
        }
        for(ValeurCaracteristiqueOffreVoyageDTO itemDto : itemsDtos){
            //Get Offre Voyage
            OffreVoyage existingOffreVoyage = null;
            existingOffreVoyage = offreVoyageRepository.findByDesignation(itemDto.getOffreVoyageDesignation(),false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("Offre de voyage n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Get Propriete Offre Voyage
            ProprieteOffreVoyage existingProprieteOffreVoyage = null;
            existingProprieteOffreVoyage = proprieteOffreVoyageRepository.findByDesignation(itemDto.getProprieteOffreVoyageDesignation(),false);
            if (existingProprieteOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("ProprieteOffre de voyage n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if(existingProprieteOffreVoyage.getEstObligatoire() == true){
                if(itemDto.getValeurTexte()==null){
                    response.setStatus(functionalError.DATA_EXIST("La propriete offre de voyage " +existingProprieteOffreVoyage.getDesignation()+ ", est obligatoire.Sa valeur doit être définie", locale));
                    response.setHasError(true);
                    return response;
                }
            }
            itemDto.setTypeProprieteOffreVoyageDesignation(existingProprieteOffreVoyage.getTypeProprieteOffreVoyage().getDesignation());
            itemDto=Utilities.transformerValeurCaracteristiqueOffreVoyagEnLaClasseFilleCorrespondateEnFonctionDuTypeDeLaPropriete(itemDto);
            ValeurCaracteristiqueOffreVoyageDTO entitySaved=null;
            entitySaved=saveValeurCaracteristiqueOffreVoyageDTOEnFonctionDuTypeDeLaPropriete(itemDto,locale);
            itemsDto.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(itemsDto)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> update(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) throws ParseException {
       /* Response<JourSemaineDTO> response = new Response<JourSemaineDTO>();
        List<JourSemaine> items = new ArrayList<JourSemaine>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<JourSemaineDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<JourSemaineDTO>());
        for(JourSemaineDTO dto: request.getDatas() ) {
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
        for(JourSemaineDTO dto: itemsDtos) {
            JourSemaine entityToSave = Repository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le jour de la semaine ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                JourSemaine existingJourSemaine = Repository.findByDesignation(dto.getDesignation(), false);
                if (existingJourSemaine != null && !existingJourSemaine.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Joursemaine -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            //JourSemaineReference
            String jourSemaineEffectif=entityToSave.getJourSemaine()!=null&&entityToSave.getJourSemaine().getDesignation()!=null
                                       ?entityToSave.getJourSemaine().getDesignation()
                                       :null;
            if(jourSemaineEffectif==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Aucun jour effectif de la semaine défini", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingJourSemaineEffectif = referenceRepository.findByDesignation(jourSemaineEffectif,false);
            if (existingJourSemaineEffectif == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le jour de la semaine -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getJourSemaineDesignation()) && !dto.getJourSemaineDesignation().equals(existingJourSemaineEffectif.getDesignation())) {
                Reference jourSemaineEffectifToSave = referenceRepository.findByDesignation(dto.getJourSemaineDesignation(), false);
                if (jourSemaineEffectifToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le nouveau jour de la semaine n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setJourSemaine(jourSemaineEffectifToSave);
            }
            //OffreVoyage
            String offreVoyageDesignation=entityToSave.getOffreVoyage()!=null&&entityToSave.getOffreVoyage().getDesignation()!=null
                    ?entityToSave.getOffreVoyage().getDesignation()
                    :null;
            if(offreVoyageDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage n'existe", locale));
                response.setHasError(true);
                return response;
            }
            OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(offreVoyageDesignation,false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (existingOffreVoyage.getIsActif()!=null && existingOffreVoyage.getIsActif() == true) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Desactivez l'offre de voyage avant de proceder au changement du prix", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getOffreVoyageDesignation()) && !dto.getOffreVoyageDesignation().equals(existingOffreVoyage.getDesignation())) {
                OffreVoyage offreVoyageToSave = offreVoyageRepository.findByDesignation(dto.getOffreVoyageDesignation(), false);
                if (offreVoyageToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle offre de voyage-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setOffreVoyage(offreVoyageToSave);
            }
            //Autres
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDesignation().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            JourSemaine entityUpdated=null;
            entityUpdated=Repository.save(entityToSave);
            if (entityUpdated == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification", locale));
                response.setHasError(true);
                return response;
            }
            //Check if programmeList
            if(!CollectionUtils.isEmpty(dto.getProgrammeDTOList())){
                Request<ProgrammeDTO> subRequest = new Request<ProgrammeDTO>();
                subRequest.setDatas(dto.getProgrammeDTOList());
                //subRequest.setUser(request.getUser());
                //Initialisation de l'offre de voyage
                for(ProgrammeDTO programmeDTO: dto.getProgrammeDTOList()){
                    programmeDTO.setJourSemaineDesignation(entityUpdated.getDesignation());
                }
                Response<ProgrammeDTO> subResponse = programmeBusiness.update(subRequest, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            items.add(entityUpdated);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification, ",locale));
            response.setHasError(true);
            return response;
        }
        List<JourSemaineDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? JourSemaineTransformer.INSTANCE.toLiteDtos(items)
                                : JourSemaineTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Jour semaine-----");
        return response;*/
        return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> delete(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) {

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
    public Response<ValeurCaracteristiqueOffreVoyageDTO> forceDelete(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getByCriteria(Request<ValeurCaracteristiqueOffreVoyageDTO> request, Locale locale) {
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
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getAllValeurCaracteristiqueOffreVoyageByOffreVoyageDesignation(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = new Response<ValeurCaracteristiqueOffreVoyageDTO>();
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDto= new ArrayList<ValeurCaracteristiqueOffreVoyageDTO>();
        List<ValeurCaracteristiqueOffreVoyage> items = new ArrayList<ValeurCaracteristiqueOffreVoyage>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("offreVoyageDesignation", request.getData().getDesignation());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String offreVoyageDesignation=request.getData().getDesignation();
        OffreVoyage existingOffreVoyage = null;
        existingOffreVoyage = offreVoyageRepository.findByDesignation(offreVoyageDesignation,false);
        if (existingOffreVoyage == null) {
            response.setStatus(functionalError.DATA_EXIST("Offre de voyage n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        items= (List<ValeurCaracteristiqueOffreVoyage>) valeurCaracteristiqueOffreVoyageRepository.findAllByOffreVoyageDesignation(offreVoyageDesignation,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie ne dipose d'aucune offre de voyage", locale));
            response.setHasError(true);
            return response;
        }
        itemsDto=convertValeurCaracteristiqueOffreVoyagesFilleToValeurCaracteristiqueOffreVoyageDTO(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update l'offre de voyage-----");
        return response;
    }

    @Override
    public  List<ValeurCaracteristiqueOffreVoyageDTO>convertValeurCaracteristiqueOffreVoyagesFilleToValeurCaracteristiqueOffreVoyageDTO(List<ValeurCaracteristiqueOffreVoyage> valeurCaracteristiqueOffreVoyageList) {
        List<ValeurCaracteristiqueOffreVoyageDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ValeurCaracteristiqueOffreVoyageDTO>());

        for(ValeurCaracteristiqueOffreVoyage valeurCaracteristiqueOffreVoyage:valeurCaracteristiqueOffreVoyageList) {
         if(valeurCaracteristiqueOffreVoyage instanceof  ValeurCaracteristiqueOffreVoyageBoolean){
            ValeurCaracteristiqueOffreVoyageBoolean itemBoolean = new ValeurCaracteristiqueOffreVoyageBoolean();
            itemBoolean= (ValeurCaracteristiqueOffreVoyageBoolean) valeurCaracteristiqueOffreVoyage;
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( itemBoolean.getId());
            rtn.setDesignation( itemBoolean.getDesignation());
            rtn.setDescription( itemBoolean.getDescription());
            rtn.setValeurTexte(itemBoolean.getValeur().toString());
            rtn.setDeletedAt( itemBoolean.getDeletedAt()==null?null:itemBoolean.getDeletedAt().toString());
            rtn.setUpdatedAt( itemBoolean.getUpdatedAt()==null?null:itemBoolean.getUpdatedAt().toString());
            rtn.setCreatedAt( itemBoolean.getCreatedAt()==null?null:itemBoolean.getCreatedAt().toString());
            rtn.setCreatedBy( itemBoolean.getCreatedBy());
            rtn.setIsDeleted( itemBoolean.getIsDeleted());
            rtn.setDeletedBy( itemBoolean.getDeletedBy());
            rtn.setUpdatedBy( itemBoolean.getUpdatedBy());
            rtn.setOffreVoyageDesignation(itemBoolean.getOffreVoyage().getDesignation());
            rtn.setProprieteOffreVoyageDesignation(itemBoolean.getProprieteOffreVoyage().getDesignation());

            itemsDTO.add(rtn);
        }
        else if(valeurCaracteristiqueOffreVoyage instanceof ValeurCaracteristiqueOffreVoyageString){
            ValeurCaracteristiqueOffreVoyageString itemString = new ValeurCaracteristiqueOffreVoyageString();
            itemString= (ValeurCaracteristiqueOffreVoyageString) valeurCaracteristiqueOffreVoyage;
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( itemString.getId());
            rtn.setDesignation( itemString.getDesignation());
            rtn.setDescription( itemString.getDescription());
            rtn.setValeurTexte(itemString.getValeur());
            rtn.setDeletedAt( itemString.getDeletedAt()==null?null:itemString.getDeletedAt().toString());
            rtn.setUpdatedAt( itemString.getUpdatedAt()==null?null:itemString.getUpdatedAt().toString());
            rtn.setCreatedAt( itemString.getCreatedAt()==null?null:itemString.getCreatedAt().toString());
            rtn.setCreatedBy( itemString.getCreatedBy());
            rtn.setIsDeleted( itemString.getIsDeleted());
            rtn.setDeletedBy( itemString.getDeletedBy());
            rtn.setUpdatedBy( itemString.getUpdatedBy());
            rtn.setOffreVoyageDesignation(itemString.getOffreVoyage().getDesignation());
            rtn.setProprieteOffreVoyageDesignation(itemString.getProprieteOffreVoyage().getDesignation());

            itemsDTO.add(rtn);
        }
        else if(valeurCaracteristiqueOffreVoyage instanceof  ValeurCaracteristiqueOffreVoyageLong){
            ValeurCaracteristiqueOffreVoyageLong itemLong = new ValeurCaracteristiqueOffreVoyageLong();
            itemLong= (ValeurCaracteristiqueOffreVoyageLong) valeurCaracteristiqueOffreVoyage;
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( itemLong.getId());
            rtn.setDesignation( itemLong.getDesignation());
            rtn.setDescription( itemLong.getDescription());
            rtn.setValeurTexte(itemLong.getValeur().toString());
            rtn.setDeletedAt( itemLong.getDeletedAt()==null?null:itemLong.getDeletedAt().toString());
            rtn.setUpdatedAt( itemLong.getUpdatedAt()==null?null:itemLong.getDeletedAt().toString());
            rtn.setCreatedAt( itemLong.getCreatedAt()==null?null:itemLong.getDeletedAt().toString());
            rtn.setCreatedBy( itemLong.getCreatedBy());
            rtn.setIsDeleted( itemLong.getIsDeleted());
            rtn.setDeletedBy( itemLong.getDeletedBy());
            rtn.setUpdatedBy( itemLong.getUpdatedBy());
            rtn.setOffreVoyageDesignation(itemLong.getOffreVoyage().getDesignation());
            rtn.setProprieteOffreVoyageDesignation(itemLong.getProprieteOffreVoyage().getDesignation());

            itemsDTO.add(rtn);
        }
        else{

        }
      }
        return itemsDTO;
    }

    @Override
    public ValeurCaracteristiqueOffreVoyageDTO saveValeurCaracteristiqueOffreVoyageDTOEnFonctionDuTypeDeLaPropriete(ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDTO,Locale locale) throws ParseException {

        if(valeurCaracteristiqueOffreVoyageDTO instanceof  ValeurCaracteristiqueOffreVoyageLongDTO){
            Request<ValeurCaracteristiqueOffreVoyageLongDTO> subRequest = new Request<ValeurCaracteristiqueOffreVoyageLongDTO>();
            List<ValeurCaracteristiqueOffreVoyageLongDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ValeurCaracteristiqueOffreVoyageLongDTO>());
            ValeurCaracteristiqueOffreVoyageLongDTO valeurLongDTO = (ValeurCaracteristiqueOffreVoyageLongDTO) valeurCaracteristiqueOffreVoyageDTO;
            //Conversion
            Long valeur = Utilities.convertStringToLong(valeurLongDTO.getValeurTexte());
            valeurLongDTO.setValeur(valeur);
            itemsDTO.add(valeurLongDTO);
            subRequest.setDatas( itemsDTO);
            //subRequest.setUser(request.getUser());
            Response<ValeurCaracteristiqueOffreVoyageLongDTO> subResponse = valeurCaracteristiqueOffreVoyageLongBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            rtn.setValeurTexte( subResponse.getItems().get(0).getValeur().toString());
            rtn.setDeletedAt( subResponse.getItems().get(0).getDeletedAt());
            rtn.setUpdatedAt( subResponse.getItems().get(0).getUpdatedAt());
            rtn.setCreatedAt( subResponse.getItems().get(0).getCreatedAt());
            rtn.setCreatedBy( subResponse.getItems().get(0).getCreatedBy());
            rtn.setIsDeleted( subResponse.getItems().get(0).getIsDeleted());
            rtn.setDeletedBy( subResponse.getItems().get(0).getDeletedBy());
            rtn.setUpdatedBy( subResponse.getItems().get(0).getUpdatedBy());
            rtn.setIsDeletedParam( subResponse.getItems().get(0).getIsDeletedParam());
            rtn.setUpdatedAtParam( subResponse.getItems().get(0).getUpdatedAtParam());
            rtn.setCreatedAtParam( subResponse.getItems().get(0).getCreatedAtParam());
            rtn.setCreatedByParam( subResponse.getItems().get(0).getCreatedByParam());
            rtn.setUpdatedByParam(subResponse.getItems().get(0).getUpdatedByParam());
            rtn.setOffreVoyageDesignation(subResponse.getItems().get(0).getOffreVoyageDesignation());
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            rtn.setProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getProprieteOffreVoyageDesignation());
            rtn.setTypeProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getTypeProprieteOffreVoyageDesignation());

            return rtn;
        }
        else if(valeurCaracteristiqueOffreVoyageDTO instanceof  ValeurCaracteristiqueOffreVoyageStringDTO){
            Request<ValeurCaracteristiqueOffreVoyageStringDTO> subRequest = new Request<ValeurCaracteristiqueOffreVoyageStringDTO>();
            List<ValeurCaracteristiqueOffreVoyageStringDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ValeurCaracteristiqueOffreVoyageStringDTO>());
            ValeurCaracteristiqueOffreVoyageStringDTO valeurStringDTO = (ValeurCaracteristiqueOffreVoyageStringDTO) valeurCaracteristiqueOffreVoyageDTO;
            //Conversion
            valeurStringDTO.setValeur(valeurStringDTO.getValeurTexte());
            itemsDTO.add(valeurStringDTO);
            subRequest.setDatas( itemsDTO);
            Response<ValeurCaracteristiqueOffreVoyageStringDTO> subResponse = valeurCaracteristiqueOffreVoyageStringBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            rtn.setValeurTexte( subResponse.getItems().get(0).getValeur().toString());
            rtn.setDeletedAt( subResponse.getItems().get(0).getDeletedAt());
            rtn.setUpdatedAt( subResponse.getItems().get(0).getUpdatedAt());
            rtn.setCreatedAt( subResponse.getItems().get(0).getCreatedAt());
            rtn.setCreatedBy( subResponse.getItems().get(0).getCreatedBy());
            rtn.setIsDeleted( subResponse.getItems().get(0).getIsDeleted());
            rtn.setDeletedBy( subResponse.getItems().get(0).getDeletedBy());
            rtn.setUpdatedBy( subResponse.getItems().get(0).getUpdatedBy());
            rtn.setIsDeletedParam( subResponse.getItems().get(0).getIsDeletedParam());
            rtn.setUpdatedAtParam( subResponse.getItems().get(0).getUpdatedAtParam());
            rtn.setCreatedAtParam( subResponse.getItems().get(0).getCreatedAtParam());
            rtn.setCreatedByParam( subResponse.getItems().get(0).getCreatedByParam());
            rtn.setUpdatedByParam(subResponse.getItems().get(0).getUpdatedByParam());
            rtn.setOffreVoyageDesignation(subResponse.getItems().get(0).getOffreVoyageDesignation());
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            rtn.setProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getProprieteOffreVoyageDesignation());
            rtn.setTypeProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getTypeProprieteOffreVoyageDesignation());

            return rtn;
        }
        else if(valeurCaracteristiqueOffreVoyageDTO instanceof  ValeurCaracteristiqueOffreVoyageBooleanDTO){
            Request<ValeurCaracteristiqueOffreVoyageBooleanDTO> subRequest = new Request<ValeurCaracteristiqueOffreVoyageBooleanDTO>();
            List<ValeurCaracteristiqueOffreVoyageBooleanDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ValeurCaracteristiqueOffreVoyageBooleanDTO>());
            ValeurCaracteristiqueOffreVoyageBooleanDTO valeurBooleanDTO = (ValeurCaracteristiqueOffreVoyageBooleanDTO) valeurCaracteristiqueOffreVoyageDTO;
            //Conversion
            Boolean valeur = Utilities.convertStringToBoolean(valeurBooleanDTO.getValeurTexte());
            valeurBooleanDTO.setValeur(valeur);
            itemsDTO.add(valeurBooleanDTO);
            subRequest.setDatas( itemsDTO);
            Response<ValeurCaracteristiqueOffreVoyageBooleanDTO> subResponse = valeurCaracteristiqueOffreVoyageBooleanBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ValeurCaracteristiqueOffreVoyageDTO();
            }
            ValeurCaracteristiqueOffreVoyageDTO rtn = new ValeurCaracteristiqueOffreVoyageDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            rtn.setValeurTexte( subResponse.getItems().get(0).getValeur().toString());
            rtn.setDeletedAt( subResponse.getItems().get(0).getDeletedAt());
            rtn.setUpdatedAt( subResponse.getItems().get(0).getUpdatedAt());
            rtn.setCreatedAt( subResponse.getItems().get(0).getCreatedAt());
            rtn.setCreatedBy( subResponse.getItems().get(0).getCreatedBy());
            rtn.setIsDeleted( subResponse.getItems().get(0).getIsDeleted());
            rtn.setDeletedBy( subResponse.getItems().get(0).getDeletedBy());
            rtn.setUpdatedBy( subResponse.getItems().get(0).getUpdatedBy());
            rtn.setIsDeletedParam( subResponse.getItems().get(0).getIsDeletedParam());
            rtn.setUpdatedAtParam( subResponse.getItems().get(0).getUpdatedAtParam());
            rtn.setCreatedAtParam( subResponse.getItems().get(0).getCreatedAtParam());
            rtn.setCreatedByParam( subResponse.getItems().get(0).getCreatedByParam());
            rtn.setUpdatedByParam(subResponse.getItems().get(0).getUpdatedByParam());
            rtn.setOffreVoyageDesignation(subResponse.getItems().get(0).getOffreVoyageDesignation());
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            rtn.setProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getProprieteOffreVoyageDesignation());
            rtn.setTypeProprieteOffreVoyageDesignation(subResponse.getItems().get(0).getTypeProprieteOffreVoyageDesignation());

            return rtn;
        }
        else{

        }
        return new ValeurCaracteristiqueOffreVoyageDTO();
    }
}
