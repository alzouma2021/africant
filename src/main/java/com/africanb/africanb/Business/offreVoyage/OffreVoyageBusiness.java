package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.offreVoyage.JourSemaine;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.VilleRepository;
import com.africanb.africanb.dao.repository.offreVoyage.JourSemaineRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.PrixOffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProgrammeRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.*;
import com.africanb.africanb.helper.transformer.offrreVoyage.OffreVoyageTransformer;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Reference.Reference;
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
public class OffreVoyageBusiness implements IBasicBusiness<Request<OffreVoyageDTO>, Response<OffreVoyageDTO>> {

    private Response<OffreVoyageDTO> response;

    private final ReferenceRepository referenceRepository;
    private final VilleRepository villeRepository;
    private final ProgrammeRepository programmeRepository;
    private final JourSemaineRepository jourSemaineRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final OffreVoyageRepository offreVoyageRepository;
    private final PrixOffreVoyageRepository prixOffreVoyageRepository;
    private final PrixOffreVoyageBusiness prixOffreVoyageBusiness;
    private final ProgrammeBusiness programmeBusiness;
    private final VilleEscaleBusiness villeEscaleBusiness;
    private final JourSemaineBusiness jourSemaineBusinesse;
    private final ValeurCaracteristiqueOffreVoyageBusiness valeurCaracteristiqueOffreVoyageBusiness;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public OffreVoyageBusiness(ReferenceRepository referenceRepository, VilleRepository villeRepository, ProgrammeRepository programmeRepository, JourSemaineRepository jourSemaineRepository, CompagnieTransportRepository compagnieTransportRepository, OffreVoyageRepository offreVoyageRepository, PrixOffreVoyageRepository prixOffreVoyageRepository, PrixOffreVoyageBusiness prixOffreVoyageBusiness, ProgrammeBusiness programmeBusiness, VilleEscaleBusiness villeEscaleBusiness, JourSemaineBusiness jourSemaineBusinesse, ValeurCaracteristiqueOffreVoyageBusiness valeurCaracteristiqueOffreVoyageBusiness, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.referenceRepository = referenceRepository;
        this.villeRepository = villeRepository;
        this.programmeRepository = programmeRepository;
        this.jourSemaineRepository = jourSemaineRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.offreVoyageRepository = offreVoyageRepository;
        this.prixOffreVoyageRepository = prixOffreVoyageRepository;
        this.prixOffreVoyageBusiness = prixOffreVoyageBusiness;
        this.programmeBusiness = programmeBusiness;
        this.villeEscaleBusiness = villeEscaleBusiness;
        this.jourSemaineBusinesse = jourSemaineBusinesse;
        this.valeurCaracteristiqueOffreVoyageBusiness = valeurCaracteristiqueOffreVoyageBusiness;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<OffreVoyageDTO> create(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<OffreVoyageDTO> response = new Response<OffreVoyageDTO>();
        List<OffreVoyage> items = new ArrayList<OffreVoyage>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<OffreVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<OffreVoyageDTO>());
        for(OffreVoyageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("villeDepartDesignation", dto.getVilleDepartDesignation());
            fieldsToVerify.put("villeDestinationDesignation", dto.getVilleDestinationDesignation());
            fieldsToVerify.put("typeOffreVoyageDesignation", dto.getTypeOffreVoyageDesignation());
            fieldsToVerify.put("compagnieRaisonSociale", dto.getCompagnieTransportRaisonSociale());
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
        for(OffreVoyageDTO itemDto : itemsDtos){
            OffreVoyage existingOffreVoyage = null;
            existingOffreVoyage= offreVoyageRepository.findByDesignation(itemDto.getDesignation(),false);
            if (!Objects.isNull(existingOffreVoyage)) {
                response.setStatus(functionalError.DATA_EXIST("L'offre de voyage ayant  pour identifiant -> " + itemDto.getDesignation() +",existe", locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVilleDepart;
            existingVilleDepart= villeRepository.findByDesignation(itemDto.getVilleDepartDesignation(),false);
            if (Objects.isNull(existingVilleDepart)) {
                response.setStatus(functionalError.DATA_EXIST("La ville de départ indiquée n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVilleDestination;
            existingVilleDestination= villeRepository.findByDesignation(itemDto.getVilleDestinationDesignation(),false);
            if (Objects.isNull(existingVilleDestination)) {
                response.setStatus(functionalError.DATA_EXIST("La ville de destination indiquée n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieTransport existingCompagnieTransport;
            existingCompagnieTransport= compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (Objects.isNull(existingCompagnieTransport)) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (existingCompagnieTransport.getIsValidate() == null || !existingCompagnieTransport.getIsValidate()) {
                response.setStatus(functionalError.DATA_EXIST("Vous ne pouvez pas créer des offres de voyage.Car,votre compagnie n'est encore validée", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeOffreVoyage;
            existingTypeOffreVoyage= referenceRepository.findByDesignation(itemDto.getTypeOffreVoyageDesignation(),false);
            if (Objects.isNull(existingTypeOffreVoyage)) {
                response.setStatus(functionalError.DATA_EXIST("Le type offre de voyage ayant  pour identifiant -> " + itemDto.getDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }

            OffreVoyage entityToSave = OffreVoyageTransformer.INSTANCE.toEntity(itemDto,existingVilleDepart,existingVilleDestination,existingTypeOffreVoyage,existingCompagnieTransport);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());

            OffreVoyage entitySaved = offreVoyageRepository.save(entityToSave);
            if(Objects.isNull(entitySaved)){
                response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
                response.setHasError(true);
                return response;
            }

            if(!CollectionUtils.isEmpty(itemDto.getPrixOffreVoyageDTOList())){
                    Request<PrixOffreVoyageDTO> subRequest = new Request<>();
                    subRequest.setDatas(itemDto.getPrixOffreVoyageDTOList());
                    for(PrixOffreVoyageDTO prixOffreVoyageDTO: itemDto.getPrixOffreVoyageDTOList()){
                        prixOffreVoyageDTO.setOffreVoyageDesignation(entitySaved.getDesignation());
                    }
                    Response<PrixOffreVoyageDTO> subResponse = prixOffreVoyageBusiness.create(subRequest, locale);
                    if (subResponse.isHasError()) {
                        response.setStatus(subResponse.getStatus());
                        response.setHasError(Boolean.TRUE);
                        return response;
                    }
            }

            if(!CollectionUtils.isEmpty(itemDto.getJourSemaineDTOList())){
                Request<JourSemaineDTO> subRequestJourSemaine = new Request<>();
                subRequestJourSemaine.setDatas(itemDto.getJourSemaineDTOList());
                for(JourSemaineDTO jourSemaineDTO: itemDto.getJourSemaineDTOList()){
                    jourSemaineDTO.setOffreVoyageDesignation(entitySaved.getDesignation());
                }
                Response<JourSemaineDTO> subResponse = jourSemaineBusinesse.create(subRequestJourSemaine, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }

            if(!CollectionUtils.isEmpty(itemDto.getVilleEscaleDTOList())){
                Request<VilleEscaleDTO> subRequestVilleEscale = new Request<>();
                subRequestVilleEscale.setDatas(itemDto.getVilleEscaleDTOList());
                for(VilleEscaleDTO villeEscaleDTO: itemDto.getVilleEscaleDTOList()){
                    villeEscaleDTO.setOffreVoyageDesignation(entitySaved.getDesignation());
                }
                Response<VilleEscaleDTO> subResponse = villeEscaleBusiness.create(subRequestVilleEscale, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }

            if(!CollectionUtils.isEmpty(itemDto.getValeurCaracteristiqueOffreVoyageDTOList())){
                Request<ValeurCaracteristiqueOffreVoyageDTO> subRequestValeurCaracteristiqueOffreVoyage = new Request<>();
                subRequestValeurCaracteristiqueOffreVoyage.setDatas(itemDto.getValeurCaracteristiqueOffreVoyageDTOList());
                for(ValeurCaracteristiqueOffreVoyageDTO valeurCaracteristiqueOffreVoyageDto: itemDto.getValeurCaracteristiqueOffreVoyageDTOList()){
                    valeurCaracteristiqueOffreVoyageDto.setOffreVoyageDesignation(entitySaved.getDesignation());
                }
                Response<ValeurCaracteristiqueOffreVoyageDTO> subResponse = valeurCaracteristiqueOffreVoyageBusiness.create(subRequestValeurCaracteristiqueOffreVoyage, locale);
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

        List<OffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? OffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                                    : OffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<OffreVoyageDTO> update(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<OffreVoyageDTO> response = new Response<>();
        List<OffreVoyage> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<OffreVoyageDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(OffreVoyageDTO dto: request.getDatas() ) {
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
        for(OffreVoyageDTO dto: itemsDtos) {
            OffreVoyage entityToSave = offreVoyageRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (entityToSave.getIsActif()) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Vous ne pouvez pas modifier l'offre de voyage car elle est active.", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(dto.getDesignation(), false);
                if (existingOffreVoyage != null && !existingOffreVoyage.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("OffreVoyage -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String villeDepartDesignation = entityToSave.getVilleDepart() != null && entityToSave.getVilleDepart().getDesignation() != null
                                       ?    entityToSave.getVilleDepart().getDesignation()
                                       :    null;
            if(villeDepartDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage ne possede aucune ville de départ", locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVilleDepart = villeRepository.findByDesignation(villeDepartDesignation,false);
            if (existingVilleDepart == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La ville de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getVilleDepartDesignation()) && !dto.getVilleDepartDesignation().equals(existingVilleDepart.getDesignation())) {
                Ville villeDepartToSave = villeRepository.findByDesignation(dto.getVilleDepartDesignation(), false);
                if (villeDepartToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle ville de départ  l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setVilleDepart(villeDepartToSave);
            }
            String villeDestinationDesignation=entityToSave.getVilleDestination()!=null&&entityToSave.getVilleDestination().getDesignation()!=null
                    ?entityToSave.getVilleDestination().getDesignation()
                    :null;
            if(villeDestinationDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage ne possede aucune ville de destination", locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVilleDestination = villeRepository.findByDesignation(villeDestinationDesignation,false);
            if (existingVilleDestination == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La ville de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getVilleDestinationDesignation()) && !dto.getVilleDestinationDesignation().equals(existingVilleDestination.getDesignation())) {
                Ville villeDestinationToSave = villeRepository.findByDesignation(dto.getVilleDestinationDesignation(), false);
                if (villeDestinationToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle ville de destination  l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setVilleDestination(villeDestinationToSave);
            }

            String typeOffreVoyageDesignation=entityToSave.getTypeOffreVoyage() != null && entityToSave.getTypeOffreVoyage().getDesignation() != null
                    ?   entityToSave.getTypeOffreVoyage().getDesignation()
                    :   null;

            if(typeOffreVoyageDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Le prix offre de voyage ne relie à aucun type offre de voyage", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeOffrreVoyage = referenceRepository.findByDesignation(typeOffreVoyageDesignation,false);
            if (existingTypeOffrreVoyage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le type offre de voyage de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getTypeOffreVoyageDesignation()) && !dto.getTypeOffreVoyageDesignation().equals(existingTypeOffrreVoyage.getDesignation())) {
                Reference typeOffreVoyageToSave = referenceRepository.findByDesignation(dto.getTypeOffreVoyageDesignation(), false);
                if (typeOffreVoyageToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le nouveau type offre de voyage de l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setTypeOffreVoyage(typeOffreVoyageToSave);
            }
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDesignation().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(dto.getIsActif()!=null && !dto.getIsActif().equals(entityToSave.getIsActif())){
                entityToSave.setIsActif(dto.getIsActif());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            OffreVoyage entityupdated=offreVoyageRepository.save(entityToSave);
            if(entityupdated==null){
                response.setStatus(functionalError.SAVE_FAIL("Erreur de Modification", locale));
                response.setHasError(true);
                return response;
            }
            if(!CollectionUtils.isEmpty(dto.getPrixOffreVoyageDTOList())){
                Request<PrixOffreVoyageDTO> subRequest = new Request<PrixOffreVoyageDTO>();
                subRequest.setDatas( dto.getPrixOffreVoyageDTOList());
                for(PrixOffreVoyageDTO prixOffreVoyageDTO: dto.getPrixOffreVoyageDTOList()){
                    prixOffreVoyageDTO.setOffreVoyageDesignation(entityupdated.getDesignation());
                }
                Response<PrixOffreVoyageDTO> subResponse = prixOffreVoyageBusiness.update(subRequest, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            if(!CollectionUtils.isEmpty(dto.getJourSemaineDTOList())){
                Request<JourSemaineDTO> subRequestJourSemaine = new Request<JourSemaineDTO>();
                subRequestJourSemaine.setDatas( dto.getJourSemaineDTOList());
                for(JourSemaineDTO jourSemaineDTO: dto.getJourSemaineDTOList()){
                    jourSemaineDTO.setOffreVoyageDesignation(entityupdated.getDesignation());
                }
                Response<JourSemaineDTO> subResponse = jourSemaineBusinesse.update(subRequestJourSemaine, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            if(!CollectionUtils.isEmpty(dto.getVilleEscaleDTOList())){
                Request<VilleEscaleDTO> subRequestVilleEscale = new Request<VilleEscaleDTO>();
                subRequestVilleEscale.setDatas( dto.getVilleEscaleDTOList());
                for(VilleEscaleDTO villeEscaleDTO: dto.getVilleEscaleDTOList()){
                    villeEscaleDTO.setOffreVoyageDesignation(entityupdated.getDesignation());
                }
                Response<VilleEscaleDTO> subResponse = villeEscaleBusiness.update(subRequestVilleEscale, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            items.add(entityupdated);
        }
        List<OffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? OffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                                : OffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update l'offre de voyage-----");
        return response;
    }

    @Override
    public Response<OffreVoyageDTO> delete(Request<OffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<OffreVoyageDTO> forceDelete(Request<OffreVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<OffreVoyageDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<OffreVoyageDTO> getByCriteria(Request<OffreVoyageDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<Boolean> toActiveTravelOffer(Request<OffreVoyageDTO> request, Locale locale) {
        Response<Boolean> response = new Response<Boolean>();
        List<OffreVoyage> items = new ArrayList<OffreVoyage>();
        if (request.getDatas() == null || request.getDatas().isEmpty()) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide", locale));
            response.setHasError(true);
            return response;
        }
        for (OffreVoyageDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        for (OffreVoyageDTO dto : request.getDatas()) {
            OffreVoyage existingOffreVoyage = null;
            existingOffreVoyage = offreVoyageRepository.findOne(dto.getId(), false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("L'offre de voyage que vous voulez activer n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (existingOffreVoyage.getCompagnieTransport() == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport de l'offre de voyage n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (existingOffreVoyage.getCompagnieTransport() != null
                    && (existingOffreVoyage.getCompagnieTransport().getIsValidate() == null
                    || existingOffreVoyage.getCompagnieTransport().getIsValidate() == false)) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport de l'offre de voyage n'est pas validée", locale));
                response.setHasError(true);
                return response;
            }
            if (existingOffreVoyage.getVilleDepart() == null || existingOffreVoyage.getVilleDepart().getDesignation() == null) {
                response.setStatus(functionalError.DATA_EXIST("Aucune ville de départ n'est définie pour l'offre de voyage", locale));
                response.setHasError(true);
                return response;
            }
            if (existingOffreVoyage.getVilleDepart() == null || existingOffreVoyage.getVilleDepart().getDesignation() == null) {
                response.setStatus(functionalError.DATA_EXIST("Aucune ville de départ n'est définie pour l'offre de voyage", locale));
                response.setHasError(true);
                return response;
            }
            if (existingOffreVoyage.getVilleDestination() == null || existingOffreVoyage.getVilleDestination().getDesignation() == null) {
                response.setStatus(functionalError.DATA_EXIST("Aucune ville de destination n'est définie pour l'offre de voyage", locale));
                response.setHasError(true);
                return response;
            }
            List<PrixOffreVoyage> existingEntityPrixOffreVoyageList;
            existingEntityPrixOffreVoyageList = prixOffreVoyageRepository.findAllByOffreVoyageDesignation(existingOffreVoyage.getDesignation(), false);
            if (CollectionUtils.isEmpty(existingEntityPrixOffreVoyageList)) {
                response.setStatus(functionalError.DATA_EXIST("Aucun prix n'est défini pour l'offre de voyage", locale));
                response.setHasError(true);
                return response;
            }
            Response<Boolean> responsePrixVoyage = verifierSiPrixOffreVoyageEstDifferentDeZero(locale, response, existingEntityPrixOffreVoyageList);
            if (responsePrixVoyage != null) return responsePrixVoyage;

            List<JourSemaine> existingEntityJourSemaineList;
            existingEntityJourSemaineList = jourSemaineRepository.findAllByOffreVoyageDesignation(existingOffreVoyage.getDesignation(), false);
            if (CollectionUtils.isEmpty(existingEntityJourSemaineList)) {
                response.setStatus(functionalError.DATA_EXIST("L'offre de voyayge n'est programmé sur aucun jour de la semaine", locale));
                response.setHasError(true);
                return response;
            }

            List<Programme> existingProgrammeList;
            for(JourSemaine jourSemaine: existingEntityJourSemaineList){
                if(jourSemaine != null){
                    existingProgrammeList = programmeRepository.findByJourSemaine(jourSemaine.getDesignation(),false);
                    if (CollectionUtils.isEmpty(existingProgrammeList)) {
                        response.setStatus(functionalError.DATA_EXIST("Aucun programme pour le jour de la semaine", locale));
                        response.setHasError(true);
                        return response;
                    }
                    for(Programme programme: existingProgrammeList){
                        if(programme != null){
                            if(programme.getHeureDepart() == null){
                                response.setStatus(functionalError.DATA_EXIST("Heure départ non définie", locale));
                                response.setHasError(true);
                                return response;
                            }
                            if(programme.getHeureArrivee() == null){
                                response.setStatus(functionalError.DATA_EXIST("Heure arrivee non définie", locale));
                                response.setHasError(true);
                                return response;
                            }
                        }
                    }
                }
            }
            existingOffreVoyage.setIsActif(true);
            OffreVoyage entityToActive = offreVoyageRepository.save(existingOffreVoyage);
            if(entityToActive==null){
                response.setStatus(functionalError.DATA_EXIST("Echec d'activation de l'offe de voyage", locale));
                response.setHasError(true);
                return response;
            }
        }
        response.setItem(true);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end activation l'offre de voyage-----");
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<OffreVoyageDTO> getTravelOfferByCompagnieTransport(Request<OffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<OffreVoyageDTO> response = new Response<OffreVoyageDTO>();
        List<OffreVoyage> items = new ArrayList<OffreVoyage>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("idCompagnieTransport", request.getData().getCompagnieTransportRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String raisonCompagnie = request.getData().getCompagnieTransportRaisonSociale();
        CompagnieTransport existingCompagnieTransport;
        existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(raisonCompagnie, false);
        if (existingCompagnieTransport == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        items = offreVoyageRepository.getTravelOfferByCompagnieTransport(raisonCompagnie,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie ne dipose d'aucune offre de voyage", locale));
            response.setHasError(true);
            return response;
        }

        List<OffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? OffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                : OffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update l'offre de voyage-----");
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<OffreVoyageDTO> getOffreVoyageByCriteria(Request<RechercheCritereOffreVoyageDTO> request, Locale locale) throws ParseException {
        Response<OffreVoyageDTO> response = new Response<>();
        List<OffreVoyage> itemsResponse = new ArrayList<>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("villeDepart", request.getData().getVilleDepart());
        fieldsToVerify.put("villeDestination", request.getData().getVilleDepart());
        fieldsToVerify.put("dateDepart", request.getData().getVilleDepart());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        //This code research the offre voyage by criteria
        Date dateDepart=Utilities.convertStringToDate(request.getData().getDateDepart());
        String villeDepart=request.getData().getVilleDepart();
        String villeDestination=request.getData().getVilleDestination();
        List<OffreVoyageDTO> itemsDto;
        Map<String,JourSemaine> mapOffreVoyageToJourSemaine = new HashMap<>();
        List<OffreVoyage> items = offreVoyageRepository.getOffreVoyageByCriteria(villeDepart,villeDestination,false);

        if (items != null && !items.isEmpty()){
            items.forEach(offreVoyage -> {
                List<JourSemaine> jourSemaines = jourSemaineRepository.findAllByOffreVoyageDesignation(offreVoyage.getDesignation(), false);
                jourSemaines.stream()
                        .filter(js -> js != null &&
                                js.getJourSemaine() != null &&
                                js.getJourSemaine().getDesignation() != null &&
                                js.getJourSemaine().getDesignation().equals(Utilities.getFrenchDayOfWeek(dateDepart)))
                        .forEach(js -> {
                            itemsResponse.add(js.getOffreVoyage());
                            mapOffreVoyageToJourSemaine.put(js.getOffreVoyage().getDesignation(), js);
                        });
            });
            itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                    ? OffreVoyageTransformer.INSTANCE.toLiteDtos(itemsResponse)
                    : OffreVoyageTransformer.INSTANCE.toDtos(itemsResponse);
        }else{
            itemsDto = new ArrayList<>();
        }

        if (Objects.nonNull(itemsDto) && !itemsDto.isEmpty()) {
            itemsDto.stream()
                    .filter(dto -> dto != null && dto.getDesignation() != null)
                    .forEach(dto -> {
                        Request<OffreVoyageDTO> offreVoyageDTORequest = new Request<>();
                        OffreVoyageDTO offreVoyageDTO = new OffreVoyageDTO();
                        offreVoyageDTO.setDesignation(dto.getDesignation());
                        offreVoyageDTORequest.setData(offreVoyageDTO);
                        dto.setPrixOffreVoyageDTOList(getPrixOffreVoyageDTOS(locale, offreVoyageDTORequest));
                        dto.setVilleEscaleDTOList(getVilleEscaleDTOS(locale, offreVoyageDTORequest));
                        if (mapOffreVoyageToJourSemaine.containsKey(dto.getDesignation())) {
                            dto.setJourSemaineDTOList(getJourSemaineDTOS(locale, offreVoyageDTORequest));
                        }
                    });
        }
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update l'offre de voyage-----");
        return response;
    }

    private List<JourSemaineDTO> getJourSemaineDTOS(Locale locale, Request<OffreVoyageDTO> offreVoyageDTORequest) {
        List<JourSemaineDTO> jourSemaineDTOList;
        try {
            jourSemaineDTOList = Optional.ofNullable(
                            jourSemaineBusinesse.getJourSemaineByVoyageDesignation(offreVoyageDTORequest, locale))
                    .map(Response::getItems)
                    .orElse(new ArrayList<>());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return jourSemaineDTOList;
    }

    private List<VilleEscaleDTO> getVilleEscaleDTOS(Locale locale, Request<OffreVoyageDTO> offreVoyageDTORequest) {
        List<VilleEscaleDTO> villeEscaleDTOList;
        try {
            villeEscaleDTOList = Optional.ofNullable(
                            villeEscaleBusiness.getVilleByOffreVoyageDesignation(offreVoyageDTORequest, locale))
                    .map(Response::getItems)
                    .orElse(new ArrayList<>());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return villeEscaleDTOList;
    }

    private List<PrixOffreVoyageDTO> getPrixOffreVoyageDTOS(Locale locale, Request<OffreVoyageDTO> offreVoyageDTORequest) {
        List<PrixOffreVoyageDTO> prixOffreVoyageDTOList;
        try {
            prixOffreVoyageDTOList = Optional.ofNullable(
                    prixOffreVoyageBusiness.getPrixTravelOfferByOffreVoyageDesignation(offreVoyageDTORequest, locale))
                    .map(Response::getItems)
                    .orElse(new ArrayList<>());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return prixOffreVoyageDTOList;
    }

    private Response<Boolean> verifierSiPrixOffreVoyageEstDifferentDeZero(Locale locale, Response < Boolean > response, List < PrixOffreVoyage > existingEntityPrixOffreVoyageList){
        for (PrixOffreVoyage prixOffreVoyage : existingEntityPrixOffreVoyageList) {
            if (prixOffreVoyage != null) {
                if (prixOffreVoyage.getPrix() == 0L) {
                    response.setStatus(functionalError.DATA_EXIST("Lr prix de l'offre de voyage doit être différent de 0", locale));
                    response.setHasError(true);
                    return response;
                }
            }
        }
        return null;
    }

}
