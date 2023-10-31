
/**
 * @Author ALZOUMA MOUSSA MAHAMADOU
 */

package com.africanb.africanb.Business.compagnie;

import com.africanb.africanb.Business.document.DocumentBusiness;
import com.africanb.africanb.dao.entity.compagnie.CompagnieAttestionTransport;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.compagnie.Ville;
import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.dao.repository.compagnie.*;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
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
import com.africanb.africanb.utils.emailService.BodiesOfEmail;
import com.africanb.africanb.utils.emailService.EmailDTO;
import com.africanb.africanb.utils.emailService.EmailServiceBusiness;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
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

    private Response<CompagnieTransportDTO> response;

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
    private final StatusUtilCompagnieTransportRepository statusUtilCompagnieTransportRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final EmailServiceBusiness emailServiceBusiness;
    private final StatusUtilCompagnieTransportBusiness statusUtilCompagnieTransportBusiness;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateTimeFormat;

    public CompagnieTransportBusiness(StatusUtilRepository statusUtilRepository, CompagnieTransportRepository compagnieTransportRepository, VilleRepository villeRepository, DocumentBusiness documentBusiness, CompagnieAttestionTransportBusiness compagnieAttestionTransportBusiness, CompagnieAttestationTransportRepository compagnieAttestationTransportRepository, StatusUtilCompagnieTransportRepository statusUtilCompagnieTransportRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em, EmailServiceBusiness emailServiceBusiness, StatusUtilCompagnieTransportBusiness statusUtilCompagnieTransportBusiness) {
        this.statusUtilRepository = statusUtilRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.villeRepository = villeRepository;
        this.documentBusiness = documentBusiness;
        this.compagnieAttestionTransportBusiness = compagnieAttestionTransportBusiness;
        this.compagnieAttestationTransportRepository = compagnieAttestationTransportRepository;
        this.statusUtilCompagnieTransportRepository = statusUtilCompagnieTransportRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        this.emailServiceBusiness = emailServiceBusiness;
        this.statusUtilCompagnieTransportBusiness = statusUtilCompagnieTransportBusiness;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<CompagnieTransportDTO> create(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        List<CompagnieTransport> items = new ArrayList<CompagnieTransport>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<CompagnieTransportDTO>());
        for (CompagnieTransportDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("raisonSociale", dto.getRaisonSociale());
            fieldsToVerify.put("telephone", dto.getTelephone());
            fieldsToVerify.put("sigle", dto.getSigle());
            fieldsToVerify.put("email", dto.getEmail());
            //fieldsToVerify.put("ville", dto.getVilleId());
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
            CompagnieTransport existingEntity=null;
            existingEntity=compagnieTransportRepository.findByDesignation(dto.getVilleDesignation(),false);
            if (existingEntity != null) {
                response.setStatus(functionalError.DATA_EXIST("CompagnieTransport  -> " + dto.getVilleDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVille=null;
            existingVille=villeRepository.findByDesignation(dto.getVilleDesignation(),false);
            if (existingVille == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Ville  -> " + dto.getVilleDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            StatusUtil existingStatusUtilActual=null;
            existingStatusUtilActual=statusUtilRepository.findByDesignation(ProjectConstants.COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT,false);
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
            //entityToSave.setCreatedBy((long) request.getUser());
            CompagnieTransport entitySaved = compagnieTransportRepository.save(entityToSave);
            if(entitySaved==null){
                response.setStatus(functionalError.SAVE_FAIL("Erreur creation",locale));
                response.setHasError(true);
                return response;
            }
            //
            List<StatusUtilCompagnieTransportDTO> itemsDatas =  Collections.synchronizedList(new ArrayList<StatusUtilCompagnieTransportDTO>());
            StatusUtilCompagnieTransportDTO statusUtilCompagnieTransportDTO= new StatusUtilCompagnieTransportDTO();
            statusUtilCompagnieTransportDTO.setStatusUtilId(existingStatusUtilActual.getId());
            statusUtilCompagnieTransportDTO.setCompagnieTransportId(entitySaved.getId());
            itemsDatas.add(statusUtilCompagnieTransportDTO);
            Request<StatusUtilCompagnieTransportDTO> subRequest = new Request<StatusUtilCompagnieTransportDTO>();
            subRequest.setDatas(itemsDatas);
            //subRequest.setUser(request.getUser());
            Response<StatusUtilCompagnieTransportDTO> subResponse = statusUtilCompagnieTransportBusiness.create(subRequest, locale);
            if (subResponse.isHasError()) {
                response.setStatus(subResponse.getStatus());
                response.setHasError(Boolean.TRUE);
                return response;
            }
            //Send mail de création
            Runnable runnable = () -> {
                BodiesOfEmail bodiesOfEmail= new BodiesOfEmail();
                EmailDTO emailDTO = new EmailDTO();
                Request<EmailDTO> subRequestEmail = new Request<EmailDTO>();

                emailDTO.setSubject("Creation de compagnie de transport");
                emailDTO.setMessage(bodiesOfEmail.bodyHtmlMailCreateCompagny());
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
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        List<CompagnieTransport> items = new ArrayList<CompagnieTransport>();
        if(request.getDatas() == null  || CollectionUtils.isEmpty(request.getDatas())){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide ",locale));
            response.setHasError(true);
            return response;
        }
        List<CompagnieTransportDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<CompagnieTransportDTO>());
        for (CompagnieTransportDTO dto : request.getDatas()) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
            CompagnieTransport entityToSave = null;
            entityToSave = compagnieTransportRepository.findOne(dto.getId(), false);
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
        List<CompagnieTransport> itemsSaved = null;
        itemsSaved = compagnieTransportRepository.saveAll((Iterable<CompagnieTransport>) items);
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
        /*log.info("----begin delete Profil-----");

        Response<ProfilDto> response = new Response<ProfilDto>();
        List<Profil> items = new ArrayList<Profil>();

        for (ProfilDto dto : request.getDatas()) {
            // Definir les parametres obligatoires
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            // Verifier si la profil existe
            Profil existingEntity = null;
            existingEntity = statusUtilRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("profil -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            // -----------------------------------------------------------------------
            // ----------- CHECK IF DATA IS USED
            // -----------------------------------------------------------------------
            // user
            List<User> listOfUser = userRepository.findByProfilId(existingEntity.getId(), false);
            if (listOfUser != null && !listOfUser.isEmpty()) {
                response.setStatus(functionalError.DATA_NOT_DELETABLE("(" + listOfUser.size() + ")", locale));
                response.setHasError(true);
                return response;
            }
            // profilFonctionnalite
            List<ProfilFonctionnalite> listOfProfilFonctionnalite = profilFonctionnaliteRepository
                    .findByProfilId(existingEntity.getId(), false);
            if (listOfProfilFonctionnalite != null && !listOfProfilFonctionnalite.isEmpty()) {
                response.setStatus(
                        functionalError.DATA_NOT_DELETABLE("(" + listOfProfilFonctionnalite.size() + ")", locale));
                response.setHasError(true);
                return response;
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            existingEntity.setDeletedBy((long) request.getUser());
            items.add(existingEntity);
        }
        if (!items.isEmpty()) {
            // supprimer les donnees en base
            statusUtilRepository.saveAll((Iterable<Profil>) items);

            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }
        log.info("----end delete Profil-----");
        return response;*/
        return null;
    }

    @Override
    public Response<CompagnieTransportDTO> forceDelete(Request<CompagnieTransportDTO> request, Locale locale) {
       /* log.info("----begin forceDelete Profil-----");

        Response<ProfilDto> response = new Response<ProfilDto>();
        List<Profil> items = new ArrayList<Profil>();
        for (ProfilDto dto : request.getDatas()) {
            // Definir les parametres obligatoires
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            // Verifier si la profil existe
            Profil existingEntity = null;
            existingEntity = statusUtilRepository.findOne(dto.getId(), false);
            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("profil -> " + dto.getId(), locale));
                response.setHasError(true);
                return response;
            }
            // -----------------------------------------------------------------------
            // ----------- CHECK IF DATA IS USED
            // -----------------------------------------------------------------------

            // user
            List<User> listOfUser = userRepository.findByProfilId(existingEntity.getId(), false);
            if (listOfUser != null && !listOfUser.isEmpty()) {
                Request<UserDto> deleteRequest = new Request<UserDto>();
                deleteRequest.setDatas(UserTransformer.INSTANCE.toDtos(listOfUser));
                deleteRequest.setUser(request.getUser());
                Response<UserDto> deleteResponse = userBusiness.delete(deleteRequest, locale);
                if (deleteResponse.isHasError()) {
                    response.setStatus(deleteResponse.getStatus());
                    response.setHasError(true);
                    return response;
                }
            }
            // profilFonctionnalite
            List<ProfilFonctionnalite> listOfProfilFonctionnalite = profilFonctionnaliteRepository
                    .findByProfilId(existingEntity.getId(), false);
            if (listOfProfilFonctionnalite != null && !listOfProfilFonctionnalite.isEmpty()) {
                Request<ProfilFonctionnaliteDto> deleteRequest = new Request<ProfilFonctionnaliteDto>();
                deleteRequest.setDatas(ProfilFonctionnaliteTransformer.INSTANCE.toDtos(listOfProfilFonctionnalite));
                deleteRequest.setUser(request.getUser());
                Response<ProfilFonctionnaliteDto> deleteResponse = profilFonctionnaliteBusiness.delete(deleteRequest,
                        locale);
                if (deleteResponse.isHasError()) {
                    response.setStatus(deleteResponse.getStatus());
                    response.setHasError(true);
                    return response;
                }
            }
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            existingEntity.setDeletedBy((long) request.getUser());
            items.add(existingEntity);
        }

        if (!items.isEmpty()) {
            // supprimer les donnees en base
            statusUtilRepository.saveAll((Iterable<Profil>) items);

            response.setHasError(false);
            response.setStatus(functionalError.SUCCESS("", locale));
        }

        log.info("----end forceDelete Profil-----");
        return response;*/
        return null;
    }

    @Override
    public Response<CompagnieTransportDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<CompagnieTransportDTO> getByCriteria(Request<CompagnieTransportDTO> request, Locale locale) {
       /* log.info("----begin get Profil-----");

        Response<ProfilDto> response = new Response<ProfilDto>();

        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }

        List<Profil> items = statusUtilRepository.getByCriteria(request, em, locale);

        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("profil", locale));
            response.setHasError(false);
            return response;
        }

        List<ProfilDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ProfilTransformer.INSTANCE.toLiteDtos(items)
                : ProfilTransformer.INSTANCE.toDtos(items);

        final int size = items.size();
        List<String> listOfError = Collections.synchronizedList(new ArrayList<String>());
        itemsDto.parallelStream().forEach(dto -> {
            try {
                dto = getFullInfos(dto, size, request.getIsSimpleLoading(), locale);
            } catch (Exception e) {
                listOfError.add(e.getMessage());
                e.printStackTrace();
            }
        });
        if (Utilities.isNotEmpty(listOfError)) {
            Object[] objArray = listOfError.stream().distinct().toArray();
            throw new RuntimeException(StringUtils.join(objArray, ", "));
        }

        response.setItems(itemsDto);
        response.setCount(statusUtilRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        log.info("----end get Profil-----");
        return response;*/
        return null;
    }

   /* private ProfilDto getFullInfos(ProfilDto dto, Integer size, Boolean isSimpleLoading, Locale locale)
            throws Exception {
        // put code here
        if (Utilities.isTrue(isSimpleLoading)) {
            return dto;
        }
        if (size > 1) {
            return dto;
        }
        var datasFonctionnalites = profilFonctionnaliteRepository.getFonctionnalitedByProfilId(dto.getId(), Boolean.FALSE);
        if (Utilities.isNotEmpty(datasFonctionnalites)) {
            dto.setDatasFonctionnalite(FonctionnaliteTransformer.INSTANCE.toDtos(datasFonctionnalites));
        }
        return dto;
    }*/


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<CompagnieTransportDTO> getAllProcessingCompagnies(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        List<CompagnieTransport> items = new ArrayList<CompagnieTransport>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("size",request.getSize());
        fieldsToVerify.put("index",request.getIndex());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Long count=0L;
        count=compagnieTransportRepository.countAllProcessingCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT,false);
        log.info("_493 COUNT=====:"+count); //TODO A effacer
        items=compagnieTransportRepository.getAllProcessingCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_ENCOURS_TRAITEMENT,false, PageRequest.of(request.getIndex(), request.getSize()));
        log.info("_494 ITEMS=====:"+items.toString()); //TODO A effacer
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
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        List<CompagnieTransport> items = new ArrayList<CompagnieTransport>();
        CompagnieTransportDTO itemDTO= new CompagnieTransportDTO();
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
        CompagnieTransport existingEntity=null;
        existingEntity=compagnieTransportRepository.findOne(request.getData().getId(),false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        StatusUtil existingStatusUtilActual=null;
        existingStatusUtilActual=statusUtilRepository.findByDesignation(ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE,false);
        if (existingStatusUtilActual == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("StatusUtilActual  -> " + ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE, locale));
            response.setHasError(true);
            return response;
        }
        existingEntity.setStatusUtilActual(existingStatusUtilActual);
        existingEntity.setIsValidate(true);
        existingEntity.setUpdatedAt(Utilities.getCurrentDate());
        CompagnieTransport entityUpdate=compagnieTransportRepository.save(existingEntity);
        if (entityUpdate == null || entityUpdate.getIsValidate()==false) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'a pas été validée", locale));
            response.setHasError(true);
            return response;
        }
        List<StatusUtilCompagnieTransportDTO> itemsDatas =  Collections.synchronizedList(new ArrayList<StatusUtilCompagnieTransportDTO>());
        StatusUtilCompagnieTransportDTO statusUtilCompagnieTransportDTO=new StatusUtilCompagnieTransportDTO();
        statusUtilCompagnieTransportDTO.setStatusUtilId(existingStatusUtilActual.getId());
        statusUtilCompagnieTransportDTO.setCompagnieTransportId(entityUpdate.getId());
        itemsDatas.add(statusUtilCompagnieTransportDTO);
        Request<StatusUtilCompagnieTransportDTO> subRequest = new Request<StatusUtilCompagnieTransportDTO>();
        subRequest.setDatas(itemsDatas);
        //subRequest.setUser(request.getUser());
        Response<StatusUtilCompagnieTransportDTO> subResponse = statusUtilCompagnieTransportBusiness.create(subRequest, locale);
        if (subResponse.isHasError()) {
            response.setStatus(subResponse.getStatus());
            response.setHasError(Boolean.TRUE);
            return response;
        }
        //Send mail de validation
        Runnable runnable = () -> {
            BodiesOfEmail bodiesOfEmail= new BodiesOfEmail();
            EmailDTO emailDTO = new EmailDTO();
            Request<EmailDTO> subRequestEmail = new Request<EmailDTO>();

            emailDTO.setSubject("Validation de compagnie de transport");
            emailDTO.setMessage(bodiesOfEmail.bodyHtmlMailValidationCompagny());
            emailDTO.setToAddress(entityUpdate.getEmail());
            subRequestEmail.setData(emailDTO);

            emailServiceBusiness.sendSimpleEmail(subRequestEmail,locale);
        };
        runnable.run();

        CompagnieTransportDTO itemDto = CompagnieTransportTransformer.INSTANCE.toDto(entityUpdate);
        //response.setCount(count);
        response.setItem(itemDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get Compgnaie de transport-----");
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public  Response<CompagnieTransportDTO> getAllValidedCompagnies(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException {
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        List<CompagnieTransport> items = new ArrayList<CompagnieTransport>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("size",request.getSize());
        fieldsToVerify.put("index",request.getIndex());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Long count=0L;
        count=compagnieTransportRepository.countAllValidedCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE,false);
        items=compagnieTransportRepository.getAllValidedCompagnies(ProjectConstants.COMPAGNIE_TRANSPORT_VALIDE,false, PageRequest.of(request.getIndex(), request.getSize()));
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
        Response<DocumentDTO> response = new Response<DocumentDTO>();
        List<CompagnieTransport> items = new ArrayList<CompagnieTransport>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        if(request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie ",locale));
            response.setHasError(true);
            return response;
        }
        //Vérification champ raisonSociale
        fieldsToVerify.put("compagnieRaisonSociale",request.getData().getRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        CompagnieTransport existingEntity=null;
        existingEntity=compagnieTransportRepository.findByRaisonSociale(request.getData().getRaisonSociale(),false);
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
        //Get Extension
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if(!extension.equalsIgnoreCase("PDF")){
            response.setStatus(functionalError.DATA_NOT_EXIST("Le document doit être au format PDF ",locale));
            response.setHasError(true);
            return response;
        }
        //Check file size
        Double limitSize = Double.parseDouble(limitFileSizeDefault);
        boolean compareFileSizeToLimitSize= DocumentUtils.compareFileSizeToLimitSize(file,limitSize);
        if(!compareFileSizeToLimitSize){
            response.setStatus(functionalError.SAVE_FAIL("La taille du document ne doit pas depasser 1 Mo",locale));
            response.setHasError(true);
            return response;
        }
        //Check the file size
        byte[] content = file.getBytes();
        String contentType = file.getContentType();
        String filename = UUID.randomUUID().toString();
        //Initialisation documentDTO
        DocumentDTO documentDTO = new DocumentDTO();
        documentDTO.setDesignation(request.getData().getRaisonSociale());
        documentDTO.setPath(filename);
        documentDTO.setTypeMime(contentType);
        documentDTO.setExtension(extension);
        //Check if directory exists on hard disk
        boolean checkIfDirectoryExists = DocumentUtils.checkIfDirectoryExists(documentpath);
        if(!checkIfDirectoryExists){
            boolean createDirectoryOnHardDisk = DocumentUtils.createDirectoryOnHardDisk(documentpath);
            if(!createDirectoryOnHardDisk){
                response.setStatus(functionalError.SAVE_FAIL("Erreur de creation :: repertoire inexistant",locale));
                response.setHasError(true);
                return response;
            }
        }
        //Create a file on disk local
        boolean createFileOnDiskHard=false;
        String fileLocation = documentpath + filename + "." + extension;
        createFileOnDiskHard=DocumentUtils.createFileOnDiskHard(content, fileLocation);
        if(!createFileOnDiskHard){
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation du fichier",locale));
            response.setHasError(true);
            return response;
        }
        //Creating documentDTO
        Request<DocumentDTO> subRequest = new Request<DocumentDTO>();
        subRequest.setData(documentDTO);
        Response<DocumentDTO> subResponse = documentBusiness.create(subRequest, locale);
        if (subResponse.isHasError()) {
            response.setStatus(subResponse.getStatus());
            response.setHasError(Boolean.TRUE);
            return response;
        }
        //Creating compagnieAttestionTransportDTO
        List<CompagnieAttestionTransportDTO> itemsDTO = new ArrayList<CompagnieAttestionTransportDTO>();
        CompagnieAttestionTransportDTO compagnieAttestionTransportDTO = new CompagnieAttestionTransportDTO();
        compagnieAttestionTransportDTO.setCompagnieRaisonSociale(request.getData().getRaisonSociale());
        compagnieAttestionTransportDTO.setDocumentDesignation(subResponse.getItem().getDesignation());
        itemsDTO.add(compagnieAttestionTransportDTO);
        Request<CompagnieAttestionTransportDTO> subRequest1 = new Request<CompagnieAttestionTransportDTO>();
        subRequest1.setDatas(itemsDTO);
        Response<CompagnieAttestionTransportDTO> subResponse1 = compagnieAttestionTransportBusiness.create(subRequest1, locale);
        if (subResponse.isHasError()) {
            response.setStatus(subResponse.getStatus());
            response.setHasError(Boolean.TRUE);
            return response;
        }
        //return document for response
        response.setItem(subResponse.getItem());
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class,IllegalArgumentException.class, IOException.class})
    public  Response<DocumentReponseDTO> getLinkedAttestionTransport(Request<CompagnieTransportDTO> request, Locale locale) throws ParseException, IOException {
        Response<DocumentReponseDTO> response = new Response<DocumentReponseDTO>();
        List<CompagnieTransport> items = new ArrayList<CompagnieTransport>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        if(request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée définie ",locale));
            response.setHasError(true);
            return response;
        }
        //Vérification champ raisonSociale
        fieldsToVerify.put("compagnieRaisonSociale",request.getData().getRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        //Check if compagny exists
        CompagnieTransport existingEntity=null;
        existingEntity=compagnieTransportRepository.findByRaisonSociale(request.getData().getRaisonSociale(),false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        //Check if compagny has a attestation transport
        String raisonSociale = request.getData().getRaisonSociale();
        CompagnieAttestionTransport existingCompagnieAttestionTransport = null;
        existingCompagnieAttestionTransport = compagnieAttestationTransportRepository.findByRaisonSociale(raisonSociale,false);
        if (existingEntity == null) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune attestation de transport declarée pour la compagnie", locale));
            response.setHasError(true);
            return response;
        }
        //Get information about Attestation Document
        Document attestationTransportEntity = existingCompagnieAttestionTransport.getAttestionTransport();
        if(attestationTransportEntity==null || attestationTransportEntity.getPath()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune attestation de transport declarée pour la compagnie", locale));
            response.setHasError(true);
            return response;
        }
        //Check if the attestion transport exists on hard disk
        documentpath = documentpath + attestationTransportEntity.getPath() +"."+ attestationTransportEntity.getExtension();
        boolean checkIfDocumentExistsOnDirectory = DocumentUtils.checkIfDocumentExistsOnDirectory(documentpath);
        if(!checkIfDocumentExistsOnDirectory){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune attestation de transport declarée pour la compagnie", locale));
            response.setHasError(true);
            return response;
        }
        //Build path absolu of attestion transport with Extension
        String linked =  racineDocumentPath + documentpath;
        //return document for response
        DocumentReponseDTO documentReponseDTO = new DocumentReponseDTO();
        documentReponseDTO.setLinkedAttestionDocument(linked);
        response.setItem(documentReponseDTO);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }
}