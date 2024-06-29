package com.africanb.africanb.Business.compagnie;


import com.africanb.africanb.dao.entity.compagnie.Bagage;
import com.africanb.africanb.dao.entity.compagnie.CompagnieTransport;
import com.africanb.africanb.dao.repository.Reference.ReferenceRepository;
import com.africanb.africanb.dao.repository.compagnie.BagageRepository;
import com.africanb.africanb.dao.repository.compagnie.CompagnieTransportRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.BagageDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.compagnie.BagageTransformer;
import com.africanb.africanb.helper.validation.Validate;
import com.africanb.africanb.utils.Reference.Reference;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



@Log
@Component
public class BagageBusiness implements IBasicBusiness<Request<BagageDTO>, Response<BagageDTO>> {


    private final ReferenceRepository referenceRepository;
    private final BagageRepository bagageRepository;
    private final CompagnieTransportRepository compagnieTransportRepository;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public BagageBusiness(ReferenceRepository referenceRepository, BagageRepository bagageRepository, CompagnieTransportRepository compagnieTransportRepository, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.referenceRepository = referenceRepository;
        this.bagageRepository = bagageRepository;
        this.compagnieTransportRepository = compagnieTransportRepository;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<BagageDTO> create(Request<BagageDTO> request, Locale locale) throws ParseException {
        Response<BagageDTO> response = new Response<>();
        List<Bagage> items = new ArrayList<>();
        if(request.getDatas() == null || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<BagageDTO> itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(BagageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
            fieldsToVerify.put("designation", dto.getDesignation());
            fieldsToVerify.put("coutBagageParTypeBagage", dto.getCoutBagageParTypeBagage());
            fieldsToVerify.put("nombreBagageGratuitParTypeBagage", dto.getNombreBagageGratuitParTypeBagage());
            fieldsToVerify.put("compagnieTransportRaisonSociale", dto.getCompagnieTransportRaisonSociale());
            fieldsToVerify.put("typeBagageDesignation", dto.getTypeBagageDesignation());
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
        for(BagageDTO itemDto : itemsDtos){
            Bagage existingBagage = bagageRepository.findByDesignation(itemDto.getDesignation(), false);
            if (existingBagage != null) {
                response.setStatus(functionalError.DATA_EXIST("Le Bagage ayant  pour designation -> " + itemDto.getDesignation() +", existe déjà", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieTransport existingCompagneTransport= compagnieTransportRepository.findByRaisonSociale(itemDto.getCompagnieTransportRaisonSociale(),false);
            if (existingCompagneTransport == null) {
                response.setStatus(functionalError.DATA_EXIST("La compagnie de transport ayant  pour raison sociale -> " + itemDto.getCompagnieTransportRaisonSociale() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeBagage = referenceRepository.findByDesignation(itemDto.getTypeBagageDesignation(),false);
            if (existingTypeBagage == null) {
                response.setStatus(functionalError.DATA_EXIST("Type bagage ayant  pour designation -> " + itemDto.getTypeBagageDesignation() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            Bagage entityToSave = BagageTransformer.INSTANCE.toEntity(itemDto,existingTypeBagage,existingCompagneTransport);
            entityToSave.setIsDeleted(false);
            entityToSave.setCreatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        List<Bagage> itemsSaved = bagageRepository.saveAll(items);
        if (CollectionUtils.isEmpty(itemsSaved)) {
            response.setStatus(functionalError.SAVE_FAIL("Erreur de creation", locale));
            response.setHasError(true);
            return response;
        }
        List<BagageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? BagageTransformer.INSTANCE.toLiteDtos(itemsSaved)
                                    : BagageTransformer.INSTANCE.toDtos(itemsSaved);
        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        return response;
    }

    @Override
    public Response<BagageDTO> update(Request<BagageDTO> request, Locale locale) throws ParseException {
        Response<BagageDTO> response = new Response<>();
        List<Bagage> items = new ArrayList<>();
        if(request.getDatas() == null  || request.getDatas().isEmpty()){
            response.setStatus(functionalError.DATA_NOT_EXIST("Liste vide",locale));
            response.setHasError(true);
            return response;
        }
        List<BagageDTO>itemsDtos =  Collections.synchronizedList(new ArrayList<>());
        for(BagageDTO dto: request.getDatas() ) {
            Map<String, Object> fieldsToVerify = new HashMap<>();
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
        for(BagageDTO dto: itemsDtos) {
            Bagage entityToSave = bagageRepository.findOne(dto.getId(), false);
            if (entityToSave == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le bagage ayant l'identifiant suivant -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getDesignation()) && !dto.getDesignation().equals(entityToSave.getDesignation())) {
                Bagage existingBagage = bagageRepository.findByDesignation(dto.getDesignation(), false);
                if (existingBagage != null && !existingBagage.getId().equals(entityToSave.getId())) {
                    response.setStatus(functionalError.DATA_EXIST("Bagage -> " + dto.getDesignation(), locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setDesignation(dto.getDesignation());
            }
            String typeBagageDesignation=entityToSave.getTypeBagage()!=null&&entityToSave.getTypeBagage().getDesignation()!=null
                                       ?entityToSave.getTypeBagage().getDesignation()
                                       :null;
            if(typeBagageDesignation==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Le bagage n'est lié à aucun type de bagage", locale));
                response.setHasError(true);
                return response;
            }
            Reference existingTypeBagage = referenceRepository.findByDesignation(typeBagageDesignation,false);
            if (existingTypeBagage == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("Le type de bagage -> " + dto.getId() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getTypeBagageDesignation()) && !dto.getTypeBagageDesignation().equals(existingTypeBagage.getDesignation())) {
                Reference typeBagageToSave = referenceRepository.findByDesignation(dto.getTypeBagageDesignation(), false);
                if (typeBagageToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("Le nouveau type bagage défini -> " + dto.getTypeBagageDesignation() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setTypeBagage(typeBagageToSave);
            }
            String compagnieTransportRaisonSociale=entityToSave.getCompagnieTransport()!=null&&entityToSave.getCompagnieTransport().getRaisonSociale()!=null
                    ?entityToSave.getCompagnieTransport().getRaisonSociale()
                    :null;
            if(compagnieTransportRaisonSociale==null){
                response.setStatus(functionalError.DATA_NOT_EXIST("Le bagage n'est rattaché à aucune compagnie", locale));
                response.setHasError(true);
                return response;
            }
            CompagnieTransport existingCompagnieTransport = compagnieTransportRepository.findByRaisonSociale(compagnieTransportRaisonSociale,false);
            if (existingCompagnieTransport == null) {
                response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport -> " + dto.getCompagnieTransportRaisonSociale() +", n'existe pas", locale));
                response.setHasError(true);
                return response;
            }
            if (Utilities.isNotBlank(dto.getCompagnieTransportRaisonSociale()) && !dto.getCompagnieTransportRaisonSociale().equals(existingCompagnieTransport.getRaisonSociale())) {
                CompagnieTransport CompgnaieTransportToSave = compagnieTransportRepository.findByRaisonSociale(dto.getCompagnieTransportRaisonSociale(), false);
                if (CompgnaieTransportToSave == null) {
                    response.setStatus(functionalError.DATA_NOT_EXIST("La nouvelle compagnie -> " + dto.getCompagnieTransportRaisonSociale() +", n'existe pas", locale));
                    response.setHasError(true);
                    return response;
                }
                entityToSave.setCompagnieTransport(CompgnaieTransportToSave);
            }
            if(Utilities.isNotBlank(dto.getDescription()) && !dto.getDescription().equals(entityToSave.getDescription())){
                entityToSave.setDescription(dto.getDescription());
            }
            if(Utilities.isValidID(dto.getCoutBagageParTypeBagage())&&!dto.getCoutBagageParTypeBagage().equals(entityToSave.getCoutBagageParTypeBagage())){
                entityToSave.setCoutBagageParTypeBagage(dto.getCoutBagageParTypeBagage());
            }
            if(Utilities.isValidID(dto.getNombreBagageGratuitParTypeBagage())&&!dto.getNombreBagageGratuitParTypeBagage().equals(entityToSave.getNombreBagageGratuitParTypeBagage())){
                entityToSave.setNombreBagageGratuitParTypeBagage(dto.getNombreBagageGratuitParTypeBagage());
            }
            entityToSave.setUpdatedAt(Utilities.getCurrentDate());
            items.add(entityToSave);
        }
        if(CollectionUtils.isEmpty(items)){
            response.setStatus(functionalError.DATA_NOT_EXIST("Modification échouée ",locale));
            response.setHasError(true);
            return response;
        }
        List<BagageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                ? BagageTransformer.INSTANCE.toLiteDtos(items)
                                : BagageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end update priix de l'offre de voyage-----");
        return response;
    }

    @Override
    public Response<BagageDTO> delete(Request<BagageDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<BagageDTO> forceDelete(Request<BagageDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<BagageDTO> getAll(Locale locale) throws ParseException {
       return null;
    }

    @Override
    public Response<BagageDTO> getByCriteria(Request<BagageDTO> request, Locale locale) {
        return null;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public Response<BagageDTO> getAllBagageByCompagnieTransportRaisonSociale(Request<BagageDTO> request, Locale locale) throws ParseException {
        Response<BagageDTO> response = new Response<>();
        if (request.getData() == null ) {
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucune donnée definie", locale));
            response.setHasError(true);
            return response;
        }
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("compagnieTransportRaisonSociale", request.getData().getCompagnieTransportRaisonSociale());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        String compagnieTransportRaisonSociale=request.getData().getCompagnieTransportRaisonSociale();
        CompagnieTransport existingCompagnieTransport= compagnieTransportRepository.findByRaisonSociale(compagnieTransportRaisonSociale,false);
        if (existingCompagnieTransport == null) {
            response.setStatus(functionalError.DATA_EXIST("La compagnie de transport n'existe pas", locale));
            response.setHasError(true);
            return response;
        }
        List<Bagage> items = bagageRepository.findByCompagnieTransportRaisonSociale(compagnieTransportRaisonSociale,false);
        if (CollectionUtils.isEmpty(items)) {
            response.setStatus(functionalError.DATA_NOT_EXIST("La compagnie de transport ne possede aucun bagage", locale));
            response.setHasError(true);
            return response;
        }

        List<BagageDTO> itemsDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                ? BagageTransformer.INSTANCE.toLiteDtos(items)
                : BagageTransformer.INSTANCE.toDtos(items);

        response.setItems(itemsDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));
        log.info("----end Listing bagage-----");
        return response;
    }
}
