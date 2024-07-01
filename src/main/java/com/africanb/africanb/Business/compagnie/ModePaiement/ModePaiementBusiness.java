package com.africanb.africanb.Business.compagnie.ModePaiement;

import com.africanb.africanb.Business.design.factory.modePaiment.ModePaiementDTOCreator;
import com.africanb.africanb.Business.design.factory.modePaiment.ModePaiementEntityCreator;
import com.africanb.africanb.Business.design.factory.modePaiment.ModePaiementUtils;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.ModeAbonnement;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.*;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.ModeAbonnementRepository;
import com.africanb.africanb.dao.repository.compagnie.ModePaiement.ModePaiementRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.*;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import com.africanb.africanb.utils.Reference.Reference;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Log
@Component
public class ModePaiementBusiness implements IBasicBusiness<Request<ModePaiementDTO>, Response<ModePaiementDTO>> {

    private  Response<ModePaiementDTO> response;

    private final FunctionalError functionalError;
    private final ModePaiementRepository modePaiementRepository;
    private final ModeAbonnementRepository modeAbonnementRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReferenceRepository typeModePaiementRepository;
    private final ModePaiementEnEspeceBusiness modePaiementEnEspeceBusiness;
    private final ModePaiementMoovMoneyBusiness modePaiementMoovMoneyBusiness;
    private final ModePaiementOrangeMoneyBusiness modePaiementOrangeMoneyBusiness;
    private  final ModePaiementWaveBusiness modePaiementWaveBusiness;
    private final ModePaiementMtnMoneyBusiness modePaiementMtnMoneyBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ModePaiementBusiness(FunctionalError functionalError, ModePaiementRepository modePaiementRepository, ModeAbonnementRepository modeAbonnementRepository, CompagnieTransportRepository compagnieTransportRepository, ReferenceRepository typeModePaiementRepository, ModePaiementEnEspeceBusiness modePaiementEnEspeceBusiness, ModePaiementMoovMoneyBusiness modePaiementMoovMoneyBusiness, ModePaiementOrangeMoneyBusiness modePaiementOrangeMoneyBusiness, ModePaiementWaveBusiness modePaiementWaveBusiness, ModePaiementMtnMoneyBusiness modePaiementMtnMoneyBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.functionalError = functionalError;
        this.modePaiementRepository = modePaiementRepository;
        this.modeAbonnementRepository = modeAbonnementRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.typeModePaiementRepository = typeModePaiementRepository;
        this.modePaiementEnEspeceBusiness = modePaiementEnEspeceBusiness;
        this.modePaiementMoovMoneyBusiness = modePaiementMoovMoneyBusiness;
        this.modePaiementOrangeMoneyBusiness = modePaiementOrangeMoneyBusiness;
        this.modePaiementWaveBusiness = modePaiementWaveBusiness;
        this.modePaiementMtnMoneyBusiness = modePaiementMtnMoneyBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ModePaiementDTO> create(Request<ModePaiementDTO> request, Locale locale) throws ParseException {
        Response<ModePaiementDTO> response = new Response<>();
        List<ModePaiementDTO> itemsDto = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }

        List<ModePaiementDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(ModePaiementDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<>();
                fieldsToVerify.put("designation", dto.getDesignation());
                fieldsToVerify.put("compagnieTransportRaisonSociale", dto.getCompagnieTransportRaisonSociale());
                fieldsToVerify.put("typeModeAbonnementDesignation", dto.getTypeModePaiementDesignation());
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

        for(ModePaiementDTO itemDto : itemsDtos){
            CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            List<ModeAbonnement> exitingModeAbonnementList = modeAbonnementRepository.findByCompagnieTransport(itemDto.getCompagnieTransportRaisonSociale(),false);
            if(CollectionUtils.isEmpty(exitingModeAbonnementList)){
                response.setStatus(functionalError.SAVE_FAIL("La compagnie ne dispose pas de mode abonnement", locale));
                response.setHasError(true);
                return response;
            }else{
                for(ModeAbonnement modeAbonnement: exitingModeAbonnementList){
                    if(modeAbonnement != null){
                        if(modeAbonnement instanceof AbonnementPrelevement){
                            if(itemDto.getTypeModePaiementDesignation().equalsIgnoreCase(ProjectConstants.REF_ELEMENT_MODE_PAIEMENT_EN_ESPECE)){
                                response.setStatus(functionalError.DATA_EXIST("Impossible de pouvoir définir un mode de paiement en espece.Car,la société a défini un mode d'abonnement prélevement", locale));
                                response.setHasError(true);
                                return response;
                            }
                        }
                    }
                }
            }
            Reference existingTypeModeAbonnement = typeModePaiementRepository.findByDesignation(itemDto.getTypeModePaiementDesignation(),false);
            if (existingTypeModeAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de mode d'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ModePaiementDTO entitySaved = saveModePaiement(ModePaiementUtils.transformAbstractClassIntoChildClass(itemDto),locale);
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
    public Response<ModePaiementDTO> update(Request<ModePaiementDTO> request, Locale locale) throws ParseException {
        Response<ModePaiementDTO> response = new Response<>();
        List<ModePaiementDTO> itemsDto= new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }

        List<ModePaiementDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(ModePaiementDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<>();
                fieldsToVerify.put("id", dto.getId());
                fieldsToVerify.put("compagnieTransportRaisonSociale", dto.getCompagnieTransportRaisonSociale());
                fieldsToVerify.put("typeModePaiementDesignation", dto.getTypeModePaiementDesignation());
                if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                    response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                    response.setHasError(true);
                    return response;
                }
                if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                    response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de l'identifiant'" + dto.getId() + "' pour les abonnements", locale));
                    response.setHasError(true);
                    return response;
                }
                itemsDtos.add(dto);
            }
        }

        for(ModePaiementDTO itemDto : itemsDtos){
            CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingPeriodiciteAbonnement = typeModePaiementRepository.findByDesignation(itemDto.getTypeModePaiementDesignation(),false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("La periodicite de l'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeModeAbonnement = typeModePaiementRepository.findByDesignation(itemDto.getTypeModePaiementDesignation(),false);
            if (existingTypeModeAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de mode d'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ModePaiementDTO entitySaved = updateModePaiement(ModePaiementUtils.transformAbstractClassIntoChildClass(itemDto),locale);
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
    public Response<ModePaiementDTO> delete(Request<ModePaiementDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ModePaiementDTO> forceDelete(Request<ModePaiementDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ModePaiementDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ModePaiementDTO> getByCriteria(Request<ModePaiementDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ModePaiementDTO> getModePaiementByCompagnieTransport(Request<ModePaiementDTO> request, Locale locale) {
        Response<ModePaiementDTO> response = new Response<>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("compagnieTransportRaisonSociale", request.getData().getCompagnieTransportRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String compagnieTransportRaisonScoiale=request.getData().getCompagnieTransportRaisonSociale();
        CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(compagnieTransportRaisonScoiale,false);
        if (existingCompagnieTransport == null) {
            response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiement> items = modePaiementRepository.findByCompagnieTransport(compagnieTransportRaisonScoiale,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport ne dispose d'aucun mode de paiement", locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementDTO> itemsDto = buildModePaiementDTOFromEntity(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Mode Paiement-----");
        return response;
    }

    public ModePaiementDTO saveModePaiement(ModePaiementDTO modePaiementDTO, Locale locale) throws ParseException {
        if(modePaiementDTO instanceof ModePaiementMtnMoneyDTO modePaiementMtnMoneyDTO){
            Request<ModePaiementMtnMoneyDTO> subRequest = new Request<>();
            List<ModePaiementMtnMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementMtnMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMtnMoneyDTO> subResponse = modePaiementMtnMoneyBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            return ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
         if(modePaiementDTO instanceof ModePaiementOrangeMoneyDTO modePaiementOrangeMoneyDTO){
            Request<ModePaiementOrangeMoneyDTO> subRequest = new Request<>();
            List<ModePaiementOrangeMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementOrangeMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementOrangeMoneyDTO> subResponse = modePaiementOrangeMoneyBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
             return ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
         if(modePaiementDTO instanceof ModePaiementMoovMoneyDTO modePaiementMoovMoneyDTO){
            Request<ModePaiementMoovMoneyDTO> subRequest = new Request<>();
            List<ModePaiementMoovMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementMoovMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMoovMoneyDTO> subResponse = modePaiementMoovMoneyBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
             return ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
         if(modePaiementDTO instanceof ModePaiementEnEspeceDTO modePaiementEnEspeceDTO){
            Request<ModePaiementEnEspeceDTO> subRequest = new Request<>();
            List<ModePaiementEnEspeceDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementEnEspeceDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementEnEspeceDTO> subResponse = modePaiementEnEspeceBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
             return ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
        if(modePaiementDTO instanceof ModePaiementWaveDTO modePaiementWaveDTO){
            Request<ModePaiementWaveDTO> subRequest = new Request<>();
            List<ModePaiementWaveDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementWaveDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementWaveDTO> subResponse = modePaiementWaveBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            return ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
        return new ModePaiementDTO();
    }

    public ModePaiementDTO updateModePaiement(ModePaiementDTO modePaiementDTO, Locale locale) throws ParseException {
        if(modePaiementDTO instanceof ModePaiementMtnMoneyDTO modePaiementMtnMoneyDTO){
            Request<ModePaiementMtnMoneyDTO> subRequest = new Request<>();
            List<ModePaiementMtnMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementMtnMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMtnMoneyDTO> subResponse = modePaiementMtnMoneyBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
        if(modePaiementDTO instanceof ModePaiementOrangeMoneyDTO modePaiementOrangeMoneyDTO){
            Request<ModePaiementOrangeMoneyDTO> subRequest = new Request<>();
            List<ModePaiementOrangeMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementOrangeMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementOrangeMoneyDTO> subResponse = modePaiementOrangeMoneyBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
        if(modePaiementDTO instanceof ModePaiementMoovMoneyDTO modePaiementMoovMoneyDTO){
            Request<ModePaiementMoovMoneyDTO> subRequest = new Request<>();
            List<ModePaiementMoovMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementMoovMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMoovMoneyDTO> subResponse = modePaiementMoovMoneyBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
        if(modePaiementDTO instanceof ModePaiementEnEspeceDTO modePaiementEnEspeceDTO){
            Request<ModePaiementEnEspeceDTO> subRequest = new Request<>();
            List<ModePaiementEnEspeceDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementEnEspeceDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementEnEspeceDTO> subResponse = modePaiementEnEspeceBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTOCreator.createModePaiementDTO(subResponse.getItems().get(0));
        }
        return new ModePaiementDTO();
    }

    public  List<ModePaiementDTO> buildModePaiementDTOFromEntity(List<ModePaiement> modePaiementList) {
        return  modePaiementList.stream()
                    .filter(Objects::nonNull)
                    .map(modePaiement -> ModePaiementEntityCreator.createModePaiementDTO(modePaiement))
                    .collect(Collectors.toList());
    }
}