package com.africanb.africanb.Business.compagnie.ModePaiement;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementEnEspece;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.ModePaiement.ModePaiementEnEspeceRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementEnEspeceDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.ModePaiement.ModePaimentEnEspeceTransformer;
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
public class ModePaiementEnEspeceBusiness implements IBasicBusiness<Request<ModePaiementEnEspeceDTO>, Response<ModePaiementEnEspeceDTO>> {

    private Response<ModePaiementEnEspeceDTO> response;

    private final ModePaiementEnEspeceRepository modePaiementEnEspeceRepository;
    private final FunctionalError functionalError;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReferenceRepository typeModePaiementRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ModePaiementEnEspeceBusiness(ModePaiementEnEspeceRepository modePaiementEnEspeceRepository, FunctionalError functionalError, CompagnieTransportRepository compagnieTransportRepository, ReferenceRepository typeModePaiementRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.modePaiementEnEspeceRepository = modePaiementEnEspeceRepository;
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
    public Response<ModePaiementEnEspeceDTO> create(Request<ModePaiementEnEspeceDTO> request, Locale locale) throws ParseException {
        Response<ModePaiementEnEspeceDTO> response = new Response<ModePaiementEnEspeceDTO>();
        List<ModePaiementEnEspece> items = new ArrayList<ModePaiementEnEspece>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        for(ModePaiementEnEspeceDTO itemDto : request.getDatas() ){
            //Chech If CompagnieTransport exists
            CompagnieTransport exitingCompagnieTransport=null;
            exitingCompagnieTransport=compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(), false);
            if (exitingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("Compagnie de transport ayant la raison sociale"+itemDto.getCompagnieTransportRaisonSociale()+" n'existe pas", locale)) ;
                response.setHasError(true);
                return response;
            }
            //Check if periodiciteAbonnement exists
            Reference existingTypeModePaiement = null;
            existingTypeModePaiement = typeModePaiementRepository.findByDesignation(itemDto.getTypeModePaiementDesignation(),false);
            if (existingTypeModePaiement == null) {
                response.setStatus(functionalError.DATA_EXIST("Le mode de paiment n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            ModePaiementEnEspece entityToSave = ModePaimentEnEspeceTransformer.INSTANCE.toEntity(itemDto,exitingCompagnieTransport,existingTypeModePaiement);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            ModePaiementEnEspece entitySaved=null;
            entitySaved= modePaiementEnEspeceRepository.save(entityToSave);
            items.add(entitySaved);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementEnEspeceDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? ModePaimentEnEspeceTransformer.INSTANCE.toLiteDtos(items)
                                    : ModePaimentEnEspeceTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<ModePaiementEnEspeceDTO> update(Request<ModePaiementEnEspeceDTO> request, Locale locale) throws ParseException {
        Response<ModePaiementEnEspeceDTO> response = new Response<ModePaiementEnEspeceDTO>();
        List<ModePaiementEnEspece> items = new ArrayList<ModePaiementEnEspece>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementEnEspeceDTO>itemsDtos = Collections.synchronizedList(new ArrayList<ModePaiementEnEspeceDTO>());
        for(ModePaiementEnEspeceDTO dto: request.getDatas() ) {
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
        for(ModePaiementEnEspeceDTO dto: itemsDtos) {
            ModePaiementEnEspece entityToSave = modePaiementEnEspeceRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le mode de paiement suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            ModePaiementEnEspece entityUpdated=null;
            entityUpdated= modePaiementEnEspeceRepository.save(entityToSave);
            items.add(entityUpdated);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification, ",locale));
            response.setHasError(true);
            return response;
        }
        List<ModePaiementEnEspeceDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? ModePaimentEnEspeceTransformer.INSTANCE.toLiteDtos(items)
                                : ModePaimentEnEspeceTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update Jour semaine-----");
        return response;
    }

    @Override
    public Response<ModePaiementEnEspeceDTO> delete(Request<ModePaiementEnEspeceDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ModePaiementEnEspeceDTO> forceDelete(Request<ModePaiementEnEspeceDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ModePaiementEnEspeceDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ModePaiementEnEspeceDTO> getByCriteria(Request<ModePaiementEnEspeceDTO> request, Locale locale) {
        return null;
    }
}
