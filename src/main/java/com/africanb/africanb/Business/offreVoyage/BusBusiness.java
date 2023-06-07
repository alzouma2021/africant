
package com.africanb.africanb.Business.offreVoyage;


import com.africanb.africanb.dao.entity.offreVoyage.Bus;
import com.africanb.africanb.dao.entity.offreVoyage.OffreVoyage;
import com.africanb.africanb.dao.repository.offreVoyage.BusRepository;
import com.africanb.africanb.dao.repository.offreVoyage.OffreVoyageRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.BusDTO;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.offrreVoyage.BusTransformer;
import com.africanb.africanb.helper.transformer.offrreVoyage.PrixOffreVoyageTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BusBusiness implements IBasicBusiness<Request<BusDTO>, Response<BusDTO>> {


    private Response<BusDTO> response;

    @Autowired
    private BusRepository busRepository;
    @Autowired
    private OffreVoyageRepository offreVoyageRepository;
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

    public BusBusiness() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<BusDTO> create(Request<BusDTO> request, Locale locale) throws ParseException {
        Response<BusDTO> response = new Response<BusDTO>();
        List<Bus> items = new ArrayList<Bus>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<BusDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<BusDTO>());
        for(BusDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("numeroBus", dto.getNumeroBus());
            fieldsToVerify.put("nombrePlace", dto.getNombrePlace());
            fieldsToVerify.put("offreVoyageDesignation", dto.getOffreVoyageDesignation());
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
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getNumeroBus()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication du numero bus'" + dto.getNumeroBus(), locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(BusDTO itemDto : itemsDtos){
            Bus existingBus = null;
            existingBus = busRepository.findByNumeroBus(itemDto.getDesignation(), false);
            if (existingBus != null) {
                response.setStatus(functionalError.DATA_EXIST("Le bus ayant le numéro -> " + itemDto.getNumeroBus() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            OffreVoyage existingOffreVoyage = null;
            existingOffreVoyage= offreVoyageRepository.findByDesignation(itemDto.getOffreVoyageDesignation(),false);
            if (existingOffreVoyage == null) {
                response.setStatus(functionalError.DATA_EXIST("L'offre de voyage ayant  pour identifiant -> " + itemDto.getOffreVoyageDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Bus entityToSave = BusTransformer.INSTANCE.toEntity(itemDto,existingOffreVoyage);
            log.info("_105 VilleDTO transform to Entity :: ="+ entityToSave.toString());
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            //entityToSave.setCreatedBy(request.user); // à modifier
            items.add(entityToSave);
        }
        List<Bus> itemsSaved = null;
        itemsSaved = busRepository.saveAll((Iterable<Bus>) items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<BusDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? BusTransformer.INSTANCE.toLiteDtos(itemsSaved)
                : BusTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<BusDTO> update(Request<BusDTO> request, Locale locale) throws ParseException {
        Response<BusDTO> response = new Response<BusDTO>();
        List<Bus> items = new ArrayList<Bus>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<BusDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<BusDTO>());
        for(BusDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
            fieldsToVerify.put("id", dto.getId());
            if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
                response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
                response.setHasError(true);
                return response;
            }
            if(itemsDtos.stream().anyMatch(a->a.getDesignation().equalsIgnoreCase(dto.getDesignation()))){
                response.setStatus(functionalError.DATA_DUPLICATE("Tentative de duplication de l'identifiant", locale));
                response.setHasError(true);
                return response;
            }
            itemsDtos.add(dto);
        }
        for(BusDTO dto: itemsDtos) {
            Bus entityToSave = busRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le bus ayant le numéro -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                Bus existingBus = busRepository.findByDesignation(dto.getDesignation(), false);
                if (existingBus != null && !existingBus.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Bus -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            if (Utilities.isNotBlank(dto.getNumeroBus()) && !dto.getDesignation().equals(entityToSave.getNumeroBus())) {
                Bus existingBus = busRepository.findByNumeroBus(dto.getNumeroBus(), false);
                if (existingBus != null && !existingBus.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Bus -> " + dto.getNumeroBus(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setNumeroBus(dto.getNumeroBus());
            }
            //Autres
            if(Utilities.isNotBlank(dto.getNumeroBus()) && !dto.getDescription().equals(entityToSave.getNumeroBus())){
                entityToSave.setNumeroBus(dto.getNumeroBus());
            }
            if(Utilities.isValidID(dto.getNombrePlace()) && !dto.getNombrePlace().equals(entityToSave.getNombrePlace())){
                entityToSave.setNombrePlace(dto.getNombrePlace());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            //entityToSave.setUpdatedBy(request.user);
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Modification échouée ",locale));
            response.setHasError(true);
            return response;
        }
        List<BusDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? BusTransformer.INSTANCE.toLiteDtos(items)
                : BusTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update priix de l'offre de voyage-----");
        return response;
    }

    @Override
    public Response<BusDTO> delete(Request<BusDTO> request, Locale locale) {

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
    public Response<BusDTO> forceDelete(Request<BusDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<BusDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<BusDTO> getByCriteria(Request<BusDTO> request, Locale locale) {
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
    public Response<BusDTO> getBusByOffreVoyage(Request<BusDTO> request, Locale locale) throws ParseException {
        Response<BusDTO> response = new Response<BusDTO>();
        List<Bus> items = new ArrayList<Bus>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<String, Object>();
        fieldsToVerify.put("offrVoyageDesigntaion", request.getData().getDesignation());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String offreVoyageDesignation=request.getData().getDesignation();
        OffreVoyage existingOffreVoyage = null;
        existingOffreVoyage= offreVoyageRepository.findByDesignation(offreVoyageDesignation,false);
        if (existingOffreVoyage == null) {
            response.setStatus(functionalError.DATA_EXIST("L'offre de voyage n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        items = busRepository.findByOffreVoyageDesignation(offreVoyageDesignation,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("L'offre de voayage ne dispose d'aucun prix", locale));
            response.setHasError(true);
            return response;
        }
        List<BusDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? BusTransformer.INSTANCE.toLiteDtos(items)
                : BusTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end Listing prix offre voyage-----");
        return response;
    }
}