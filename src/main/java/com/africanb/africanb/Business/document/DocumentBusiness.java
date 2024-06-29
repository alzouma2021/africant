package com.africanb.africanb.Business.document;

import com.africanb.africanb.dao.entity.document.Document;
import com.africanb.africanb.dao.repository.document.DocumentRepository;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.IBasicBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.document.DocumentDTO;
import com.africanb.africanb.helper.searchFunctions.Utilities;
import com.africanb.africanb.helper.transformer.document.DocumentTransformer;
import com.africanb.africanb.helper.validation.Validate;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Log
@Component
public class DocumentBusiness implements IBasicBusiness<Request<DocumentDTO>, Response<DocumentDTO>> {


    private Response<DocumentDTO> response;

    private final FunctionalError functionalError;
    private final DocumentRepository documentRepository;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final EntityManager em;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat dateTimeFormat;

    public DocumentBusiness(FunctionalError functionalError, DocumentRepository documentRepository, TechnicalError technicalError, ExceptionUtils exceptionUtils, EntityManager em) {
        this.functionalError = functionalError;
        this.documentRepository = documentRepository;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.em = em;
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    @Override
    public Response<DocumentDTO> create(Request<DocumentDTO> request, Locale locale) throws ParseException {
        Response<DocumentDTO> response = new Response<>();
        if(request.getData()==null){
            response.setStatus(functionalError.DATA_NOT_EXIST("Aucun document défini",locale));
            response.setHasError(true);
            return response;
        }
        DocumentDTO dto=request.getData();
        Map<String, Object> fieldsToVerify = new HashMap<>();
        fieldsToVerify.put("designation", dto.getDesignation());
        fieldsToVerify.put("path", dto.getPath());
        fieldsToVerify.put("typeMime", dto.getTypeMime());
        fieldsToVerify.put("extension", dto.getExtension());
        if (!Validate.RequiredValue(fieldsToVerify).isGood()) {
            response.setStatus(functionalError.FIELD_EMPTY(Validate.getValidate().getField(), locale));
            response.setHasError(true);
            return response;
        }
        Document existingDocument = documentRepository.findByDesignation(dto.getDesignation(), false);
        if (existingDocument != null) {
            response.setStatus(functionalError.DATA_EXIST("Document existe dèja", locale));
            response.setHasError(true);
            return response;
        }
        Document entityToSave = DocumentTransformer.INSTANCE.toEntity(dto);
        entityToSave.setIsDeleted(false);
        entityToSave.setCreatedAt(Utilities.getCurrentDate());
        Document itemsSaved = documentRepository.save(entityToSave);
        DocumentDTO itemDto = (Utilities.isTrue(request.getIsSimpleLoading()))
                                    ? DocumentTransformer.INSTANCE.toLiteDto(itemsSaved)
                                    : DocumentTransformer.INSTANCE.toDto(itemsSaved);

        response.setItem(itemDto);
        response.setHasError(false);
        response.setStatus(functionalError.SUCCESS("", locale));

        return response;
    }

    @Override
    public Response<DocumentDTO> update(Request<DocumentDTO> request, Locale locale) throws ParseException {
      return null;
    }



    @Override
    public Response<DocumentDTO> delete(Request<DocumentDTO> request, Locale locale) {
        return null;
    }

    @Override
    public Response<DocumentDTO> forceDelete(Request<DocumentDTO> request, Locale locale) {
        return null ;
    }

    @Override
    public Response<DocumentDTO> getAll(Locale locale) throws ParseException {
        return null;
    }

    @Override
    public Response<DocumentDTO> getByCriteria(Request<DocumentDTO> request, Locale locale) {
        return null;
    }
}
