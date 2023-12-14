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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Log
@Component
public class ReservationBilletVoyageBusiness implements IBasicBusiness<Request<ReservationBilletVoyageDTO>, Response<ReservationBilletVoyageDTO>> {


    private Response<ReservationBilletVoyageDTO> response;

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
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        List<ReservationBilletVoyage> items = new ArrayList<ReservationBilletVoyage>();
        if(Optional.of(request.getData()).isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        ReservationBilletVoyageDTO dto = request.getData();
        List<ReservationBilletVoyageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<ReservationBilletVoyageDTO>());
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
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
        if (existingUser==null) {
            response.setStatus(functionalError.SAVE_FAIL("user inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        OffreVoyage existingOffreVoyage = null;
        existingOffreVoyage = offreVoyageRepository.findByDesignation(dto.getOffreVoyageDesignation(),false);
        if (existingOffreVoyage==null) {
            response.setStatus(functionalError.SAVE_FAIL("Offre de voyage inexistante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        Gare existingGare = null;
        existingGare = gareRepository.findByDesignation(dto.getGareDesignation(),false);
        if (existingGare==null) {
            response.setStatus(functionalError.SAVE_FAIL("gare inexistante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        Programme existingProgramme = null;
        existingProgramme = programmeRepository.findByDesignation(dto.getProgrammeDesignation(),false);
        if (existingProgramme==null) {
            response.setStatus(functionalError.SAVE_FAIL("programme inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        StatusUtil existingStatusUtilActual = null;
        existingStatusUtilActual = statusUtilRepository.findByDesignation(ProjectConstants.REF_ELEMENT_RESERVATION_CREE,false);
        if (existingStatusUtilActual==null) {
            response.setStatus(functionalError.SAVE_FAIL("Status inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }

        Integer nombrePlaceDisponible=existingProgramme.getNombrePlaceDisponible();
        if(nombrePlaceDisponible<dto.getNombrePlace()){
            response.setStatus(functionalError.SAVE_FAIL("Aucune place disponible !!!", locale));
            response.setHasError(true);
            return response;
        }

        PrixOffreVoyage existingPrixOffreVoyage=prixOffreVoyageRepository.findByOffreVoyageAndCategorieVoyageur(existingOffreVoyage.getDesignation(),dto.getCategorieVoyageur(),false);
        if(existingPrixOffreVoyage==null){
            response.setStatus(functionalError.SAVE_FAIL("Aucun prix fixé pour la catégorie du voyageur !!!", locale));
            response.setHasError(true);
            return response;
        }
        Double montantTotalReservation = getMontantReservation(dto, existingPrixOffreVoyage);
        log.info("_147 Montant total de la reservation="+ montantTotalReservation);
        dto.setMontantTotalReservation(montantTotalReservation);
        ReservationBilletVoyage entityToSave = ReservationBilletVoyageTransformer.INSTANCE.toEntity(dto,existingGare,existingOffreVoyage,existingProgramme,existingStatusUtilActual);
        entityToSave.setIsDeleted(false);
        //entityToSave.setDateReservation(Utilities.getCurrentDate());
        ReservationBilletVoyage entitySaved = reservationBilletVoyageRepository.save(entityToSave);
        if(entitySaved==null){
            response.setStatus(functionalError.SAVE_FAIL("Erreur creation",locale));
            response.setHasError(true);
            return response;
        }

        boolean responseStatusReservation = initializeReservationOffreVoyageStatus(locale, response, existingStatusUtilActual, entitySaved);
        if (responseStatusReservation == false){
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

    private boolean initializeReservationOffreVoyageStatus(Locale locale, Response<ReservationBilletVoyageDTO> response, StatusUtil existingStatusUtilActual, ReservationBilletVoyage entitySaved) throws ParseException {
        List<StatusUtilReservationBilletVoyageDTO> itemDatas =  Collections.synchronizedList(new ArrayList<>());
        StatusUtilReservationBilletVoyageDTO statusUtilReservationBilletVoyageDTO= new StatusUtilReservationBilletVoyageDTO();
        statusUtilReservationBilletVoyageDTO.setStatusUtilDesignation(existingStatusUtilActual.getDesignation());
        statusUtilReservationBilletVoyageDTO.setReservationBilletVoyageDesignation(entitySaved.getDesignation());
        itemDatas.add(statusUtilReservationBilletVoyageDTO);
        Request<StatusUtilReservationBilletVoyageDTO> subRequest = new Request<>();
        subRequest.setDatas(itemDatas);
        Response<StatusUtilReservationBilletVoyageDTO> subResponse = statusUtilRservationBilletVoyageBusiness.create(subRequest, locale);
        if (subResponse.isHasError()) return false;
        return true ;
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
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        List<ReservationBilletVoyage> items = new ArrayList<ReservationBilletVoyage>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        //get users connected
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
        items=reservationBilletVoyageRepository.findByUsersSample(existingUser.getEmail(),false);
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
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        List<ReservationBilletVoyage> items = new ArrayList<ReservationBilletVoyage>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        //get users connected
        Users existingUser = usersRepository.findOne(Long.valueOf(request.userID),false);
        if (existingUser==null || existingUser.getRole()==null || existingUser.getRole().getCode()==null) {
            response.setStatus(functionalError.SAVE_FAIL("user inexistant !!!!", locale));
            response.setHasError(true);
            return response;
        }
        if(!existingUser.getRole().getCode().equalsIgnoreCase(ProjectConstants.ROLE_UTI_GARE_COMPAGNIE_TRANSPORT)){
            response.setStatus(functionalError.SAVE_FAIL("Autorisation insuffisante !!!!", locale));
            response.setHasError(true);
            return response;
        }
        Gare existingGare=null;
        if(existingUser.getGareDesignation()==null){
            response.setStatus(functionalError.SAVE_FAIL("Gare utilisateur non trouvée !!!!", locale));
            response.setHasError(true);
            return response;
        }
        existingGare = gareRepository.findByDesignation(existingUser.getGareDesignation(),false);
        if (existingGare == null) {
            response.setStatus(functionalError.SAVE_FAIL("Gare utilisateur inexistante !!!", locale));
            response.setHasError(true);
            return response;
        }
        items=reservationBilletVoyageRepository.findByUserGareCompagnieTransport(existingGare.getDesignation(),false);
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
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        List<ReservationBilletVoyage> items = new ArrayList<ReservationBilletVoyage>();
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        //get users connected
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
        CompagnieTransport existingCompagnieTransport=existingUser.getCompagnieTransport();
        if(existingCompagnieTransport==null || existingCompagnieTransport.getRaisonSociale()==null){
            response.setStatus(functionalError.SAVE_FAIL("Compagnie transport non trouvée !!!!", locale));
            response.setHasError(true);
            return response;
        }
        items=reservationBilletVoyageRepository.findByAdminCompagnieTransport(existingCompagnieTransport.getRaisonSociale(),false);
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

    @Override
    public Response<ReservationBilletVoyageDTO> update(Request<ReservationBilletVoyageDTO> request, Locale locale) throws ParseException {
        /*Response<OffreVoyageDTO> response = new Response<OffreVoyageDTO>();
        List<OffreVoyage> items = new ArrayList<OffreVoyage>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<OffreVoyageDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<OffreVoyageDTO>());
        for(OffreVoyageDTO dto: request.getDatas() ) {
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
        for(OffreVoyageDTO dto: itemsDtos) {
            OffreVoyage entityToSave = offreVoyageRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (entityToSave.getIsActif() == true) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Vous ne pouvez pas modifier l'offre de voyage car elle est active.", locale));
                response.setHasError(true);
                return response;
            }
            //TODO Verifier si l'offre de voyage a été réservée
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                OffreVoyage existingOffreVoyage = offreVoyageRepository.findByDesignation(dto.getDesignation(), false);
                if (existingOffreVoyage != null && !existingOffreVoyage.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("OffreVoyage -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            //Ville depart
            String villeDepartDesignation=entityToSave.getVilleDepart()!=null&&entityToSave.getVilleDepart().getDesignation()!=null
                                       ?entityToSave.getVilleDepart().getDesignation()
                                       :null;
            if(villeDepartDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage ne possede aucune ville de départ", locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVilleDepart = villeRepository.findByDesignation(villeDepartDesignation,false);
            if (existingVilleDepart == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La ville de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getVilleDepartDesignation()) && !dto.getVilleDepartDesignation().equals(existingVilleDepart.getDesignation())) {
                Ville villeDepartToSave = villeRepository.findByDesignation(dto.getVilleDepartDesignation(), false);
                if (villeDepartToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle ville de départ  l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setVilleDepart(villeDepartToSave);
            }
            //Ville destnation
            String villeDestinationDesignation=entityToSave.getVilleDestination()!=null&&entityToSave.getVilleDestination().getDesignation()!=null
                    ?entityToSave.getVilleDestination().getDesignation()
                    :null;
            if(villeDestinationDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage ne possede aucune ville de destination", locale));
                response.setHasError(true);
                return response;
            }
            Ville existingVilleDestination = villeRepository.findByDesignation(villeDestinationDesignation,false);
            if (existingVilleDestination == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La ville de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getVilleDestinationDesignation()) && !dto.getVilleDestinationDesignation().equals(existingVilleDestination.getDesignation())) {
                Ville villeDestinationToSave = villeRepository.findByDesignation(dto.getVilleDestinationDesignation(), false);
                if (villeDestinationToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle ville de destination  l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setVilleDestination(villeDestinationToSave);
            }
            //TypeOffreVoyage
            String typeOffreVoyageDesignation=entityToSave.getTypeOffreVoyage()!=null&&entityToSave.getTypeOffreVoyage().getDesignation()!=null
                    ?entityToSave.getTypeOffreVoyage().getDesignation()
                    :null;
            if(typeOffreVoyageDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Le prix offre de voyage ne relie à aucun type offre de voyage", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeOffrreVoyage = referenceRepository.findByDesignation(typeOffreVoyageDesignation,false);
            if (existingTypeOffrreVoyage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le type offre de voyage de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getTypeOffreVoyageDesignation()) && !dto.getTypeOffreVoyageDesignation().equals(existingTypeOffrreVoyage.getDesignation())) {
                Reference typeOffreVoyageToSave = referenceRepository.findByDesignation(dto.getTypeOffreVoyageDesignation(), false);
                if (typeOffreVoyageToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le nouveau type offre de voyage de l'offre de voyage n'existe pas-> " + dto.getId() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setTypeOffreVoyage(typeOffreVoyageToSave);
            }
            //Autres
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDesignation().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(dto.getIsActif()!=null && !dto.getIsActif().equals(entityToSave.getIsActif())){
                entityToSave.setIsActif(dto.getIsActif());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            OffreVoyage entityupdated=null;
            entityupdated=offreVoyageRepository.save(entityToSave);
            if(entityupdated==null){
                response.setStatus(functionalError.SAVE_FAIL("Erreur de Modification", locale));
                response.setHasError(true);
                return response;
            }
            //Check if prixOffreVoyageExist
            if(!CollectionUtils.isEmpty(dto.getPrixOffreVoyageDTOList())){
                Request<PrixOffreVoyageDTO> subRequest = new Request<PrixOffreVoyageDTO>();
                subRequest.setDatas( dto.getPrixOffreVoyageDTOList());
                for(PrixOffreVoyageDTO prixOffreVoyageDTO: dto.getPrixOffreVoyageDTOList()){
                    prixOffreVoyageDTO.setOffreVoyageDesignation(entityupdated.getDesignation());
                }
                //subRequest.setUser(request.getUser());
                Response<PrixOffreVoyageDTO> subResponse = prixOffreVoyageBusiness.update(subRequest, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            //Check if jourSemaine
            if(!CollectionUtils.isEmpty(dto.getJourSemaineDTOList())){
                Request<JourSemaineDTO> subRequestJourSemaine = new Request<JourSemaineDTO>();
                subRequestJourSemaine.setDatas( dto.getJourSemaineDTOList());
                for(JourSemaineDTO jourSemaineDTO: dto.getJourSemaineDTOList()){
                    jourSemaineDTO.setOffreVoyageDesignation(entityupdated.getDesignation());
                }
                //subRequest.setUser(request.getUser());
                Response<JourSemaineDTO> subResponse = jourSemaineBusinesse.update(subRequestJourSemaine, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            //Check if villeEscaleList
            if(!CollectionUtils.isEmpty(dto.getVilleEscaleDTOList())){
                Request<VilleEscaleDTO> subRequestVilleEscale = new Request<VilleEscaleDTO>();
                subRequestVilleEscale.setDatas( dto.getVilleEscaleDTOList());
                //subRequest.setUser(request.getUser());
                //Initialisation de l'offre de voyage
                for(VilleEscaleDTO villeEscaleDTO: dto.getVilleEscaleDTOList()){
                    villeEscaleDTO.setOffreVoyageDesignation(entityupdated.getDesignation());
                }
                Response<VilleEscaleDTO> subResponse = villeEscaleBusiness.update(subRequestVilleEscale, locale);
                if (subResponse.isHasError()) {
                    response.setStatus(subResponse.getStatus());
                    response.setHasError(Boolean.TRUE);
                    return response;
                }
            }
            items.add(entityupdated);
        }
        List<OffreVoyageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? OffreVoyageTransformer.INSTANCE.toLiteDtos(items)
                                : OffreVoyageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update l'offre de voyage-----");
        return response;*/
        return null;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> delete(Request<ReservationBilletVoyageDTO> request, Locale locale) {

/*        log.info("----begin delete agence-----");

        Response<AgenceDto> response = new Response<AgenceDto>();
        List<Agence> items = new ArrayList<Agence>();

        //Verification
        if(request.getDatas().isEmpty() || request.getDatas() == null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste de données est vide ",locale));
            response.setHasError(true);
            return response;
        }

        //Verification des champs obligatoires
        for(AgenceDto dto : request.getDatas()) {

            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }

        }

        //Parcourir la liste
        for(AgenceDto dto : request.getDatas()){

            // Verification du parametre identifiant
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());

            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }

            // Verify if Functionality  exist
            Agence existingEntity = null;

            existingEntity = agenceRepository.findOne(dto.getId(), false);

            if (existingEntity == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'agence ayant  id -> " + dto.getId() + ",n'existe pas", locale));
                response.setHasError(true);
                return response;
            }

            log.info("_413 Verification d'existence de l'objet"+existingEntity.toString()); //TODO A effacer

            //Suppression logique
            existingEntity.setIsDeleted(true);
            existingEntity.setDeletedAt(Utilities.getCurrentDate());
            existingEntity.setDeletedBy(request.user);// a modifier

            items.add(existingEntity);

        }

        //Verificatioon de la liste de données recues
        if(items == null  || items.isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste de données est vide ",locale));
            response.setHasError(true);
            return response;
        }

        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        return response;*/
        return null;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> forceDelete(Request<ReservationBilletVoyageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<ReservationBilletVoyageDTO> getByCriteria(Request<ReservationBilletVoyageDTO> request, Locale locale) {
       /*
        log.info("----begin get agence-----");

        Response<AgenceDto> response = new Response<AgenceDto>();

        if (Utilities.blank(request.getData().getOrderField())) {
            request.getData().setOrderField("");
        }
        if (Utilities.blank(request.getData().getOrderDirection())) {
            request.getData().setOrderDirection("asc");
        }

        List<Agence> items = agenceRepository.getByCriteria(request, em, locale);

        if (Utilities.isEmpty(items)) {
            response.setStatus(functionalError.DATA_EMPTY("Aucune agence ne correspond aux critères de recherche definis", locale));
            response.setHasError(false);
            return response;
        }

        List<AgenceDto> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                 ? AgenceTransformer.INSTANCE.toLiteDtos(items)
                                 : AgenceTransformer.INSTANCE.toDtos(items);


        response.setItems(itemsDto);
        response.setCount(agenceRepository.count(request, em, locale));
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        log.info("----end get agence-----");

        return response;
    */
        return null;
    }

}
