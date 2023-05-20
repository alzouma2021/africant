package com.africanb.africanb.Business.compagnie.ModePaiement;


import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementEnEspece;
import com.africanb.africanb.dao.entity.compagnie.ModePaiment.ModePaiementMoovMoney;
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
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementMoovMoneyDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.ModePaiement.ModePaimentEnEspeceTransformer;
import com.africanb.africanb.helper.transformer.compagnie.ModePaiement.ModePaimentMoovMoneyTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Reference.Reference;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ModePaiementEnEspeceRepository modePaiementEnEspeceRepository;
    @Autowired
    private FunctionalError functionalError;
    @Autowired
    CompagnieTransportRepository compagnieTransportRepository;
    @Autowired
    ReferenceRepository typeModePaiementRepository;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private EntityManager em;

    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public ModePaiementEnEspeceBusiness() {
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
    public Response<ModePaiementEnEspeceDTO> forceDelete(Request<ModePaiementEnEspeceDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<ModePaiementEnEspeceDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<ModePaiementEnEspeceDTO> getByCriteria(Request<ModePaiementEnEspeceDTO> request, Locale locale) {
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
