package com.africanb.africanb.Business.compagnie;

import com.africanb.africanb.Business.design.factory.modeAbonnement.ModeAbonnementDTOCreator;
import com.africanb.africanb.Business.design.factory.modeAbonnement.ModeAbonnementEntityCreator;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
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

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


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
        Response<ModeAbonnementDTO> response = new Response<>();
        List<ModeAbonnementDTO> itemsDto= new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ModeAbonnementDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(ModeAbonnementDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<>();
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
            CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingPeriodiciteAbonnement = referenceRepository.findByDesignation(itemDto.getPeriodiciteAbonnementDesignation(),false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("La periodicite de l'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeModeAbonnement = referenceRepository.findByDesignation(itemDto.getTypeModeAbonnementDesignation(),false);
            if (existingTypeModeAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de mode d'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            List<ModeAbonnement> exitingModeAbonnementList = modeAbonnementRepository.findByCompagnieTransport(itemDto.getCompagnieTransportRaisonSociale(),false);
            if(!CollectionUtils.isEmpty(exitingModeAbonnementList)){
                response.setStatus(functionalError.SAVE_FAIL("La compagnie possede deja un mode d'abonnement", locale));
                response.setHasError(true);
                return response;
            }
            itemDto=Utilities.transformerLaClasseModeAbonnementEnClasseFilleCorrespondante(itemDto);
            ModeAbonnementDTO entitySaved = saveModeAbonnement(itemDto,locale);
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
        Response<ModeAbonnementDTO> response = new Response<>();
        List<ModeAbonnementDTO> itemsDto= new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ModeAbonnementDTO>itemsDtos = Collections.synchronizedList(new ArrayList<>());
        for(ModeAbonnementDTO dto: request.getDatas() ) {
            if(dto!=null){
                Map<String, Object> fieldsToVerify = new HashMap<>();
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
            CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingPeriodiciteAbonnement = referenceRepository.findByDesignation(itemDto.getPeriodiciteAbonnementDesignation(),false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("La periodicite de l'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeModeAbonnement = referenceRepository.findByDesignation(itemDto.getTypeModeAbonnementDesignation(),false);
            if (existingTypeModeAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le type de mode d'abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            itemDto=Utilities.transformerLaClasseModeAbonnementEnClasseFilleCorrespondante(itemDto);
            ModeAbonnementDTO entitySaved= updateModeAbonnement(itemDto,locale);
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
    public Response<ModeAbonnementDTO> getModeAbonnementByCompagnieTransport(Request<ModeAbonnementDTO> request, Locale locale) {
        Response<ModeAbonnementDTO> response = new Response<>();
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
        List<ModeAbonnement> items = modeAbonnementRepository.findByCompagnieTransport(compagnieTransportRaisonScoiale,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport ne dispose d'aucun mode d'abonnement", locale));
            response.setHasError(true);
            return response;
        }
        List<ModeAbonnementDTO> itemsDto = buildModeAbonnementDTOFromEntity(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    public ModeAbonnementDTO saveModeAbonnement(ModeAbonnementDTO modeAbonnementDTO, Locale locale) throws ParseException {
        if(modeAbonnementDTO instanceof AbonnementPeriodiqueDTO abonnementPeriodiqueDTO){
            Request<AbonnementPeriodiqueDTO> subRequest = new Request<>();
            List<AbonnementPeriodiqueDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(abonnementPeriodiqueDTO);
            subRequest.setDatas( itemsDTO);
            Response<AbonnementPeriodiqueDTO> subResponse = abonnementPeriodiqueBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new ModeAbonnementDTO();
            }
            return ModeAbonnementDTOCreator.createModeAbonnementDTO(subResponse.getItems().get(0));
        }
        else if(modeAbonnementDTO instanceof AbonnementPrelevementDTO abonnementPrelevementDTO){
            Request<AbonnementPrelevementDTO> subRequest = new Request<>();
            List<AbonnementPrelevementDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
            itemsDTO.add(abonnementPrelevementDTO);
            subRequest.setDatas( itemsDTO);
            Response<AbonnementPrelevementDTO> subResponse = abonnementPrelevementBusiness.create(subRequest,locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return new AbonnementPrelevementDTO();
            }
            return ModeAbonnementDTOCreator.createModeAbonnementDTO(subResponse.getItems().get(0));
        }
        return new ModeAbonnementDTO();
    }


    public ModeAbonnementDTO updateModeAbonnement(ModeAbonnementDTO modeAbonnementDTO, Locale locale) throws ParseException {
      if(modeAbonnementDTO != null){
          if(modeAbonnementDTO.getTypeModeAbonnementDesignation()!= null
                  && modeAbonnementDTO.getTypeModeAbonnementDesignation().equals(ProjectConstants.REF_ELEMENT_ABONNEMENT_PERIODIQUE)){
              Request<AbonnementPeriodiqueDTO> subRequest = new Request<>();
              List<AbonnementPeriodiqueDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
              AbonnementPeriodiqueDTO abonnementPeriodiqueDTO = (AbonnementPeriodiqueDTO) modeAbonnementDTO;
              itemsDTO.add(abonnementPeriodiqueDTO);
              subRequest.setDatas( itemsDTO);
              Response<AbonnementPeriodiqueDTO> subResponse = abonnementPeriodiqueBusiness.update(subRequest,locale);
              if (subResponse.isHasError()) {
                  response.setStatus(subResponse.getStatus());
                  response.setHasError(Boolean.TRUE);
                  return new ModeAbonnementDTO();
              }
              return ModeAbonnementDTOCreator.createModeAbonnementDTO(subResponse.getItems().get(0));
          }
          else if(modeAbonnementDTO.getTypeModeAbonnementDesignation()!= null
                  && modeAbonnementDTO.getTypeModeAbonnementDesignation().equals(ProjectConstants.REF_ELEMENT_ABONNEMENT_PRELEVEMENT)){
              Request<AbonnementPrelevementDTO> subRequest = new Request<>();
              List<AbonnementPrelevementDTO> itemsDTO = Collections.synchronizedList(new ArrayList<>());
              AbonnementPrelevementDTO abonnementPrelevementDTO = (AbonnementPrelevementDTO) modeAbonnementDTO;
              itemsDTO.add(abonnementPrelevementDTO);
              subRequest.setDatas(itemsDTO);
              Response<AbonnementPrelevementDTO> subResponse = abonnementPrelevementBusiness.update(subRequest,locale);
              if (subResponse.isHasError()) {
                  response.setStatus(subResponse.getStatus());
                  response.setHasError(Boolean.TRUE);
                  return new AbonnementPrelevementDTO();
              }
              return ModeAbonnementDTOCreator.createModeAbonnementDTO(subResponse.getItems().get(0));
          }
      }
      return new ModeAbonnementDTO();
    }

    public  List<ModeAbonnementDTO> buildModeAbonnementDTOFromEntity(List<ModeAbonnement> modeAbonnementList) {
        return modeAbonnementList.stream()
                .filter(Objects::nonNull)
                .map(modeAbonnement -> ModeAbonnementEntityCreator.createModeAbonnementDTO(modeAbonnement))
                .collect(Collectors.toList());
    }
}