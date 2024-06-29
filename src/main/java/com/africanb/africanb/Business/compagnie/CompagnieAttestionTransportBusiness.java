package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.CompagnieAttestionTransport;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.dao.repository.compagnie.CompagnieAttestationTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.document.DocumentRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.CompagnieAttestionTransportDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.CompagnieAttestionTransportTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
@Component
public class CompagnieAttestionTransportBusiness implements IBasicBusiness<Request<CompagnieAttestionTransportDTO>, Response<CompagnieAttestionTransportDTO>> {

    private Response<CompagnieAttestionTransportDTO> response;

    private final CompagnieAttestationTransportRepository compagnieAttestionTransportRepository;
    private final DocumentRepository documentRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public CompagnieAttestionTransportBusiness(CompagnieAttestationTransportRepository compagnieAttestionTransportRepository, DocumentRepository documentRepository, CompagnieTransportRepository compagnieTransportRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.compagnieAttestionTransportRepository = compagnieAttestionTransportRepository;
        this.documentRepository = documentRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    @Override
    public Response<CompagnieAttestionTransportDTO> create(Request<CompagnieAttestionTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieAttestionTransportDTO> response = new Response<>();
        List<CompagnieAttestionTransport> items = new ArrayList<>();
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieAttestionTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(CompagnieAttestionTransportDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("compagnieRaisonSociale", dto.getCompagnieRaisonSociale());
            fieldsToVerify.put("documentDesignation", dto.getDocumentDesignation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getCompagnieRaisonSociale().equalsIgnoreCase(dto.getCompagnieRaisonSociale()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de compagnie '" + dto.getCompagnieRaisonSociale() , locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDocumentDesignation().equalsIgnoreCase(dto.getDocumentDesignation()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de compagnie '" + dto.getDocumentDesignation() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(CompagnieAttestionTransportDTO dto : itemsDtos){
            CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(dto.getCompagnieRaisonSociale(), false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("CompagnieTransport Raison sociale -> " + dto.getCompagnieRaisonSociale() +"n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Document existingDocument = documentRepository.findByDesignation(dto.getDocumentDesignation(), false);
            if (existingDocument == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Document -> " + dto.getDocumentDesignation() +"n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieAttestionTransport existingEntity = compagnieAttestionTransportRepository.findByRaisonSociale(dto.getCompagnieRaisonSociale(),false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport dispose déjà d'une attestionde transport", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieAttestionTransport entityToSave = CompagnieAttestionTransportTransformer
                                        .INSTANCE.toEntity(dto,existingCompagnieTransport,existingDocument);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieAttestionTransport> itemsSaved = compagnieAttestionTransportRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("CompagnieAttestionTransport", locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieAttestionTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? CompagnieAttestionTransportTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                : CompagnieAttestionTransportTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<CompagnieAttestionTransportDTO> update(Request<CompagnieAttestionTransportDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<CompagnieAttestionTransportDTO> delete(Request<CompagnieAttestionTransportDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<CompagnieAttestionTransportDTO> forceDelete(Request<CompagnieAttestionTransportDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<CompagnieAttestionTransportDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<CompagnieAttestionTransportDTO> getByCriteria(Request<CompagnieAttestionTransportDTO> request, Locale locale) {
        return null;
    }
}
