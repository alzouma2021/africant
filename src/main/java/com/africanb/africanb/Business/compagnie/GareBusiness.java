
package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.Gare;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.dao.repository.compagnie.GareRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.GareDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.GareTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
public class GareBusiness implements IBasicBusiness<Request<GareDTO>, Response<GareDTO>> {


    private Response<GareDTO> response;

    private final CompagnieTransportRepository compagnieTransportRepository;
    private final GareRepository gareRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public GareBusiness(CompagnieTransportRepository compagnieTransportRepository, GareRepository gareRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.gareRepository = gareRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<GareDTO> create(Request<GareDTO> request, Locale locale) throws ParseException {
        Response<GareDTO> response = new Response<GareDTO>();
        List<Gare> items = new ArrayList<Gare>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<GareDTO> itemsDtos = Collections.synchronizedList(new ArrayList<GareDTO>());
        for(GareDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("email", dto.getEmail());
            fieldsToVerify.put("telephone1", dto.getTelephone1());
            fieldsToVerify.put("telephone2", dto.getTelephone2());
            fieldsToVerify.put("adresseLocalisation", dto.getAdresseLocalisation());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de la designation'" + dto.getDesignation(), locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(GareDTO itemDto : itemsDtos){
            Gare existingGare = null;
            existingGare = gareRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingGare != null) {
                response.setStatus(functionalError.DATA_EXIST("La Gare ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieTransport existingCompagnieTransport = null;
            existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport ayant  pour raison sociale -> " + itemDto.getCompagnieTransportRaisonSociale() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (existingCompagnieTransport.getIsValidate()==null || existingCompagnieTransport.getIsValidate() == false) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Impossible de créer une gare.Compagnie de transport non validée", locale));
                response.setHasError(true);
                return response;
            }
            Gare entityToSave = GareTransformer.INSTANCE.toEntity(itemDto,existingCompagnieTransport);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }
        List<Gare> itemsSaved = null;
        itemsSaved = gareRepository.saveAll((Iterable<Gare>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<GareDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? GareTransformer.INSTANCE.toLiteDtos(itemsSaved)
                : GareTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<GareDTO> update(Request<GareDTO> request, Locale locale) throws ParseException {
        Response<GareDTO> response = new Response<GareDTO>();
        List<Gare> items = new ArrayList<Gare>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<GareDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<GareDTO>());
        for(GareDTO dto: request.getDatas() ) {
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
        for(GareDTO dto: itemsDtos) {
            Gare entityToSave = gareRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La gare suivante -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                Gare existingGare = gareRepository.findByDesignation(dto.getDesignation(), false);
                if (existingGare != null && !existingGare.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Gare -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String raisonSociale=entityToSave.getCompagnieTransport().getRaisonSociale()!=null
                                      ?entityToSave.getCompagnieTransport().getRaisonSociale()
                                      :null;
            CompagnieTransport existingCompagnieRansport = compagnieTransportRepository.findByRaisonSociale(raisonSociale,false);
            if (existingCompagnieRansport == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voyage du prix de l'offre de voyage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            //Autres
            if(Utilities.isNotBlank(dto.getEmail()) && !dto.getEmail().equals(entityToSave.getEmail())){
                entityToSave.setEmail(dto.getEmail());
            }
            if(Utilities.isNotBlank(dto.getTelephone1()) && !dto.getTelephone1().equals(entityToSave.getTelephone1())){
                entityToSave.setTelephone1(dto.getTelephone1());
            }
            if(Utilities.isNotBlank(dto.getTelephone2()) && !dto.getEmail().equals(entityToSave.getTelephone2())){
                entityToSave.setTelephone2(dto.getTelephone2());
            }
            if(Utilities.isNotBlank(dto.getAdresseLocalisation()) && !dto.getAdresseLocalisation().equals(entityToSave.getAdresseLocalisation())){
                entityToSave.setAdresseLocalisation(dto.getAdresseLocalisation());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Modification échouée.Aucune donnée",locale));
            response.setHasError(true);
            return response;
        }
        List<GareDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? GareTransformer.INSTANCE.toLiteDtos(items)
                : GareTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update gare-----");
        return response;
    }

    @Override
    public Response<GareDTO> delete(Request<GareDTO> request, Locale locale) {

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
    public Response<GareDTO> forceDelete(Request<GareDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<GareDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<GareDTO> getByCriteria(Request<GareDTO> request, Locale locale) {
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

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<GareDTO> getGareByCompagnieTransportRaisonScoiale(Request<GareDTO> request, Locale locale) throws ParseException {
        Response<GareDTO> response = new Response<GareDTO>();
        List<Gare> items = new ArrayList<Gare>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("compagnieTransportRaisonSociale", request.getData().getCompagnieTransportRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String raisonSociale=request.getData().getCompagnieTransportRaisonSociale();
        CompagnieTransport existingCompagnieTransport = null;
        existingCompagnieTransport= compagnieTransportRepository.findByRaisonSociale(raisonSociale,false);
        if (existingCompagnieTransport == null) {
            response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        items =gareRepository.findByCompagnieTransportRaisonSociale(raisonSociale,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport ne dispose d'aucune gare", locale));
            response.setHasError(true);
            return response;
        }
        List<GareDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? GareTransformer.INSTANCE.toLiteDtos(items)
                : GareTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end Listing gare-----");
        return response;
    }
}