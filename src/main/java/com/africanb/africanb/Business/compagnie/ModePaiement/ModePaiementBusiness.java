package com.africanb.africanb.Business.compagnie.ModePaiement;


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
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import com.africanb.africanb.utils.Reference.Reference;
import lombok.extern.java.Log;
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
public class ModePaiementBusiness implements IBasicBusiness<Request<ModePaiementDTO>, Response<ModePaiementDTO>> {

    private Response<ModePaiementDTO> response;

    private final FunctionalError functionalError;
    private final ModePaiementRepository modePaiementRepository;
    private final ModeAbonnementRepository modeAbonnementRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReferenceRepository typeModePaiementRepository;
    private final ModePaiementEnEspeceBusiness modePaiementEnEspeceBusiness;
    private final ModePaiementMoovMoneyBusiness modePaiementMoovMoneyBusiness;
    private final ModePaiementOrangeMoneyBusiness modePaiementOrangeMoneyBusiness;
    private final ModePaiementMtnMoneyBusiness modePaiementMtnMoneyBusiness;
    private final ModePaiementWaveBusiness modePaiementWaveBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ModePaiementBusiness(FunctionalError functionalError, ModePaiementRepository modePaiementRepository, ModeAbonnementRepository modeAbonnementRepository, CompagnieTransportRepository compagnieTransportRepository, ReferenceRepository typeModePaiementRepository, ModePaiementEnEspeceBusiness modePaiementEnEspeceBusiness, ModePaiementMoovMoneyBusiness modePaiementMoovMoneyBusiness, ModePaiementOrangeMoneyBusiness modePaiementOrangeMoneyBusiness, ModePaiementMtnMoneyBusiness modePaiementMtnMoneyBusiness, ModePaiementWaveBusiness modePaiementWaveBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.functionalError = functionalError;
        this.modePaiementRepository = modePaiementRepository;
        this.modeAbonnementRepository = modeAbonnementRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.typeModePaiementRepository = typeModePaiementRepository;
        this.modePaiementEnEspeceBusiness = modePaiementEnEspeceBusiness;
        this.modePaiementMoovMoneyBusiness = modePaiementMoovMoneyBusiness;
        this.modePaiementOrangeMoneyBusiness = modePaiementOrangeMoneyBusiness;
        this.modePaiementMtnMoneyBusiness = modePaiementMtnMoneyBusiness;
        this.modePaiementWaveBusiness = modePaiementWaveBusiness;
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
                Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
            itemDto=Utilities.transformerLaClasseModePaiementtEnClasseFilleCorrespondante(itemDto);
            ModePaiementDTO entitySaved= saveModePaiementEnFonctionDeLaClasseFilleCorrespondante(itemDto,locale);
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
                Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
            };
            itemDto=Utilities.transformerLaClasseModePaiementtEnClasseFilleCorrespondante(itemDto);
            ModePaiementDTO entitySaved=null;
            entitySaved=updateModePaiementEnFonctionDeLaClasseFilleCorrespondante(itemDto,locale);
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
    public Response<ModePaiementDTO> getModePaiementByCompagnieTransport(Request<ModePaiementDTO> request, Locale locale) throws ParseException {
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
        List<ModePaiementDTO> itemsDto = transformerClasseFilleEnClasseModePaiementDTO(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Mode Paiement-----");
        return response;
    }

    public ModePaiementDTO saveModePaiementEnFonctionDeLaClasseFilleCorrespondante(ModePaiementDTO modePaiementDTO, Locale locale) throws ParseException {
        if(modePaiementDTO instanceof ModePaiementMtnMoneyDTO ){
            Request<ModePaiementMtnMoneyDTO> subRequest = new Request<>();
            ModePaiementMtnMoneyDTO modePaiementMtnMoneyDTO = (ModePaiementMtnMoneyDTO) modePaiementDTO;
            List<ModePaiementMtnMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(modePaiementMtnMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMtnMoneyDTO> subResponse = modePaiementMtnMoneyBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }

            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneMtnMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());

            return rtn;
        }
        else if(modePaiementDTO instanceof ModePaiementOrangeMoneyDTO){
            Request<ModePaiementOrangeMoneyDTO> subRequest = new Request<ModePaiementOrangeMoneyDTO>();
            List<ModePaiementOrangeMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementOrangeMoneyDTO>());
            ModePaiementOrangeMoneyDTO modePaiementOrangeMoneyDTO = (ModePaiementOrangeMoneyDTO) modePaiementDTO;
            //Conversion
            itemsDTO.add(modePaiementOrangeMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementOrangeMoneyDTO> subResponse = modePaiementOrangeMoneyBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneOrangeMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            return rtn;
        }
        else if(modePaiementDTO instanceof ModePaiementMoovMoneyDTO){
            Request<ModePaiementMoovMoneyDTO> subRequest = new Request<ModePaiementMoovMoneyDTO>();
            List<ModePaiementMoovMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementMoovMoneyDTO>());
            ModePaiementMoovMoneyDTO modePaiementMoovMoneyDTO = (ModePaiementMoovMoneyDTO) modePaiementDTO;
            //Conversion
            itemsDTO.add(modePaiementMoovMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMoovMoneyDTO> subResponse = modePaiementMoovMoneyBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneMoovMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            return rtn;
        }else if(modePaiementDTO instanceof ModePaiementEnEspeceDTO){
            Request<ModePaiementEnEspeceDTO> subRequest = new Request<ModePaiementEnEspeceDTO>();
            List<ModePaiementEnEspeceDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementEnEspeceDTO>());
            ModePaiementEnEspeceDTO modePaiementEnEspeceDTO = (ModePaiementEnEspeceDTO) modePaiementDTO;
            //Conversion
            itemsDTO.add(modePaiementEnEspeceDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementEnEspeceDTO> subResponse = modePaiementEnEspeceBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            //rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneMoovMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            return rtn;
        }
        else{

        }
        return new ModePaiementDTO();
    }

    public ModePaiementDTO updateModePaiementEnFonctionDeLaClasseFilleCorrespondante(ModePaiementDTO modePaiementDTO, Locale locale) throws ParseException {

        if(modePaiementDTO instanceof ModePaiementMtnMoneyDTO){
            Request<ModePaiementMtnMoneyDTO> subRequest = new Request<ModePaiementMtnMoneyDTO>();
            List<ModePaiementMtnMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementMtnMoneyDTO>());
            ModePaiementMtnMoneyDTO modePaiementMtnMoneyDTO = (ModePaiementMtnMoneyDTO) modePaiementDTO;
            //Conversion
            itemsDTO.add(modePaiementMtnMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMtnMoneyDTO> subResponse = modePaiementMtnMoneyBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneMtnMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            return rtn;
        }
        else if(modePaiementDTO instanceof ModePaiementOrangeMoneyDTO){
            Request<ModePaiementOrangeMoneyDTO> subRequest = new Request<ModePaiementOrangeMoneyDTO>();
            List<ModePaiementOrangeMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementOrangeMoneyDTO>());
            ModePaiementOrangeMoneyDTO modePaiementOrangeMoneyDTO = (ModePaiementOrangeMoneyDTO) modePaiementDTO;
            //Conversion
            itemsDTO.add(modePaiementOrangeMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementOrangeMoneyDTO> subResponse = modePaiementOrangeMoneyBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneOrangeMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            return rtn;
        }
        else if(modePaiementDTO instanceof ModePaiementMoovMoneyDTO){
            Request<ModePaiementMoovMoneyDTO> subRequest = new Request<ModePaiementMoovMoneyDTO>();
            List<ModePaiementMoovMoneyDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementMoovMoneyDTO>());
            ModePaiementMoovMoneyDTO modePaiementMoovMoneyDTO = (ModePaiementMoovMoneyDTO) modePaiementDTO;
            //Conversion
            itemsDTO.add(modePaiementMoovMoneyDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementMoovMoneyDTO> subResponse = modePaiementMoovMoneyBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneMoovMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            return rtn;
        }else if(modePaiementDTO instanceof ModePaiementEnEspeceDTO){
            Request<ModePaiementEnEspeceDTO> subRequest = new Request<ModePaiementEnEspeceDTO>();
            List<ModePaiementEnEspeceDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementEnEspeceDTO>());
            ModePaiementEnEspeceDTO modePaiementEnEspeceDTO = (ModePaiementEnEspeceDTO) modePaiementDTO;
            //Conversion
            itemsDTO.add(modePaiementEnEspeceDTO);
            subRequest.setDatas( itemsDTO);
            Response<ModePaiementEnEspeceDTO> subResponse = modePaiementEnEspeceBusiness.update(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModePaiementDTO();
            }
            ModePaiementDTO rtn = new ModePaiementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());
            //rtn.setTelephoneGenerique( subResponse.getItems().get(0).getTelephoneMoovMoney());
            rtn.setTypeModePaiementDesignation(subResponse.getItems().get(0).getTypeModePaiementDesignation());
            rtn.setCompagnieTransportRaisonSociale(subResponse.getItems().get(0).getCompagnieTransportRaisonSociale());

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
            rtn.setOrderDirection(subResponse.getItems().get(0).getOrderDirection());
            return rtn;
        }
        else{

        }
        return new ModePaiementDTO();
    }

    public  List<ModePaiementDTO> transformerClasseFilleEnClasseModePaiementDTO(List<ModePaiement> modePaiementList) {
        List<ModePaiementDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModePaiementDTO>());

        for(ModePaiement modePaiement:modePaiementList) {
            if(modePaiement instanceof ModePaiementMoovMoney){
                ModePaiementMoovMoney modePaiementMoovMoney = new ModePaiementMoovMoney();
                modePaiementMoovMoney= (ModePaiementMoovMoney) modePaiement;
                ModePaiementDTO rtn = new ModePaiementDTO();

                rtn.setId(modePaiementMoovMoney.getId());
                rtn.setDesignation(modePaiementMoovMoney.getDesignation());
                rtn.setDescription(modePaiementMoovMoney.getDescription());

                rtn.setTelephoneGenerique(modePaiementMoovMoney.getTelephoneMoovMoney());
                rtn.setTypeModePaiementDesignation(modePaiementMoovMoney.getTypeModePaiement().getDesignation());
                rtn.setCompagnieTransportRaisonSociale(modePaiementMoovMoney.getCompagnieTransport().getRaisonSociale());

                rtn.setDeletedAt(modePaiementMoovMoney.getDeletedAt()!=null?modePaiementMoovMoney.getDeletedAt().toString():null);
                rtn.setUpdatedAt(modePaiementMoovMoney.getUpdatedAt()!=null?modePaiementMoovMoney.getUpdatedAt().toString():null);
                rtn.setCreatedAt(modePaiementMoovMoney.getCreatedAt()!=null?modePaiementMoovMoney.getCreatedAt().toString():null);
                rtn.setCreatedBy(modePaiementMoovMoney.getCreatedBy());
                rtn.setIsDeleted(modePaiementMoovMoney.getIsDeleted());
                rtn.setDeletedBy(modePaiementMoovMoney.getDeletedBy());
                rtn.setUpdatedBy(modePaiementMoovMoney.getUpdatedBy());

                itemsDTO.add(rtn);
            }
            else if(modePaiement instanceof ModePaiementOrangeMoney){
                ModePaiementOrangeMoney modePaiementOrangeMoney = new ModePaiementOrangeMoney();
                modePaiementOrangeMoney= (ModePaiementOrangeMoney) modePaiement;
                ModePaiementDTO rtn = new ModePaiementDTO();

                rtn.setId(modePaiementOrangeMoney.getId());
                rtn.setDesignation(modePaiementOrangeMoney.getDesignation());
                rtn.setDescription(modePaiementOrangeMoney.getDescription());

                rtn.setTelephoneGenerique(modePaiementOrangeMoney.getTelephoneOrangeMoney());
                rtn.setTypeModePaiementDesignation(modePaiementOrangeMoney.getTypeModePaiement().getDesignation());
                rtn.setCompagnieTransportRaisonSociale(modePaiementOrangeMoney.getCompagnieTransport().getRaisonSociale());

                rtn.setDeletedAt(modePaiementOrangeMoney.getDeletedAt()!=null?modePaiementOrangeMoney.getDeletedAt().toString():null);
                rtn.setUpdatedAt(modePaiementOrangeMoney.getUpdatedAt()!=null?modePaiementOrangeMoney.getUpdatedAt().toString():null);
                rtn.setCreatedAt(modePaiementOrangeMoney.getCreatedAt()!=null?modePaiementOrangeMoney.getCreatedAt().toString():null);
                rtn.setCreatedBy(modePaiementOrangeMoney.getCreatedBy());
                rtn.setIsDeleted(modePaiementOrangeMoney.getIsDeleted());
                rtn.setDeletedBy(modePaiementOrangeMoney.getDeletedBy());
                rtn.setUpdatedBy(modePaiementOrangeMoney.getUpdatedBy());

                itemsDTO.add(rtn);
            }else if(modePaiement instanceof ModePaiementMtnMoney){
                ModePaiementMtnMoney modePaiementMtnMoney = new ModePaiementMtnMoney();
                modePaiementMtnMoney= (ModePaiementMtnMoney) modePaiement;
                ModePaiementDTO rtn = new ModePaiementDTO();

                rtn.setId(modePaiementMtnMoney.getId());
                rtn.setDesignation(modePaiementMtnMoney.getDesignation());
                rtn.setDescription(modePaiementMtnMoney.getDescription());

                rtn.setTelephoneGenerique(modePaiementMtnMoney.getTelephoneMtnMoney());
                rtn.setTypeModePaiementDesignation(modePaiementMtnMoney.getTypeModePaiement().getDesignation());
                rtn.setCompagnieTransportRaisonSociale(modePaiementMtnMoney.getCompagnieTransport().getRaisonSociale());

                rtn.setDeletedAt(modePaiementMtnMoney.getDeletedAt()!=null?modePaiementMtnMoney.getDeletedAt().toString():null);
                rtn.setUpdatedAt(modePaiementMtnMoney.getUpdatedAt()!=null?modePaiementMtnMoney.getUpdatedAt().toString():null);
                rtn.setCreatedAt(modePaiementMtnMoney.getCreatedAt()!=null?modePaiementMtnMoney.getCreatedAt().toString():null);
                rtn.setCreatedBy(modePaiementMtnMoney.getCreatedBy());
                rtn.setIsDeleted(modePaiementMtnMoney.getIsDeleted());
                rtn.setDeletedBy(modePaiementMtnMoney.getDeletedBy());
                rtn.setUpdatedBy(modePaiementMtnMoney.getUpdatedBy());

                itemsDTO.add(rtn);
            }else if(modePaiement instanceof ModePaiementWave){
                ModePaiementWave modePaiementWave = new ModePaiementWave();
                modePaiementWave = (ModePaiementWave) modePaiement;
                ModePaiementDTO rtn = new ModePaiementDTO();

                rtn.setId(modePaiementWave.getId());
                rtn.setDesignation(modePaiementWave.getDesignation());
                rtn.setDescription(modePaiementWave.getDescription());

                rtn.setTelephoneGenerique(modePaiementWave.getTelephoneWave());
                rtn.setTypeModePaiementDesignation(modePaiementWave.getTypeModePaiement().getDesignation());
                rtn.setCompagnieTransportRaisonSociale(modePaiementWave.getCompagnieTransport().getRaisonSociale());

                rtn.setDeletedAt(modePaiementWave.getDeletedAt()!=null?modePaiementWave.getDeletedAt().toString():null);
                rtn.setUpdatedAt(modePaiementWave.getUpdatedAt()!=null?modePaiementWave.getUpdatedAt().toString():null);
                rtn.setCreatedAt(modePaiementWave.getCreatedAt()!=null?modePaiementWave.getCreatedAt().toString():null);
                rtn.setCreatedBy(modePaiementWave.getCreatedBy());
                rtn.setIsDeleted(modePaiementWave.getIsDeleted());
                rtn.setDeletedBy(modePaiementWave.getDeletedBy());
                rtn.setUpdatedBy(modePaiementWave.getUpdatedBy());

                itemsDTO.add(rtn);
            }else if(modePaiement instanceof ModePaiementEnEspece){
                ModePaiementEnEspece modePaiementEnEspece = new ModePaiementEnEspece();
                modePaiementEnEspece = (ModePaiementEnEspece) modePaiement;
                ModePaiementDTO rtn = new ModePaiementDTO();

                rtn.setId(modePaiementEnEspece.getId());
                rtn.setDesignation(modePaiementEnEspece.getDesignation());
                rtn.setDescription(modePaiementEnEspece.getDescription());

                rtn.setTypeModePaiementDesignation(modePaiementEnEspece.getTypeModePaiement().getDesignation());
                rtn.setCompagnieTransportRaisonSociale(modePaiementEnEspece.getCompagnieTransport().getRaisonSociale());

                rtn.setDeletedAt(modePaiementEnEspece.getDeletedAt()!=null?modePaiementEnEspece.getDeletedAt().toString():null);
                rtn.setUpdatedAt(modePaiementEnEspece.getUpdatedAt()!=null?modePaiementEnEspece.getUpdatedAt().toString():null);
                rtn.setCreatedAt(modePaiementEnEspece.getCreatedAt()!=null?modePaiementEnEspece.getCreatedAt().toString():null);
                rtn.setCreatedBy(modePaiementEnEspece.getCreatedBy());
                rtn.setIsDeleted(modePaiementEnEspece.getIsDeleted());
                rtn.setDeletedBy(modePaiementEnEspece.getDeletedBy());
                rtn.setUpdatedBy(modePaiementEnEspece.getUpdatedBy());

                itemsDTO.add(rtn);
            }
            else{

            }
        }
        return itemsDTO;
    }
}