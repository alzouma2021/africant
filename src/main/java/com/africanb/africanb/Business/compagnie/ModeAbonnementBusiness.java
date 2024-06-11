package com.africanb.africanb.Business.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPeriodique;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.ModeAbonnement;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.ModeAbonnementRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPeriodiqueDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;
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
public class ModeAbonnementBusiness implements IBasicBusiness<Request<ModeAbonnementDTO>, Response<ModeAbonnementDTO>> {

    private Response<ModeAbonnementDTO> response;

    private final FunctionalError functionalError;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReferenceRepository referenceRepository;
    private final AbonnementPeriodiqueBusiness abonnementPeriodiqueBusiness;
    private final ModeAbonnementRepository modeAbonnementRepository;
    private final AbonnementPrelevementBusiness abonnementPrelevementBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ModeAbonnementBusiness(FunctionalError functionalError, CompagnieTransportRepository compagnieTransportRepository, ReferenceRepository referenceRepository, AbonnementPeriodiqueBusiness abonnementPeriodiqueBusiness, ModeAbonnementRepository modeAbonnementRepository, AbonnementPrelevementBusiness abonnementPrelevementBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.functionalError = functionalError;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.referenceRepository = referenceRepository;
        this.abonnementPeriodiqueBusiness = abonnementPeriodiqueBusiness;
        this.modeAbonnementRepository = modeAbonnementRepository;
        this.abonnementPrelevementBusiness = abonnementPrelevementBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ModeAbonnementDTO> create(Request<ModeAbonnementDTO> request, Locale locale) throws ParseException {
        Response<ModeAbonnementDTO> response = new Response<ModeAbonnementDTO>();
        List<ModeAbonnement> items = new ArrayList<ModeAbonnement>();
        List<ModeAbonnementDTO> itemsDto= new ArrayList<ModeAbonnementDTO>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ModeAbonnementDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<ModeAbonnementDTO>());
        for(ModeAbonnementDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
                fieldsToVerify.put("designation", dto.getDesignation());
                fieldsToVerify.put("compagnieTransportRaisonSociale", dto.getCompagnieTransportRaisonSociale());
                fieldsToVerify.put("periodiciteAbonnementDesignation", dto.getPeriodiciteAbonnementDesignation());
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
        for(ModeAbonnementDTO itemDto : itemsDtos){
            //Verify Compagnie transport
            CompagnieTransport existingCompagnieTransport = null;
            existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Verify periodiciteAbonnement
            Reference existingPeriodiciteAbonnement = null;
            existingPeriodiciteAbonnement = referenceRepository.findByDesignation(itemDto.getPeriodiciteAbonnementDesignation(),false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("La periodicite de l'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Verify typeModeAbonnement
            Reference existingTypeModeAbonnement = null;
            existingTypeModeAbonnement = referenceRepository.findByDesignation(itemDto.getTypeModeAbonnementDesignation(),false);
            if (existingTypeModeAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de mode d'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Check if the compagny have a mode abonnement
            List<ModeAbonnement> exitingModeAbonnementList=null;
            exitingModeAbonnementList=modeAbonnementRepository.findByCompagnieTransport(itemDto.getCompagnieTransportRaisonSociale(),false);
            if(!CollectionUtils.isEmpty(exitingModeAbonnementList)){
                response.setStatus(functionalError.SAVE_FAIL("La compagnie possede deja un mode d'abonnement", locale));
                response.setHasError(true);
                return response;
            }
            itemDto=Utilities.transformerLaClasseModeAbonnementEnClasseFilleCorrespondante(itemDto);
            ModeAbonnementDTO entitySaved=null;
            entitySaved=saveModeAbonnementEnFonctionDeLaClasseFilleCorrespondante(itemDto,locale);
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
    public Response<ModeAbonnementDTO> update(Request<ModeAbonnementDTO> request, Locale locale) throws ParseException {
        Response<ModeAbonnementDTO> response = new Response<ModeAbonnementDTO>();
        List<ModeAbonnement> items = new ArrayList<ModeAbonnement>();
        List<ModeAbonnementDTO> itemsDto= new ArrayList<ModeAbonnementDTO>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ModeAbonnementDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<ModeAbonnementDTO>());
        for(ModeAbonnementDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
                fieldsToVerify.put("id", dto.getId());
                fieldsToVerify.put("compagnieTransportRaisonSociale", dto.getCompagnieTransportRaisonSociale());
                fieldsToVerify.put("periodiciteAbonnementDesignation", dto.getPeriodiciteAbonnementDesignation());
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
        for(ModeAbonnementDTO itemDto : itemsDtos){
            //Verify Compagnie transport
            CompagnieTransport existingCompagnieTransport = null;
            existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Verify periodiciteAbonnement
            Reference existingPeriodiciteAbonnement = null;
            existingPeriodiciteAbonnement = referenceRepository.findByDesignation(itemDto.getPeriodiciteAbonnementDesignation(),false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("La periodicite de l'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Verify typeModeAbonnement
            Reference existingTypeModeAbonnement = null;
            existingTypeModeAbonnement = referenceRepository.findByDesignation(itemDto.getTypeModeAbonnementDesignation(),false);
            if (existingTypeModeAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de mode d'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            };
            itemDto=Utilities.transformerLaClasseModeAbonnementEnClasseFilleCorrespondante(itemDto);
            ModeAbonnementDTO entitySaved=null;
            entitySaved=updateModeAbonnementEnFonctionDeLaClasseFilleCorrespondante(itemDto,locale);
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
    public Response<ModeAbonnementDTO> delete(Request<ModeAbonnementDTO> request, Locale locale) {
        return new Response<>();
    }

    @Override
    public Response<ModeAbonnementDTO> forceDelete(Request<ModeAbonnementDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ModeAbonnementDTO> getAll(Locale locale) throws ParseException {
        return new Response<>();
    }

    @Override
    public Response<ModeAbonnementDTO> getByCriteria(Request<ModeAbonnementDTO> request, Locale locale) {
        return new Response<>();
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ModeAbonnementDTO> getModeAbonnementByCompagnieTransport(Request<ModeAbonnementDTO> request, Locale locale) throws ParseException {
        Response<ModeAbonnementDTO> response = new Response<ModeAbonnementDTO>();
        List<ModeAbonnementDTO> itemsDto= new ArrayList<ModeAbonnementDTO>();
        List<ModeAbonnement> items = new ArrayList<ModeAbonnement>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("compagnieTransportRaisonSociale", request.getData().getCompagnieTransportRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String compagnieTransportRaisonScoiale=request.getData().getCompagnieTransportRaisonSociale();
        CompagnieTransport existingCompagnieTransport = null;
        existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(compagnieTransportRaisonScoiale,false);
        if (existingCompagnieTransport == null) {
            response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        items=(List<ModeAbonnement>) modeAbonnementRepository.findByCompagnieTransport(compagnieTransportRaisonScoiale,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport ne dispose d'aucun mode d'abonnement", locale));
            response.setHasError(true);
            return response;
        }
        itemsDto=transformerClasseFilleEnClasseModeAbonnementDTO(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Mode Abonnement-----");
        return response;
    }

    public ModeAbonnementDTO saveModeAbonnementEnFonctionDeLaClasseFilleCorrespondante(ModeAbonnementDTO modeAbonnementDTO,Locale locale) throws ParseException {

        if(modeAbonnementDTO instanceof AbonnementPeriodiqueDTO){

            Request<AbonnementPeriodiqueDTO> subRequest = new Request<AbonnementPeriodiqueDTO>();
            List<AbonnementPeriodiqueDTO> itemsDTO = Collections.synchronizedList(new ArrayList<AbonnementPeriodiqueDTO>());
            AbonnementPeriodiqueDTO abonnementPeriodiqueDTO = (AbonnementPeriodiqueDTO) modeAbonnementDTO;
            //Conversion
            itemsDTO.add(abonnementPeriodiqueDTO);
            subRequest.setDatas( itemsDTO);
            Response<AbonnementPeriodiqueDTO> subResponse = abonnementPeriodiqueBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModeAbonnementDTO();
            }
            ModeAbonnementDTO rtn = new ModeAbonnementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setRedevance( subResponse.getItems().get(0).getRedevance());
            rtn.setRedevancePublicite( subResponse.getItems().get(0).getRedevancePublicite());
            rtn.setDateDebutAbonnement(subResponse.getItems().get(0).getDateDebutAbonnement());
            rtn.setDateFinAbonnement(subResponse.getItems().get(0).getDateFinAbonnement());
            rtn.setPeriodiciteAbonnementDesignation(subResponse.getItems().get(0).getPeriodiciteAbonnementDesignation());
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
        else if(modeAbonnementDTO instanceof AbonnementPrelevementDTO){
            Request<AbonnementPrelevementDTO> subRequest = new Request<AbonnementPrelevementDTO>();
            List<AbonnementPrelevementDTO> itemsDTO = Collections.synchronizedList(new ArrayList<AbonnementPrelevementDTO>());
            AbonnementPrelevementDTO abonnementPrelevementDTO = (AbonnementPrelevementDTO) modeAbonnementDTO;
            //Conversion
            itemsDTO.add(abonnementPrelevementDTO);
            subRequest.setDatas( itemsDTO);
            Response<AbonnementPrelevementDTO> subResponse = abonnementPrelevementBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new AbonnementPrelevementDTO();
            }
            ModeAbonnementDTO rtn = new ModeAbonnementDTO();
            rtn.setId( subResponse.getItems().get(0).getId());
            rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
            rtn.setDescription( subResponse.getItems().get(0).getDescription());

            rtn.setTaux( subResponse.getItems().get(0).getTaux());
            rtn.setDateDebutAbonnement(subResponse.getItems().get(0).getDateDebutAbonnement());
            rtn.setDateFinAbonnement(subResponse.getItems().get(0).getDateFinAbonnement());
            rtn.setPeriodiciteAbonnementDesignation(subResponse.getItems().get(0).getPeriodiciteAbonnementDesignation());
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
        return new ModeAbonnementDTO();
    }


    public ModeAbonnementDTO updateModeAbonnementEnFonctionDeLaClasseFilleCorrespondante(ModeAbonnementDTO modeAbonnementDTO,Locale locale) throws ParseException {
      if(modeAbonnementDTO!=null){
          if(modeAbonnementDTO.getTypeModeAbonnementDesignation()!= null
                  && modeAbonnementDTO.getTypeModeAbonnementDesignation().equals(ProjectConstants.REF_ELEMENT_ABONNEMENT_PERIODIQUE)){
              Request<AbonnementPeriodiqueDTO> subRequest = new Request<AbonnementPeriodiqueDTO>();
              List<AbonnementPeriodiqueDTO> itemsDTO = Collections.synchronizedList(new ArrayList<AbonnementPeriodiqueDTO>());
              AbonnementPeriodiqueDTO abonnementPeriodiqueDTO = (AbonnementPeriodiqueDTO) modeAbonnementDTO;
              //Conversion
              itemsDTO.add(abonnementPeriodiqueDTO);
              subRequest.setDatas( itemsDTO);
              Response<AbonnementPeriodiqueDTO> subResponse = abonnementPeriodiqueBusiness.update(subRequest,locale);
              if (subResponse.isHasError()) {
                  response.setStatus(subResponse.getStatus());
                  response.setHasError(Boolean.TRUE);
                  return new ModeAbonnementDTO();
              }
              ModeAbonnementDTO rtn = new ModeAbonnementDTO();
              rtn.setId( subResponse.getItems().get(0).getId());
              rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
              rtn.setDescription( subResponse.getItems().get(0).getDescription());

              rtn.setRedevance( subResponse.getItems().get(0).getRedevance());
              rtn.setRedevancePublicite( subResponse.getItems().get(0).getRedevancePublicite());
              rtn.setDateDebutAbonnement(subResponse.getItems().get(0).getDateDebutAbonnement());
              rtn.setDateFinAbonnement(subResponse.getItems().get(0).getDateFinAbonnement());
              rtn.setPeriodiciteAbonnementDesignation(subResponse.getItems().get(0).getPeriodiciteAbonnementDesignation());
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
          else if(modeAbonnementDTO.getTypeModeAbonnementDesignation()!= null
                  && modeAbonnementDTO.getTypeModeAbonnementDesignation().equals(ProjectConstants.REF_ELEMENT_ABONNEMENT_PRELEVEMENT)){
              Request<AbonnementPrelevementDTO> subRequest = new Request<AbonnementPrelevementDTO>();
              List<AbonnementPrelevementDTO> itemsDTO = Collections.synchronizedList(new ArrayList<AbonnementPrelevementDTO>());
              AbonnementPrelevementDTO abonnementPrelevementDTO = (AbonnementPrelevementDTO) modeAbonnementDTO;
              //Conversion
              itemsDTO.add(abonnementPrelevementDTO);
              subRequest.setDatas(itemsDTO);
              Response<AbonnementPrelevementDTO> subResponse = abonnementPrelevementBusiness.update(subRequest,locale);
              if (subResponse.isHasError()) {
                  response.setStatus(subResponse.getStatus());
                  response.setHasError(Boolean.TRUE);
                  return new AbonnementPrelevementDTO();
              }
              ModeAbonnementDTO rtn = new ModeAbonnementDTO();
              rtn.setId( subResponse.getItems().get(0).getId());
              rtn.setDesignation( subResponse.getItems().get(0).getDesignation());
              rtn.setDescription( subResponse.getItems().get(0).getDescription());

              // rtn.setRedevance( subResponse.getItems().get(0).getRedevance());
              rtn.setTaux( subResponse.getItems().get(0).getTaux());
              rtn.setDateDebutAbonnement(subResponse.getItems().get(0).getDateDebutAbonnement());
              rtn.setDateFinAbonnement(subResponse.getItems().get(0).getDateFinAbonnement());
              rtn.setPeriodiciteAbonnementDesignation(subResponse.getItems().get(0).getPeriodiciteAbonnementDesignation());
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
      }
      return new ModeAbonnementDTO();
    }

    public  List<ModeAbonnementDTO> transformerClasseFilleEnClasseModeAbonnementDTO(List<ModeAbonnement> modeAbonnementList) {
        List<ModeAbonnementDTO> itemsDTO = Collections.synchronizedList(new ArrayList<ModeAbonnementDTO>());

        for(ModeAbonnement modeAbonnement:modeAbonnementList) {
            if(modeAbonnement instanceof AbonnementPrelevement){
                AbonnementPrelevement abonnementPrelevement = new AbonnementPrelevement();
                abonnementPrelevement= (AbonnementPrelevement) modeAbonnement;
                ModeAbonnementDTO rtn = new ModeAbonnementDTO();

                rtn.setId(abonnementPrelevement.getId());
                rtn.setDesignation(abonnementPrelevement.getDesignation());
                rtn.setDescription( abonnementPrelevement.getDescription());

                // rtn.setRedevance( subResponse.getItems().get(0).getRedevance());
                rtn.setTaux(abonnementPrelevement.getTaux());
                rtn.setDateDebutAbonnement(abonnementPrelevement.getDateDebutAbonnement().toString());
                rtn.setDateFinAbonnement(abonnementPrelevement.getDateFinAbonnement().toString());
                rtn.setPeriodiciteAbonnementDesignation(abonnementPrelevement.getPeriodiciteAbonnement().getDesignation());
                rtn.setCompagnieTransportRaisonSociale(abonnementPrelevement.getCompagnieTransport().getRaisonSociale());

                rtn.setDeletedAt( abonnementPrelevement.getDeletedAt()!=null?abonnementPrelevement.getDeletedAt().toString():null);
                rtn.setUpdatedAt( abonnementPrelevement.getUpdatedAt()!=null?abonnementPrelevement.getUpdatedAt().toString():null);
                rtn.setCreatedAt( abonnementPrelevement.getCreatedAt()!=null?abonnementPrelevement.getCreatedAt().toString():null);
                rtn.setCreatedBy( abonnementPrelevement.getCreatedBy());
                rtn.setIsDeleted( abonnementPrelevement.getIsDeleted());
                rtn.setDeletedBy( abonnementPrelevement.getDeletedBy());
                rtn.setUpdatedBy( abonnementPrelevement.getUpdatedBy());

                itemsDTO.add(rtn);
            }
            else if(modeAbonnement instanceof AbonnementPeriodique){
                AbonnementPeriodique abonnementPeriodique = new AbonnementPeriodique();
                abonnementPeriodique= (AbonnementPeriodique) modeAbonnement;
                ModeAbonnementDTO rtn = new ModeAbonnementDTO();

                rtn.setId( abonnementPeriodique.getId());
                rtn.setDesignation( abonnementPeriodique.getDesignation());
                rtn.setDescription( abonnementPeriodique.getDescription());

                rtn.setRedevance( abonnementPeriodique.getRedevance());
                rtn.setRedevancePublicite( abonnementPeriodique.getRedevancePublicite());
                rtn.setDateDebutAbonnement(abonnementPeriodique.getDateDebutAbonnement()!=null?abonnementPeriodique.getDateDebutAbonnement().toString():null);
                rtn.setDateFinAbonnement(abonnementPeriodique.getDateFinAbonnement()!=null?abonnementPeriodique.getDateFinAbonnement().toString():null);
                rtn.setPeriodiciteAbonnementDesignation(abonnementPeriodique.getPeriodiciteAbonnement().getDesignation());
                rtn.setCompagnieTransportRaisonSociale(abonnementPeriodique.getCompagnieTransport().getRaisonSociale());

                rtn.setDeletedAt( abonnementPeriodique.getDeletedAt()!=null?abonnementPeriodique.getDeletedAt().toString():null);
                rtn.setUpdatedAt( abonnementPeriodique.getUpdatedAt()!=null?abonnementPeriodique.getUpdatedAt().toString():null);
                rtn.setCreatedAt( abonnementPeriodique.getCreatedAt()!=null?abonnementPeriodique.getCreatedAt().toString():null);
                rtn.setCreatedBy( abonnementPeriodique.getCreatedBy());
                rtn.setIsDeleted( abonnementPeriodique.getIsDeleted());
                rtn.setDeletedBy( abonnementPeriodique.getDeletedBy());
                rtn.setUpdatedBy( abonnementPeriodique.getUpdatedBy());

                itemsDTO.add(rtn);
            }
            else{

            }
        }
        return itemsDTO;
    }
}