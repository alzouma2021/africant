package com.africanb.africanb.Business.compagnie;

import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModeAbonnement.AbonnementPrelevement;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.AbonnementPrelevementRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.ModeAbonnementRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.AbonnementPrelevementDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.AbonnementPrelevementTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Reference.Reference;
import lombok.extern.java.Log;
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
public class AbonnementPrelevementBusiness implements IBasicBusiness<Request<AbonnementPrelevementDTO>, Response<AbonnementPrelevementDTO>> {

    private Response<AbonnementPrelevementDTO> response;

    private final AbonnementPrelevementRepository abonnementPrelevementRepository;
    private final ModeAbonnementRepository modeAbonnementRepository;
    private final FunctionalError functionalError;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReferenceRepository periodiciteAbonnementRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public AbonnementPrelevementBusiness(AbonnementPrelevementRepository abonnementPrelevementRepository, ModeAbonnementRepository modeAbonnementRepository, FunctionalError functionalError, CompagnieTransportRepository compagnieTransportRepository, ReferenceRepository periodiciteAbonnementRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.abonnementPrelevementRepository = abonnementPrelevementRepository;
        this.modeAbonnementRepository = modeAbonnementRepository;
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
    public Response<AbonnementPrelevementDTO> create(Request<AbonnementPrelevementDTO> request, Locale locale) throws ParseException {
        Response<AbonnementPrelevementDTO> response = new Response<AbonnementPrelevementDTO>();
        List<AbonnementPrelevement> items = new ArrayList<AbonnementPrelevement>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(AbonnementPrelevementDTO itemDto : request.getDatas() ){
            //Chech If CompagnieTransport exists
            CompagnieTransport exitingCompagnieTransport=null;
            exitingCompagnieTransport=compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(), false);
            if (exitingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("Compagnie de transport ayant la raison sociale"+itemDto.getCompagnieTransportRaisonSociale()+" n'existe pas", locale)) ;
                response.setHasError(true);
                return response;
            }
            Reference existingPeriodiciteAbonnement = periodiciteAbonnementRepository.findByDesignation(itemDto.getPeriodiciteAbonnementDesignation(), false);
            if (existingPeriodiciteAbonnement == null) {
                response.setStatus(functionalError.DATA_EXIST("AbonnementPeriodique -> " + itemDto.getPeriodiciteAbonnementDesignation()+" n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            AbonnementPrelevement entityToSave = AbonnementPrelevementTransformer.INSTANCE.toEntity(itemDto,exitingCompagnieTransport,existingPeriodiciteAbonnement);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            AbonnementPrelevement entitySaved=null;
            entitySaved= abonnementPrelevementRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<AbonnementPrelevementDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? AbonnementPrelevementTransformer.INSTANCE.toLiteDtos(items)
                                    : AbonnementPrelevementTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<AbonnementPrelevementDTO> update(Request<AbonnementPrelevementDTO> request, Locale locale) throws ParseException {
        log.info("_115 debut de modification");
        Response<AbonnementPrelevementDTO> response = new Response<AbonnementPrelevementDTO>();
        List<AbonnementPrelevement> items = new ArrayList<AbonnementPrelevement>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        log.info("_123 Affichage de la variable ="+request.getDatas() .toString());
        List<AbonnementPrelevementDTO>itemsDtos = Collections.synchronizedList(new ArrayList<AbonnementPrelevementDTO>());
        for(AbonnementPrelevementDTO dto: request.getDatas() ) {
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
        for(AbonnementPrelevementDTO dto: itemsDtos) {
            //Check If AbonnenementPrelevelent exists
            AbonnementPrelevement entityToSave=null;
            entityToSave=abonnementPrelevementRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_EXIST("L'abonnement ayant l'identifiant"+dto.getId()+" n'existe pas", locale)) ;
                response.setHasError(true);
                return response;
            }
            //entityToSave= Utilities.transformerEntityModeAbonnementEnEntityAbonnementPrelevement(existingModeAbonnement);
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
            if(Utilities.isValidID(dto.getTaux()) && (dto.getTaux()!=entityToSave.getTaux())){
                entityToSave.setTaux(dto.getTaux());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            AbonnementPrelevement entityUpdated=null;
            entityUpdated= abonnementPrelevementRepository.save(entityToSave);
            items.add(entityUpdated);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification, ",locale));
            response.setHasError(true);
            return response;
        }
        List<AbonnementPrelevementDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? AbonnementPrelevementTransformer.INSTANCE.toLiteDtos(items)
                                : AbonnementPrelevementTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Jour semaine-----");
        return response;
    }

    @Override
    public Response<AbonnementPrelevementDTO> delete(Request<AbonnementPrelevementDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<AbonnementPrelevementDTO> forceDelete(Request<AbonnementPrelevementDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<AbonnementPrelevementDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<AbonnementPrelevementDTO> getByCriteria(Request<AbonnementPrelevementDTO> request, Locale locale) {
        return null;
    }

}
