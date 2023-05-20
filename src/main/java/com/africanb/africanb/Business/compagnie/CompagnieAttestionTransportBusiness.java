package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.CompagnieAttestionTransport;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.StatusUtilCompagnieTransport;
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
import com.africanb.africanb.helper.dto.compagnie.StatusUtilCompagnieTransportDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.CompagnieAttestionTransportTransformer;
import com.africanb.africanb.helper.transformer.compagnie.StatusUtilCompagnieTransportTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
@Component
public class CompagnieAttestionTransportBusiness implements IBasicBusiness<Request<CompagnieAttestionTransportDTO>, Response<CompagnieAttestionTransportDTO>> {

    private Response<CompagnieAttestionTransportDTO> response;
    @Autowired
    private CompagnieAttestationTransportRepository compagnieAttestionTransportRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private CompagnieTransportRepository compagnieTransportRepository;

    @Autowired
    private FunctionalError functionalError;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;

    @Autowired
    private EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public CompagnieAttestionTransportBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }
    
    @Override
    public Response<CompagnieAttestionTransportDTO> create(Request<CompagnieAttestionTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieAttestionTransportDTO> response = new Response<CompagnieAttestionTransportDTO>();
        List<CompagnieAttestionTransport> items = new ArrayList<CompagnieAttestionTransport>();
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieAttestionTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<CompagnieAttestionTransportDTO>());
        for(CompagnieAttestionTransportDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
            CompagnieTransport existingCompagnieTransport = null;
            existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(dto.getCompagnieRaisonSociale(), false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("CompagnieTransport Raison sociale -> " + dto.getCompagnieRaisonSociale() +"n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Document existingDocument = null;
            existingDocument = documentRepository.findByDesignation(dto.getDocumentDesignation(), false);
            if (existingDocument == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Document -> " + dto.getDocumentDesignation() +"n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieAttestionTransport existingEntity = null;
            existingEntity = compagnieAttestionTransportRepository.findByRaisonSociale(dto.getCompagnieRaisonSociale(),false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport dispose déjà d'une attestionde transport", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieAttestionTransport entityToSave = CompagnieAttestionTransportTransformer
                                        .INSTANCE.toEntity(dto,existingCompagnieTransport,existingDocument);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user);
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieAttestionTransport> itemsSaved = null;
        log.info("_130 Affichage CompagnieAttestionTransport ="+ items.toString());
        itemsSaved = compagnieAttestionTransportRepository.saveAll((Iterable<CompagnieAttestionTransport>) items);
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
        /*Response<CompagnieAttestionTransportDTO> response = new Response<CompagnieAttestionTransportDTO>();
        List<CompagnieAttestionTransport> items = new ArrayList<CompagnieAttestionTransport>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieAttestionTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<CompagnieAttestionTransportDTO>());
        for(CompagnieAttestionTransportDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de StatusUtilCompagnieTransport '" + dto.getCompagnieTransportId() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(StatusUtilCompagnieTransportDTO dto : itemsDtos){
            StatusUtilCompagnieTransport entityToSave = null;
            entityToSave = compagnieAttestionTransportRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilCompagnieTransport id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            CompagnieTransport existingCompagnieTransport = null;
            if (Utilities.isValidID(dto.getCompagnieTransportId()) && !entityToSave.getCompagnieTransport().getId().equals(dto.getCompagnieTransportId())) {
                existingCompagnieTransport = compagnieTransportRepository.findOne(dto.getCompagnieTransportId(), false);
                if (existingCompagnieTransport == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("CompagnieTRansport CompagnieTransportId -> " + dto.getCompagnieTransportId(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setCompagnieTransport(existingCompagnieTransport);
            }
            StatusUtil existingStatusUtil = null;
            if (Utilities.isValidID(dto.getStatusUtilId()) && !entityToSave.getStatusUtil().getId().equals(dto.getStatusUtilId())) {
                existingStatusUtil = documentRepository.findOne(dto.getStatusUtilId(), false);
                if (existingStatusUtil == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtil StatusUtilId -> " + dto.getStatusUtilId(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setStatusUtil(existingStatusUtil);
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            items.add(entityToSave);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification ",locale));
            response.setHasError(true);
        }
        List<StatusUtilCompagnieTransport> itemsSaved = null;
        itemsSaved = compagnieAttestionTransportRepository.saveAll((Iterable<StatusUtilCompagnieTransport>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
                response.setStatus(functionalError.SAVE_FAIL("StatusUtilCompagnieTransport", locale));
                response.setHasError(true);
                return response;
        }
        //Transformation
        List<StatusUtilCompagnieTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? StatusUtilCompagnieTransportTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : StatusUtilCompagnieTransportTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;*/
        return null;
    }

    @Override
    public Response<CompagnieAttestionTransportDTO> delete(Request<CompagnieAttestionTransportDTO> request, Locale locale) {
        /*Response<CompagnieAttestionTransportDTO> response = new Response<StatusUtilCompagnieTransportDTO>();
        List<StatusUtilCompagnieTransport> items = new ArrayList<StatusUtilCompagnieTransport>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        for(StatusUtilCompagnieTransportDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        for(StatusUtilCompagnieTransportDTO dto : request.getDatas()){
            StatusUtilCompagnieTransport existingEntity = null;
            existingEntity = compagnieAttestionTransportRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilCompagnieTransport id -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            //existingEntity.setDeletedBy(request.user);
            items.add(existingEntity);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;*/
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

    /*    log.info("----begin get HistoriqueDemande-----");

        Response<StatusUtilCompagnieTransportDTO> response = new Response<StatusUtilCompagnieTransportDTO>();

        //verification si le parametre d'ordre à été fourni, sinon nous mettons le paramètre à vide
        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }

        //verification si le parametre direction à été fourni, sinon nous mettons le paramètre ascendant( du plus ancien au plus ressent)
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }

        //recuperation des entités en base
        List<HistoriqueDemande> items = statusUtilCompagnieTransportRepository.getByCriteria(request, em, locale);

        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("HistoriqueDemande", locale));
            response.setHasError(false);
            return response;
        }

        //Transformation
        List<HistoriqueDemandeDto> itemsDto = HistoriqueDemandeTransformer.INSTANCE.toDtos(items);

        //Envoie de la reponse
        response.setItems(itemsDto);
        response.setCount(statusUtilCompagnieTransportRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        log.info("----end get HistoriqueDemande-----");

        return response;
        */
        return null;
    }
}
