package com.africanb.africanb.Business.reservationBilletVoyage;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Gare;
import com.africanb.africanb.dao.entity.compagnie.StatusUtil;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.PrixOffreVoyage;
import com.africanb.africanb.dao.entity.offreVoyage.Programme;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ClientDetails;
import com.africanb.africanb.dao.entity.reservationBilletVoyage.ReservationBilletVoyage;
import com.africanb.africanb.dao.entity.security.Users;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.*;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.PrixOffreVoyageRepository;
import com.africanb.africanb.dao.repository.offreVoyage.ProgrammeRepository;
import com.africanb.africanb.dao.repository.reservationBilletVoyage.ReservationBilletVoyageRepository;
import com.africanb.africanb.dao.repository.security.UsersRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.ReservationBilletVoyageDTO;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.StatusUtilReservationBilletVoyageDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.reservationBilletVoyage.ReservationBilletVoyageTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Constants.ProjectConstants;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.rmi.server.UID;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log
@Component
public class ReservationBilletVoyageBusiness implements IBasicBusiness<Request<ReservationBilletVoyageDTO>, Response<ReservationBilletVoyageDTO>> {

    private static final String NOM_FIELD = "nom";
    private static final String PRENOMS_FIELD = "prenoms";
    private static final String EMAIL_FIELD = "email";
    private static final String TELEPHONE_FIELD = "telephone";
    private static final String GARE_FIELD = "gare";
    private static final String OFFRE_VOYAGE_FIELD = "offreVoyage";
    private static final String PROGRAMME_FIELD = "programme";
    private static final String NOMBRE_PLACE_FIELD = "nombrePlace";
    private static final String CATEGORIE_VOYAGEUR_FIELD = "categorieVoyageur";


    private final ReferenceRepository referenceRepository;
    private final ProgrammeRepository programmeRepository;
    private final OffreVoyageRepository offreVoyageRepository;
    private final GareRepository gareRepository;
    private final ModeAbonnementRepository modeAbonnementRepository;
    private final PrixOffreVoyageRepository prixOffreVoyageRepository;
    private final StatusUtilRepository statusUtilRepository;
    private final UsersRepository usersRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final ReservationBilletVoyageRepository reservationBilletVoyageRepository;
    private final StatusUtilRservationBilletVoyageBusiness statusUtilRservationBilletVoyageBusiness;

    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ReservationBilletVoyageBusiness(ReferenceRepository referenceRepository, ProgrammeRepository programmeRepository, OffreVoyageRepository offreVoyageRepository, GareRepository gareRepository, ModeAbonnementRepository modeAbonnementRepository, PrixOffreVoyageRepository prixOffreVoyageRepository, StatusUtilRepository statusUtilRepository, UsersRepository usersRepository, CompagnieTransportRepository compagnieTransportRepository, ReservationBilletVoyageRepository reservationBilletVoyageRepository, StatusUtilRservationBilletVoyageBusiness statusUtilRservationBilletVoyageBusiness, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.referenceRepository = referenceRepository;
        this.programmeRepository = programmeRepository;
        this.offreVoyageRepository = offreVoyageRepository;
        this.gareRepository = gareRepository;
        this.modeAbonnementRepository = modeAbonnementRepository;
        this.prixOffreVoyageRepository = prixOffreVoyageRepository;
        this.statusUtilRepository = statusUtilRepository;
        this.usersRepository = usersRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.reservationBilletVoyageRepository = reservationBilletVoyageRepository;
        this.statusUtilRservationBilletVoyageBusiness = statusUtilRservationBilletVoyageBusiness;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }


    @Override
    public Response<ReservationBilletVoyageDTO> create(Request<ReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        Response<ReservationBilletVoyageDTO> response = new Response<>();
        List<ReservationBilletVoyage> items = new ArrayList<>();
        if(Optional.of(request.getData()).isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        ReservationBilletVoyageDTO dto = request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put(GARE_FIELD, dto.getGareDesignation());
        fieldsToVerify.put(OFFRE_VOYAGE_FIELD, dto.getOffreVoyageDesignation());
        fieldsToVerify.put(PROGRAMME_FIELD, dto.getProgrammeDesignation());
        fieldsToVerify.put(CATEGORIE_VOYAGEUR_FIELD, dto.getCategorieVoyageur());
        fieldsToVerify.put(NOMBRE_PLACE_FIELD, dto.getNombrePlace());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        if (Optional.ofNullable(dto.getIsOtherPerson()).orElse(false)){
            if (Objects.isNull(dto.getClientDetails())) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Client details non défini", locale));
                response.setHasError(true);
                return response;
            }
            if (!validateClientDetails(dto.getClientDetails())) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
        }
        Users existingUser = usersRepository.findOne(Long.valueOf(request.userID),false);
        if (Objects.isNull(existingUser)) {
            response.setStatus(functionalError.SAVE_FAIL("user inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(dto.getOffreVoyageDesignation(),false);
        if (Objects.isNull(existingOffreVoyage)) {
            response.setStatus(functionalError.SAVE_FAIL("Offre de voyage inexistante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        Gare existingGare = gareRepository.findByDesignation(dto.getGareDesignation(),false);
        if (Objects.isNull(existingGare)) {
            response.setStatus(functionalError.SAVE_FAIL("gare inexistante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        Programme existingProgramme = programmeRepository.findByDesignation(dto.getProgrammeDesignation(),false);
        if (Objects.isNull(existingProgramme)) {
            response.setStatus(functionalError.SAVE_FAIL("programme inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        StatusUtil existingStatusUtilActual = statusUtilRepository.findByDesignation(ProjectConstants.REF_ELEMENT_RESERVATION_CREE,false);
        if (Objects.isNull(existingStatusUtilActual)) {
            response.setStatus(functionalError.SAVE_FAIL("Status inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        if(existingProgramme.getNombrePlaceDisponible() < dto.getNombrePlace()){
            response.setStatus(functionalError.SAVE_FAIL("Pas de place disponible !!!", locale));
            response.setHasError(true);
            return response;
        }
        PrixOffreVoyage existingPrixOffreVoyage = prixOffreVoyageRepository.findByOffreVoyageAndCategorieVoyageur(existingOffreVoyage.getDesignation(),dto.getCategorieVoyageur(),false);
        if(Objects.isNull(existingPrixOffreVoyage)){
            response.setStatus(functionalError.SAVE_FAIL("Aucun prix fixé pour la catégorie du voyageur !!!", locale));
            response.setHasError(true);
            return response;
        }

        Double montantTotalReservation = getMontantReservation(dto, existingPrixOffreVoyage);
        dto.setMontantTotalReservation(montantTotalReservation);
        ReservationBilletVoyage entityToSave = ReservationBilletVoyageTransformer.INSTANCE.toEntity(dto,existingGare,existingOffreVoyage,existingProgramme,existingStatusUtilActual);
        entityToSave.setIsDeleted(false);
        entityToSave.setDesignation(existingUser.getNom()+RandomStringUtils.randomAlphanumeric(6));
        ReservationBilletVoyage entitySaved = Optional.of(reservationBilletVoyageRepository.save(entityToSave)).orElseThrow();
        if(Objects.isNull(entitySaved)){
            response.setStatus(functionalError.SAVE_FAIL("Erreur de reservation",locale));
            response.setHasError(true);
            return response;
        }

        if (!initializeReservationOffreVoyageStatus(locale, existingStatusUtilActual, entitySaved)){
            response.setStatus(functionalError.SAVE_FAIL("Status reservation non crée",locale));
            response.setHasError(true);
            return response;
        }

        items.add(entitySaved);
        List<ReservationBilletVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ReservationBilletVoyageTransformer.INSTANCE.toLiteDtos(items)
                : ReservationBilletVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    private boolean initializeReservationOffreVoyageStatus(Locale locale, StatusUtil existingStatusUtilActual, ReservationBilletVoyage entitySaved) throws ParseException {
        List<StatusUtilReservationBilletVoyageDTO> itemDatas = getStatusUtilReservationBilletVoyageDTOS(existingStatusUtilActual, entitySaved);
        Request<StatusUtilReservationBilletVoyageDTO> subRequest = new Request<>();
        subRequest.setDatas(itemDatas);
        if(statusUtilRservationBilletVoyageBusiness.create(subRequest, locale).isHasError()) return false;
        return true;
    }

    private static List<StatusUtilReservationBilletVoyageDTO> getStatusUtilReservationBilletVoyageDTOS(StatusUtil existingStatusUtilActual, ReservationBilletVoyage entitySaved) {
        List<StatusUtilReservationBilletVoyageDTO> itemDatas =  Collections.synchronizedList(new ArrayList<>());
        StatusUtilReservationBilletVoyageDTO statusUtilReservationBilletVoyageDTO= new StatusUtilReservationBilletVoyageDTO();
        statusUtilReservationBilletVoyageDTO.setStatusUtilDesignation(existingStatusUtilActual.getDesignation());
        statusUtilReservationBilletVoyageDTO.setReservationBilletVoyageDesignation(entitySaved.getDesignation());
        itemDatas.add(statusUtilReservationBilletVoyageDTO);
        return itemDatas;
    }

    private boolean validateClientDetails(ClientDetails clientDetails) {
        Map<String, Object> fieldsToVerifyClient = new HashMap<>();
        fieldsToVerifyClient.put(NOM_FIELD, clientDetails.getNom());
        fieldsToVerifyClient.put(PRENOMS_FIELD, clientDetails.getPrenoms());
        fieldsToVerifyClient.put(EMAIL_FIELD, clientDetails.getEmail());
        fieldsToVerifyClient.put(TELEPHONE_FIELD, clientDetails.getTelephone());

        return Validate.RequiredValue(fieldsToVerifyClient).isGood();
    }

    private  Double getMontantReservation(ReservationBilletVoyageDTO dto, PrixOffreVoyage existingPrixOffreVoyage) {
        return existingPrixOffreVoyage.getPrix().doubleValue() * dto.getNombrePlace();
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ReservationBilletVoyageDTO> getReservationBilletVoyageBySampleUser(Request<ReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        Response<ReservationBilletVoyageDTO> response = new Response<>();
        Users existingUser = usersRepository.findOne(Long.valueOf(request.userID),false);
        if (existingUser==null || existingUser.getRole()==null || existingUser.getRole().getCode()==null) {
            response.setStatus(functionalError.SAVE_FAIL("user inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        if(existingUser.getRole().getCode().equalsIgnoreCase(ProjectConstants.ROLE_UTI_SIMPLE)){
            response.setStatus(functionalError.SAVE_FAIL("Autorisation insuffisante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        List<ReservationBilletVoyage> items = reservationBilletVoyageRepository.findByUsersSample(existingUser.getEmail(),false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune reservation de billet !!!!", locale));
            response.setHasError(true);
            return response;
        }

        List<ReservationBilletVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ReservationBilletVoyageTransformer.INSTANCE.toLiteDtos(items)
                : ReservationBilletVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get reservationBilletVoyage-----");
        return response;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ReservationBilletVoyageDTO> getReservationBilletVoyageByUserGareCompagnieTransport(Request<ReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        Response<ReservationBilletVoyageDTO> response = new Response<>();
        Users existingUser = usersRepository.findOne(Long.valueOf(request.userID),false);
        if (existingUser == null) {
            response.setStatus(functionalError.SAVE_FAIL("user inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        if(!existingUser.getRole().getCode().equalsIgnoreCase(ProjectConstants.ROLE_UTI_GARE_COMPAGNIE_TRANSPORT)){
            response.setStatus(functionalError.SAVE_FAIL("Autorisation insuffisante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        if(existingUser.getGareDesignation()==null){
            response.setStatus(functionalError.SAVE_FAIL("Gare utilisateur non trouvée !!!!", locale));
            response.setHasError(true);
            return response;
        }
        Gare existingGare = gareRepository.findByDesignation(existingUser.getGareDesignation(),false);
        if (existingGare == null) {
            response.setStatus(functionalError.SAVE_FAIL("Gare utilisateur inexistante !!!", locale));
            response.setHasError(true);
            return response;
        }
        List<ReservationBilletVoyage> items = reservationBilletVoyageRepository.findByUserGareCompagnieTransport(existingGare.getDesignation(),false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune reservation de billet !!!!", locale));
            response.setHasError(true);
            return response;
        }

        List<ReservationBilletVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ReservationBilletVoyageTransformer.INSTANCE.toLiteDtos(items)
                : ReservationBilletVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get reservationBilletVoyage-----");
        return response;
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<ReservationBilletVoyageDTO> getReservationBilletVoyageByAdminCompagnieTransport(Request<ReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        Response<ReservationBilletVoyageDTO> response = new Response<>();
        Users existingUser = usersRepository.findOne(Long.valueOf(request.userID),false);
        if (existingUser==null || existingUser.getRole()==null || existingUser.getRole().getCode()==null) {
            response.setStatus(functionalError.SAVE_FAIL("user inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        if(!existingUser.getRole().getCode().equalsIgnoreCase(ProjectConstants.ROLE_ADMIN_COMPAGNIE_TRANSPORT)){
            response.setStatus(functionalError.SAVE_FAIL("Autorisation insuffisante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        CompagnieTransport existingCompagnieTransport = existingUser.getCompagnieTransport();
        if(existingCompagnieTransport == null || existingCompagnieTransport.getRaisonSociale() == null){
            response.setStatus(functionalError.SAVE_FAIL("Compagnie transport non trouvée !!!!", locale));
            response.setHasError(true);
            return response;
        }
        List<ReservationBilletVoyage>  items = reservationBilletVoyageRepository.findByAdminCompagnieTransport(existingCompagnieTransport.getRaisonSociale(),false);
        if(CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune reservation de billet !!!!", locale));
            response.setHasError(true);
            return response;
        }

        List<ReservationBilletVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? ReservationBilletVoyageTransformer.INSTANCE.toLiteDtos(items)
                : ReservationBilletVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end get reservationBilletVoyage-----");
        return response;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> update(Request<ReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> delete(Request<ReservationBilletVoyageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> forceDelete(Request<ReservationBilletVoyageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> getByCriteria(Request<ReservationBilletVoyageDTO> request, Locale locale) {
        return null;
    }

}
