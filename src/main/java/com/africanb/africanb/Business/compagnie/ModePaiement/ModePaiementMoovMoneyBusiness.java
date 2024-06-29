package com.africanb.africanb.Business.compagnie.ModePaiement;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMoovMoney;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.ModePaiement.ModePaiementMoovMoneyRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementMoovMoneyDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.ModePaiement.ModePaimentMoovMoneyTransformer;
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
public class ModePaiementMoovMoneyBusiness implements IBasicBusiness<Request<ModePaiementMoovMoneyDTO>, Response<ModePaiementMoovMoneyDTO>> {


    private final ModePaiementMoovMoneyRepository modePaiementMoovMoneyRepository;
    private final FunctionalError functionalError;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReferenceRepository typeModePaiementRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ModePaiementMoovMoneyBusiness(ModePaiementMoovMoneyRepository modePaiementMoovMoneyRepository, FunctionalError functionalError, CompagnieTransportRepository compagnieTransportRepository, ReferenceRepository typeModePaiementRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.modePaiementMoovMoneyRepository = modePaiementMoovMoneyRepository;
        this.functionalError = functionalError;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.typeModePaiementRepository = typeModePaiementRepository;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<ModePaiementMoovMoneyDTO> create(Request<ModePaiementMoovMoneyDTO> request, Locale locale) throws ParseException {
        Response<ModePaiementMoovMoneyDTO> response = new Response<>();
        List<ModePaiementMoovMoney> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementMoovMoneyDTO>itemsDtos = Collections.synchronizedList(new ArrayList<>());
        for(ModePaiementMoovMoneyDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("telephoneMoovMoney", dto.getTelephoneMoovMoney());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(ModePaiementMoovMoneyDTO itemDto : request.getDatas() ){
            CompagnieTransport exitingCompagnieTransport=compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(), false);
            if (exitingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("Compagnie de transport ayant la raison sociale"+itemDto.getCompagnieTransportRaisonSociale()+" n'existe pas", locale)) ;
                response.setHasError(true);
                return response;
            }
            Reference existingTypeModePaiement = typeModePaiementRepository.findByDesignation(itemDto.getTypeModePaiementDesignation(),false);
            if (existingTypeModePaiement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le mode de paiment n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ModePaiementMoovMoney entityToSave = ModePaimentMoovMoneyTransformer.INSTANCE.toEntity(itemDto,exitingCompagnieTransport,existingTypeModePaiement);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            ModePaiementMoovMoney entitySaved = modePaiementMoovMoneyRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementMoovMoneyDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ModePaimentMoovMoneyTransformer.INSTANCE.toLiteDtos(items)
                                    : ModePaimentMoovMoneyTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ModePaiementMoovMoneyDTO> update(Request<ModePaiementMoovMoneyDTO> request, Locale locale) throws ParseException {
        Response<ModePaiementMoovMoneyDTO> response = new Response<>();
        List<ModePaiementMoovMoney> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementMoovMoneyDTO> itemsDtos = Collections.synchronizedList(new ArrayList<>());
        for(ModePaiementMoovMoneyDTO dto: request.getDatas() ) {
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
        for(ModePaiementMoovMoneyDTO dto: itemsDtos) {
            ModePaiementMoovMoney entityToSave = modePaiementMoovMoneyRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le mode de paiement suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if(Utilities.isNotBlank(dto.getTelephoneMoovMoney()) && (dto.getTelephoneMoovMoney().equalsIgnoreCase(entityToSave.getTelephoneMoovMoney()))){
                entityToSave.setTelephoneMoovMoney(dto.getTelephoneMoovMoney());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            ModePaiementMoovMoney entityUpdated= modePaiementMoovMoneyRepository.save(entityToSave);
            items.add(entityUpdated);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification, ",locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementMoovMoneyDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? ModePaimentMoovMoneyTransformer.INSTANCE.toLiteDtos(items)
                                : ModePaimentMoovMoneyTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Jour semaine-----");
        return response;
    }

    @Override
    public Response<ModePaiementMoovMoneyDTO> delete(Request<ModePaiementMoovMoneyDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ModePaiementMoovMoneyDTO> forceDelete(Request<ModePaiementMoovMoneyDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ModePaiementMoovMoneyDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ModePaiementMoovMoneyDTO> getByCriteria(Request<ModePaiementMoovMoneyDTO> request, Locale locale) {
        return null;
    }
}
