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
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Log
@Component
public class AbonnementPeriodiqueBusiness implements IBasicBusiness<Request<AbonnementPeriodiqueDTO>, Response<AbonnementPeriodiqueDTO>> {


    private Response<AbonnementPeriodiqueDTO> response;

    private final AbonnementPeriodiqueRepository abonnementPeriodiqueRepository;
    private final FunctionalError functionalError;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReferenceRepository periodiciteAbonnementRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public AbonnementPeriodiqueBusiness(AbonnementPeriodiqueRepository abonnementPeriodiqueRepository, FunctionalError functionalError, CompagnieTransportRepository compagnieTransportRepository, ReferenceRepository periodiciteAbonnementRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.abonnementPeriodiqueRepository = abonnementPeriodiqueRepository;
        this.functionalError = functionalError;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.periodiciteAbonnementRepository = periodiciteAbonnementRepository;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<AbonnementPeriodiqueDTO> create(Request<AbonnementPeriodiqueDTO> request, Locale locale) throws ParseException {
        Response<AbonnementPeriodiqueDTO> response = new Response<>();
        List<AbonnementPeriodique> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(AbonnementPeriodiqueDTO itemDto : request.getDatas() ){
            CompagnieTransport exitingCompagnieTransport=compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(), false);
            if (exitingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("Compagnie de transport ayant la raison sociale"+itemDto.getCompagnieTransportRaisonSociale()+" n'existe pas", locale)) ;
                response.setHasError(true);
                return response;
            }
            Reference existingPeriodiciteAbonnement = periodiciteAbonnementRepository.findByDesignation(itemDto.getPeriodiciteAbonnementDesignation(),false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("La periodicite abonnement n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            AbonnementPeriodique entityToSave = AbonnementPeriodiqueTransformer.INSTANCE.toEntity(itemDto,exitingCompagnieTransport,existingPeriodiciteAbonnement);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            AbonnementPeriodique entitySaved = abonnementPeriodiqueRepository.save(entityToSave);
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
        Response<AbonnementPeriodiqueDTO> response = new Response<>();
        List<AbonnementPeriodique> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<AbonnementPeriodiqueDTO>itemsDtos = Collections.synchronizedList(new ArrayList<>());
        for(AbonnementPeriodiqueDTO dto: request.getDatas() ) {
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
            AbonnementPeriodique entityUpdated = abonnementPeriodiqueRepository.save(entityToSave);
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
        return null;
    }
}
