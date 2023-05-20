package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.CompagnieTransportBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.CompagnieTransportDTO;
import com.africanb.africanb.helper.dto.compagnie.PaysDTO;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
import com.africanb.africanb.helper.dto.document.DocumentDTO;
import com.africanb.africanb.helper.dto.document.DocumentReponseDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import com.africanb.africanb.rest.fact.ControllerFactory;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/compagnieTransport")
public class CompagnieTransportController {

    @Autowired
    private ControllerFactory<CompagnieTransportDTO> controllerFactory;
    @Autowired
    private CompagnieTransportBusiness compagnieTransportBusiness;

    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<CompagnieTransportDTO> create(@RequestBody Request<CompagnieTransportDTO> request) {
        log.info("start method create");
        Response<CompagnieTransportDTO> response = controllerFactory.create(compagnieTransportBusiness, request, FunctionalityEnum.CREATE_COMPAGNIETRANSPORT);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<CompagnieTransportDTO> update(@RequestBody Request<CompagnieTransportDTO> request) {
        log.info("start method update");
        Response<CompagnieTransportDTO> response = controllerFactory.update(compagnieTransportBusiness, request, FunctionalityEnum.UPDATE_COMPAGNIETRANSPORT);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<CompagnieTransportDTO> delete(@RequestBody Request<CompagnieTransportDTO> request) {
        log.info("start method delete");
        Response<CompagnieTransportDTO> response = controllerFactory.delete(compagnieTransportBusiness, request, FunctionalityEnum.DELETE_COMPAGNIETRANSPORT);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<CompagnieTransportDTO> getByCriteria(@RequestBody Request<CompagnieTransportDTO> request) {
        log.info("start method /pays/getByCriteria");
        Response<CompagnieTransportDTO> response = controllerFactory.getByCriteria(compagnieTransportBusiness, request, FunctionalityEnum.VIEW_COMPAGNIETRANSPORT);
        log.info("end method /pays/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getAllProcessingCompagnies",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<CompagnieTransportDTO> getAllProcessingCompagnies(@RequestBody Request<CompagnieTransportDTO> request) {
        log.info("start method getAllProcessingCompagnies");
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=compagnieTransportBusiness.getAllProcessingCompagnies(request,locale);
            if(response.isHasError()){
                log.info(String.format("Erreur | code: {}",response.getStatus(),response.getStatus().getMessage()));
            }
            log.info(String.format("Code: {} - message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
        }catch (CannotCreateTransactionException e){
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response,locale,e);
        }catch (TransactionSystemException e){
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response,locale,e);
        }catch (RuntimeException e){
            exceptionUtils.RUNTIME_EXCEPTION(response,locale,e);
        }catch (Exception e){
            exceptionUtils.EXCEPTION(response,locale,e);
        }
        return response;
    }

    @RequestMapping(value="/getAllValidedCompagnies",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<CompagnieTransportDTO> getAllValidedCompagnies(@RequestBody Request<CompagnieTransportDTO> request) {
        log.info("start method getAllValidedCompagnies");
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=compagnieTransportBusiness.getAllValidedCompagnies(request,locale);
            if(response.isHasError()){
                log.info(String.format("Erreur | code: {}",response.getStatus(),response.getStatus().getMessage()));
            }
            log.info(String.format("Code: {} - message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
        }catch (CannotCreateTransactionException e){
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response,locale,e);
        }catch (TransactionSystemException e){
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response,locale,e);
        }catch (RuntimeException e){
            exceptionUtils.RUNTIME_EXCEPTION(response,locale,e);
        }catch (Exception e){
            exceptionUtils.EXCEPTION(response,locale,e);
        }
        return response;
    }

    @RequestMapping(value="/validateAdhesionRequestCompagny",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<CompagnieTransportDTO> validateAdhesionRequestCompagny(@RequestBody Request<CompagnieTransportDTO> request) {
        log.info("start method validateAdhesionRequestCompagny");
        Response<CompagnieTransportDTO> response = new Response<CompagnieTransportDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=compagnieTransportBusiness.validateAdhesionRequestCompagny(request,locale);
            if(response.isHasError()){
                log.info(String.format("Erreur | code: {}",response.getStatus(),response.getStatus().getMessage()));
            }
            log.info(String.format("Code: {} - message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
        }catch (CannotCreateTransactionException e){
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response,locale,e);
        }catch (TransactionSystemException e){
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response,locale,e);
        }catch (RuntimeException e){
            exceptionUtils.RUNTIME_EXCEPTION(response,locale,e);
        }catch (Exception e){
            exceptionUtils.EXCEPTION(response,locale,e);
        }
        return response;
    }

    @RequestMapping(value="/uploadAttestionTransport",method= RequestMethod.POST,consumes = {"multipart/form-data"},produces={"application/json"})
    public Response<DocumentDTO> uploadAttestionTransport(@RequestParam("file") MultipartFile file, @RequestParam String raisonSociale) {
        Response<DocumentDTO> response = new Response<DocumentDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        //Initialisation CompagnieTransportDTO
        Request<CompagnieTransportDTO> request = new Request<CompagnieTransportDTO>();
        CompagnieTransportDTO compagnieTransportDTO= new CompagnieTransportDTO();
        log.info(" Affichage de la raison sociale ="+raisonSociale); //TODO A effacer
        compagnieTransportDTO.setRaisonSociale(raisonSociale);
        request.setData(compagnieTransportDTO);
        Locale locale = new Locale(languageID, "");
        try{
            response=compagnieTransportBusiness.uploadAttestionTransport(request,file,locale);
            if(response.isHasError()){
                log.info(String.format("Erreur | code: {}",response.getStatus(),response.getStatus().getMessage()));
            }
            log.info(String.format("Code: {} - message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
        }catch (CannotCreateTransactionException e){
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response,locale,e);
        }catch (TransactionSystemException e){
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response,locale,e);
        }catch (RuntimeException e){
            exceptionUtils.RUNTIME_EXCEPTION(response,locale,e);
        }catch (Exception e){
            exceptionUtils.EXCEPTION(response,locale,e);
        }
        return response;
    }

    @RequestMapping(value="/getLinkedAttestionTransport",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<DocumentReponseDTO> getLinkedAttestionTransport(@RequestBody Request<CompagnieTransportDTO> request) {
        Response<DocumentReponseDTO> response = new Response<DocumentReponseDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        //Initialisation CompagnieTransportDTO
        Locale locale = new Locale(languageID, "");
        try{
            response=compagnieTransportBusiness.getLinkedAttestionTransport(request,locale);
            if(response.isHasError()){
                log.info(String.format("Erreur | code: {}",response.getStatus(),response.getStatus().getMessage()));
            }
            log.info(String.format("Code: {} - message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
        }catch (CannotCreateTransactionException e){
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response,locale,e);
        }catch (TransactionSystemException e){
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response,locale,e);
        }catch (RuntimeException e){
            exceptionUtils.RUNTIME_EXCEPTION(response,locale,e);
        }catch (Exception e){
            exceptionUtils.EXCEPTION(response,locale,e);
        }
        return response;
    }
}
