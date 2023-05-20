package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.offreVoyage.JourSemaineRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProgrammeRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
import com.africanb.africanb.helper.dto.offreVoyage.JourSemaineDTO;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ProgrammeDTO;
import com.africanb.africanb.helper.transformer.compagnie.VilleTransformer;
import com.africanb.africanb.helper.transformer.offrreVoyage.JourSemaineTransformer;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.offrreVoyage.OffreVoyageTransformer;
import com.africanb.africanb.helper.transformer.offrreVoyage.ProgrammeTransformer;
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
public class JourSemaineBusiness implements IBasicBusiness<Request<JourSemaineDTO>, Response<JourSemaineDTO>> {


    private Response<JourSemaineDTO> response;
    @Autowired
    private ReferenceRepository referenceRepository;
    @Autowired
    private JourSemaineRepository jourSemaineRepository;
    @Autowired
    private ProgrammeBusiness programmeBusiness;
    @Autowired
    private OffreVoyageRepository offreVoyageRepository;
    @Autowired
    private ProgrammeRepository programmeRepository;
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

    public JourSemaineBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<JourSemaineDTO> create(Request<JourSemaineDTO> request, Locale locale) throws ParseException {
        Response<JourSemaineDTO> response = new Response<JourSemaineDTO>();
        List<JourSemaine> items = new ArrayList<JourSemaine>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<JourSemaineDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<JourSemaineDTO>());
        for(JourSemaineDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("offreVoyageDesignation", dto.getOffreVoyageDesignation());
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
        for(JourSemaineDTO itemDto : itemsDtos){
            JourSemaine existingJourSemaine = null;
            existingJourSemaine = jourSemaineRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingJourSemaine != null) {
                response.setStatus(functionalError.DATA_EXIST("JourSemaine ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
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
            Reference existingJourSemaineReference = null;
            existingJourSemaineReference= referenceRepository.findByDesignation(itemDto.getJourSemaineDesignation(),false);
            if (existingJourSemaineReference == null) {
                response.setStatus(functionalError.DATA_EXIST("Le  jour effectif de la ssemaine ayant  pour identifiant -> " + itemDto.getJourSemaineDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            JourSemaine entityToSave = JourSemaineTransformer.INSTANCE.toEntity(itemDto,existingJourSemaineReference,existingOffreVoyage);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            JourSemaine entitySaved=null;
            entitySaved=jourSemaineRepository.save(entityToSave);
            //Check if programmeList
            if(!CollectionUtils.isEmpty(itemDto.getProgrammeDTOList())){
                Request<ProgrammeDTO> subRequest = new Request<ProgrammeDTO>();
                subRequest.setDatas(itemDto.getProgrammeDTOList());
                //subRequest.setUser(request.getUser());
                //Initialisation de l'offre de voyage
                for(ProgrammeDTO programmeDTO: itemDto.getProgrammeDTOList()){
                    programmeDTO.setJourSemaineDesignation(entitySaved.getDesignation());
                }
                Response<ProgrammeDTO> subResponse = programmeBusiness.create(subRequest, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<JourSemaineDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? JourSemaineTransformer.INSTANCE.toLiteDtos(items)
                                    : JourSemaineTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<JourSemaineDTO> update(Request<JourSemaineDTO> request, Locale locale) throws ParseException {
        Response<JourSemaineDTO> response = new Response<JourSemaineDTO>();
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
            JourSemaine entityToSave = jourSemaineRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le jour de la semaine ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                JourSemaine existingJourSemaine = jourSemaineRepository.findByDesignation(dto.getDesignation(), false);
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
            entityUpdated=jourSemaineRepository.save(entityToSave);
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
        return response;
    }

    @Override
    public Response<JourSemaineDTO> delete(Request<JourSemaineDTO> request, Locale locale) {

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
    public Response<JourSemaineDTO> forceDelete(Request<JourSemaineDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<JourSemaineDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<JourSemaineDTO> getByCriteria(Request<JourSemaineDTO> request, Locale locale) {
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
    public Response<JourSemaineDTO> getJourSemaineByVoyageDesignation(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<JourSemaineDTO> response = new Response<JourSemaineDTO>();
        List<JourSemaine> items = Collections.synchronizedList(new ArrayList<JourSemaine>());
        List<JourSemaineDTO> itemsDTO = Collections.synchronizedList(new ArrayList<JourSemaineDTO>());
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("offrVoyageDesignation", request.getData().getDesignation());
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
        items =jourSemaineRepository.findAllByOffreVoyageDesignation(offreVoyageDesignation,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voayage n'est programmé sur aucun de jour de la semaine", locale));
            response.setHasError(true);
            return response;
        }
        for(JourSemaine jourSemaine: items){
            List<Programme> itemsProg = Collections.synchronizedList(new ArrayList<Programme>());
            JourSemaineDTO entityToDto = JourSemaineTransformer.INSTANCE.toDto(jourSemaine);
            itemsProg=programmeRepository.findByJourSemaine(entityToDto.getDesignation(),false);
            if(!CollectionUtils.isEmpty(itemsProg)){
                List<ProgrammeDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                        ? ProgrammeTransformer.INSTANCE.toLiteDtos(itemsProg)
                        : ProgrammeTransformer.INSTANCE.toDtos(itemsProg);
                entityToDto.setProgrammeDTOList(itemsDto);
            }
            itemsDTO.add(entityToDto);
        }
        response.setItems(itemsDTO);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end jour semaine-----");
        return response;
    }
}
