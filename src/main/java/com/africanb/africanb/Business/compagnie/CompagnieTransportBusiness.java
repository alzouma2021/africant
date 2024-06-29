
package com.africanb.africanb.Business.compagnie;

import com.africanb.africanb.Business.document.DocumentBusiness;
import com.africanb.africanb.dao.entity.compagnie.CompagnieAttestionTransport;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.dao.repository.compagnie.*;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.CompagnieAttestionTransportDTO;
import com.africanb.africanb.helper.dto.compagnie.CompagnieTransportDTO;
import com.africanb.africanb.helper.dto.compagnie.StatusUtilCompagnieTransportDTO;
import com.africanb.africanb.helper.dto.document.DocumentDTO;
import com.africanb.africanb.helper.dto.document.DocumentReponseDTO;
import com.africanb.africanb.helper.transformer.compagnie.CompagnieTransportTransformer;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import com.africanb.africanb.utils.document.DocumentUtils;
import com.africanb.africanb.utils.emailService.EmailUtils;
import com.africanb.africanb.utils.emailService.EmailDTO;
import com.africanb.africanb.utils.emailService.EmailServiceBusiness;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Log
@Component
public class CompagnieTransportBusiness implements IBasicBusiness<Request<CompagnieTransportDTO>, Response<CompagnieTransportDTO>> {

    @Value("${africanb.attestionTransport.path}")
    private String documentpath;
    @Value("${taille.limite.attestation.transport}")
    private String limitFileSizeDefault;
    @Value("${racine.document.path}")
    private String racineDocumentPath;

    private final StatusUtilRepository statusUtilRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final VilleRepository villeRepository;
    private final DocumentBusiness documentBusiness;
    private final CompagnieAttestionTransportBusiness compagnieAttestionTransportBusiness;
    private final CompagnieAttestationTransportRepository compagnieAttestationTransportRepository;
    private final FunctionalError functionalError;
    private final EmailServiceBusiness emailServiceBusiness;
    private final StatusUtilCompagnieTransportBusiness statusUtilCompagnieTransportBusiness;

    private final SimpleDateFormat dateFormat;

    public CompagnieTransportBusiness(StatusUtilRepository statusUtilRepository, CompagnieTransportRepository compagnieTransportRepository, VilleRepository villeRepository, DocumentBusiness documentBusiness, CompagnieAttestionTransportBusiness compagnieAttestionTransportBusiness, CompagnieAttestationTransportRepository compagnieAttestationTransportRepository, FunctionalError functionalError, EmailServiceBusiness emailServiceBusiness, StatusUtilCompagnieTransportBusiness statusUtilCompagnieTransportBusiness) {
        this.statusUtilRepository = statusUtilRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.villeRepository = villeRepository;
        this.documentBusiness = documentBusiness;
        this.compagnieAttestionTransportBusiness = compagnieAttestionTransportBusiness;
        this.compagnieAttestationTransportRepository = compagnieAttestationTransportRepository;
        this.functionalError = functionalError;
        this.emailServiceBusiness = emailServiceBusiness;
        this.statusUtilCompagnieTransportBusiness = statusUtilCompagnieTransportBusiness;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    public Response<CompagnieTransportDTO> create(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<>();
        List<CompagnieTransport> items = new ArrayList<>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for (CompagnieTransportDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("raisonSociale", dto.getRaisonSociale());
            fieldsToVerify.put("telephone", dto.getTelephone());
            fieldsToVerify.put("sigle", dto.getSigle());
            fieldsToVerify.put("email", dto.getEmail());
            fieldsToVerify.put("villeDesignation", dto.getVilleDesignation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de compagnie '" + dto.getId() , locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getEmail().equalsIgnoreCase(dto.getEmail()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de compagnie '" + dto.getEmail() , locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de compagnie '" + dto.getDesignation() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(CompagnieTransportDTO dto: itemsDtos) {
            CompagnieTransport existingEntity=compagnieTransportRepository.findByDesignation(dto.getVilleDesignation(),false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("CompagnieTransport  -> " + dto.getVilleDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVille=villeRepository.findByDesignation(dto.getVilleDesignation(),false);
            if (existingVille == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Ville  -> " + dto.getVilleDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            StatusUtil existingStatusUtilActual=statusUtilRepository.findByDesignation(ProjectConstants.COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT,false);
            if (existingStatusUtilActual == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilActual  -> " + ProjectConstants.COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT, locale));
                response.setHasError(true);
                return response;
            }
            dto.setStatusUtilActualId(existingStatusUtilActual.getId());
            dto.setStatusUtilActualDesignation(existingStatusUtilActual.getDesignation());
            CompagnieTransport entityToSave = CompagnieTransportTransformer.INSTANCE.toEntity(dto,existingVille,existingStatusUtilActual);
            entityToSave.setIsDeleted(false);
            entityToSave.setIsActif(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            CompagnieTransport entitySaved = compagnieTransportRepository.save(entityToSave);
            if(entitySaved==null){
                response.setStatus(functionalError.SAVE_FAIL("Erreur creation",locale));
                response.setHasError(true);
                return response;
            }
            List<StatusUtilCompagnieTransportDTO> itemsDatas =  Collections.synchronizedList(new ArrayList<>());
            StatusUtilCompagnieTransportDTO statusUtilCompagnieTransportDTO= new StatusUtilCompagnieTransportDTO();
            statusUtilCompagnieTransportDTO.setStatusUtilId(existingStatusUtilActual.getId());
            statusUtilCompagnieTransportDTO.setCompagnieTransportId(entitySaved.getId());
            itemsDatas.add(statusUtilCompagnieTransportDTO);
            Request<StatusUtilCompagnieTransportDTO> subRequest = new Request<>();
            subRequest.setDatas(itemsDatas);
            Response<StatusUtilCompagnieTransportDTO> subResponse = statusUtilCompagnieTransportBusiness.create(subRequest, locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return response;
            }

            Runnable runnable = () -> {
                EmailDTO emailDTO = new EmailDTO();
                Request<EmailDTO> subRequestEmail = new Request<>();
                emailDTO.setSubject("Creation de compagnie de transport");
                emailDTO.setMessage(EmailUtils.bodyHtmlMailCreateCompagny());
                emailDTO.setToAddress(entitySaved.getEmail());
                subRequestEmail.setData(emailDTO);
                emailServiceBusiness.sendSimpleEmail(subRequestEmail,locale);
            };
            runnable.run();

            items.add(entitySaved);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de creation ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? CompagnieTransportTransformer.INSTANCE.toLiteDtos(items)
                : CompagnieTransportTransformer.INSTANCE.toDtos(items);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<CompagnieTransportDTO> update(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<>();
        List<CompagnieTransport> items = new ArrayList<>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for (CompagnieTransportDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getId().equals(dto.getId()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de compagnie '" + dto.getDesignation() , locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for (CompagnieTransportDTO dto :itemsDtos) {
            CompagnieTransport entityToSave = compagnieTransportRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("CompagnieTransport CompagnieTransportID -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                CompagnieTransport existingEntity = compagnieTransportRepository.findByDesignation(dto.getDesignation(), false);
                if (existingEntity != null && !existingEntity.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("CompagnieTransport -> " + dto.getId(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            if (Utilities.isValidID(dto.getVilleId()) && !dto.getVilleId().equals(entityToSave.getVille().getId())) {
                Ville existingVille=villeRepository.findOne(dto.getVilleId(),false);
                if (existingVille ==null ) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Ville  -> " + dto.getVilleId(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setVille(existingVille);
            }
            if (dto.getUpdatedBy() != null && dto.getUpdatedBy() > 0) {
                entityToSave.setUpdatedBy(dto.getUpdatedBy());
            }
            if (dto.getCreatedBy() != null && dto.getCreatedBy() > 0) {
                entityToSave.setCreatedBy(dto.getCreatedBy());
            }
            if (Utilities.isNotBlank(dto.getDeletedAt())) {
                entityToSave.setDeletedAt(dateFormat.parse(dto.getDeletedAt()));
            }
            if (dto.getDeletedBy() != null && dto.getDeletedBy() > 0) {
                entityToSave.setDeletedBy(dto.getDeletedBy());
            }
            if(Utilities.isNotBlank(dto.getTelephone()) && !dto.getTelephone().equals(entityToSave.getTelephone())){
                entityToSave.setTelephone(dto.getTelephone());
            }
            if(Utilities.isNotBlank(dto.getRaisonSociale()) && !dto.getRaisonSociale().equals(entityToSave.getRaisonSociale())){
                entityToSave.setRaisonSociale(dto.getRaisonSociale());
            }
            if(Utilities.isNotBlank(dto.getEmail()) && !dto.getEmail().equals(entityToSave.getEmail())){
                entityToSave.setEmail(dto.getEmail());
            }
            if(Utilities.isNotBlank(dto.getSigle()) && !dto.getSigle().equals(entityToSave.getSigle())){
                entityToSave.setSigle(dto.getSigle());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Erreur de modification ",locale));
            response.setHasError(true);
        }
        List<CompagnieTransport> itemsSaved = compagnieTransportRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("CompagnieTransport", locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? CompagnieTransportTransformer.INSTANCE.toLiteDtos(itemsSaved)
                : CompagnieTransportTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<CompagnieTransportDTO> delete(Request<CompagnieTransportDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<CompagnieTransportDTO> forceDelete(Request<CompagnieTransportDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<CompagnieTransportDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<CompagnieTransportDTO> getByCriteria(Request<CompagnieTransportDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<CompagnieTransportDTO> getDemandeCompagniesEnCours(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<>();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("size",request.getSize());
        fieldsToVerify.put("index",request.getIndex());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Long count=compagnieTransportRepository.countAllProcessingCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT,false);
        List<CompagnieTransport> items=compagnieTransportRepository.getAllProcessingCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT,false, PageRequest.of(request.getIndex(), request.getSize()));
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune compagnie de transport en de traiement",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                             ? CompagnieTransportTransformer.INSTANCE.toLiteDtos(items)
                             : CompagnieTransportTransformer.INSTANCE.toDtos(items);
        response.setCount(count);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<CompagnieTransportDTO> validateAdhesionRequestCompagny(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<>();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        if(request.getData() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie ",locale));
            response.setHasError(true);
            return response;
        }
        fieldsToVerify.put("id",request.getData().getId());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        CompagnieTransport existingEntity=compagnieTransportRepository.findOne(request.getData().getId(),false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        StatusUtil existingStatusUtilActual=statusUtilRepository.findByDesignation(ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE,false);
        if (existingStatusUtilActual == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilActual  -> " + ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE, locale));
            response.setHasError(true);
            return response;
        }
        existingEntity.setStatusUtilActual(existingStatusUtilActual);
        existingEntity.setIsValidate(true);
        existingEntity.setUpdatedAt(Utilities.getCurrentDate());
        CompagnieTransport entityUpdate = compagnieTransportRepository.save(existingEntity);
        if (entityUpdate == null || !entityUpdate.getIsValidate()) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'a pas été validée", locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilCompagnieTransportDTO> itemsDatas =  Collections.synchronizedList(new ArrayList<>());
        StatusUtilCompagnieTransportDTO statusUtilCompagnieTransportDTO=new StatusUtilCompagnieTransportDTO();
        statusUtilCompagnieTransportDTO.setStatusUtilId(existingStatusUtilActual.getId());
        statusUtilCompagnieTransportDTO.setCompagnieTransportId(entityUpdate.getId());
        itemsDatas.add(statusUtilCompagnieTransportDTO);
        Request<StatusUtilCompagnieTransportDTO> subRequest = new Request<>();
        subRequest.setDatas(itemsDatas);
        Response<StatusUtilCompagnieTransportDTO> subResponse = statusUtilCompagnieTransportBusiness.create(subRequest, locale);
        if (subResponse.isHasError()) {
            response.setStatus(subResponse.getStatus());
            response.setHasError(Boolean.TRUE);
            return response;
        }

        Runnable runnable = () -> {
            EmailDTO emailDTO = new EmailDTO();
            Request<EmailDTO> subRequestEmail = new Request<>();
            emailDTO.setSubject("Validation de compagnie de transport");
            emailDTO.setMessage(EmailUtils.bodyHtmlMailValidationCompagny());
            emailDTO.setToAddress(entityUpdate.getEmail());
            subRequestEmail.setData(emailDTO);
            emailServiceBusiness.sendSimpleEmail(subRequestEmail,locale);
        };
        runnable.run();

        CompagnieTransportDTO itemDto = CompagnieTransportTransformer.INSTANCE.toDto(entityUpdate);
        response.setItem(itemDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Compgnaie de transport-----");
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<CompagnieTransportDTO> getAllValidedCompagnies(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<>();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("size",request.getSize());
        fieldsToVerify.put("index",request.getIndex());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Long count = compagnieTransportRepository.countAllValidedCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE,false);
        List<CompagnieTransport> items=compagnieTransportRepository.getAllValidedCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE,false, PageRequest.of(request.getIndex(), request.getSize()));
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune compagnie de transport valide n'est disponible",locale));
            response.setHasError(true);
            return response;
        }

        List<CompagnieTransportDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? CompagnieTransportTransformer.INSTANCE.toLiteDtos(items)
                : CompagnieTransportTransformer.INSTANCE.toDtos(items);

        response.setCount(count);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class,IllegalArgumentException.class, IOException.class})
    public  Response<DocumentDTO> uploadAttestionTransport(Request<CompagnieTransportDTO> request, MultipartFile file, Locale locale) throws ParseException, IOException {
        Response<DocumentDTO> response = new Response<>();
        if(request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie ",locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("compagnieRaisonSociale",request.getData().getRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        CompagnieTransport existingEntity=compagnieTransportRepository.findByRaisonSociale(request.getData().getRaisonSociale(),false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        if(file.isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Fichier vide ",locale));
            response.setHasError(true);
            return response;
        }
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if( extension != null && !extension.equalsIgnoreCase("PDF")){
            response.setStatus(functionalError.DATA_NOT_EXIST("Le document doit être au format PDF ",locale));
            response.setHasError(true);
            return response;
        }
        Double limitSize = Double.parseDouble(limitFileSizeDefault);
        boolean compareFileSizeToLimitSize= DocumentUtils.compareFileSizeToLimitSize(file,limitSize);
        if(!compareFileSizeToLimitSize){
            response.setStatus(functionalError.SAVE_FAIL("La taille du document ne doit pas depasser 1 Mo",locale));
            response.setHasError(true);
            return response;
        }
        byte[] content = file.getBytes();
        String contentType = file.getContentType();
        String filename = UUID.randomUUID().toString();
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDesignation(request.getData().getRaisonSociale());
        documentDTO.setPath(filename);
        documentDTO.setTypeMime(contentType);
        documentDTO.setExtension(extension);
        boolean checkIfDirectoryExists = DocumentUtils.checkIfDirectoryExists(documentpath);
        if(!checkIfDirectoryExists){
            boolean createDirectoryOnHardDisk = DocumentUtils.createDirectoryOnHardDisk(documentpath);
            if(!createDirectoryOnHardDisk){
                response.setStatus(functionalError.SAVE_FAIL("Erreur de creation :: repertoire inexistant",locale));
                response.setHasError(true);
                return response;
            }
        }
        String fileLocation = documentpath + filename + "." + extension;
        boolean createFileOnDiskHard=DocumentUtils.createFileOnDiskHard(content, fileLocation);
        if(!createFileOnDiskHard){
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation du fichier",locale));
            response.setHasError(true);
            return response;
        }
        Request<DocumentDTO> subRequest = new Request<>();
        subRequest.setData(documentDTO);
        Response<DocumentDTO> subResponse = documentBusiness.create(subRequest, locale);
        if (subResponse.isHasError()) {
            response.setStatus(subResponse.getStatus());
            response.setHasError(Boolean.TRUE);
            return response;
        }
        List<CompagnieAttestionTransportDTO> itemsDTO = new ArrayList<>();
        CompagnieAttestionTransportDTO compagnieAttestionTransportDTO = new CompagnieAttestionTransportDTO();
        compagnieAttestionTransportDTO.setCompagnieRaisonSociale(request.getData().getRaisonSociale());
        compagnieAttestionTransportDTO.setDocumentDesignation(subResponse.getItem().getDesignation());
        itemsDTO.add(compagnieAttestionTransportDTO);
        Request<CompagnieAttestionTransportDTO> subRequest1 = new Request<>();
        subRequest1.setDatas(itemsDTO);
        Response<CompagnieAttestionTransportDTO> subResponse1 = compagnieAttestionTransportBusiness.create(subRequest1, locale);
        if (subResponse1.isHasError()) {
            response.setStatus(subResponse1.getStatus());
            response.setHasError(Boolean.TRUE);
            return response;
        }
        response.setItem(subResponse.getItem());
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class,IllegalArgumentException.class, IOException.class})
    public  Response<DocumentReponseDTO> getLinkedAttestionTransport(Request<CompagnieTransportDTO> request, Locale locale) {
        Response<DocumentReponseDTO> response = new Response<>();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        if(request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie ",locale));
            response.setHasError(true);
            return response;
        }
        fieldsToVerify.put("compagnieRaisonSociale",request.getData().getRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        CompagnieTransport existingEntity = compagnieTransportRepository.findByRaisonSociale(request.getData().getRaisonSociale(),false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        String raisonSociale = request.getData().getRaisonSociale();
        CompagnieAttestionTransport existingCompagnieAttestionTransport = compagnieAttestationTransportRepository.findByRaisonSociale(raisonSociale,false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune attestation de transport declarée pour la compagnie", locale));
            response.setHasError(true);
            return response;
        }
        Document attestationTransportEntity = existingCompagnieAttestionTransport.getAttestionTransport();
        if(attestationTransportEntity==null || attestationTransportEntity.getPath()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune attestation de transport declarée pour la compagnie", locale));
            response.setHasError(true);
            return response;
        }
        documentpath = documentpath + attestationTransportEntity.getPath() +"."+ attestationTransportEntity.getExtension();
        boolean checkIfDocumentExistsOnDirectory = DocumentUtils.checkIfDocumentExistsOnDirectory(documentpath);
        if(!checkIfDocumentExistsOnDirectory){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune attestation de transport declarée pour la compagnie", locale));
            response.setHasError(true);
            return response;
        }
        DocumentReponseDTO documentReponseDTO = new DocumentReponseDTO();
        documentReponseDTO.setLinkedAttestionDocument(racineDocumentPath + documentpath);
        response.setItem(documentReponseDTO);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }
}