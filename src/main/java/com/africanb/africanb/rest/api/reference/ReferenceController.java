package com.africanb.africanb.rest.api.reference;


import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import com.africanb.africanb.rest.fact.ControllerFactory;
import com.africanb.africanb.utils.Reference.RechercherReferenceDTO;
import com.africanb.africanb.utils.Reference.ReferenceBusines;
import com.africanb.africanb.utils.Reference.ReferenceDTO;
import lombok.extern.java.Log;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/references")
public class ReferenceController {

    private final ControllerFactory<ReferenceDTO> controllerFactory;
    private final ReferenceBusines referenceBusines;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public ReferenceController(ControllerFactory<ReferenceDTO> controllerFactory, ReferenceBusines referenceBusines, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.referenceBusines = referenceBusines;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceDTO> create(@RequestBody Request<ReferenceDTO> request) {
        log.info("start method create");
        Response<ReferenceDTO> response = controllerFactory.create(referenceBusines, request, FunctionalityEnum.CREATE_REFERENCE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceDTO> update(@RequestBody Request<ReferenceDTO> request) {
        log.info("start method update");
        Response<ReferenceDTO> response = controllerFactory.update(referenceBusines, request, FunctionalityEnum.UPDATE_REFERENCE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceDTO> delete(@RequestBody Request<ReferenceDTO> request) {
        log.info("start method delete");
        Response<ReferenceDTO> response = controllerFactory.delete(referenceBusines, request, FunctionalityEnum.DELETE_REFERENCE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceDTO> getByCriteria(@RequestBody Request<ReferenceDTO> request) {
        log.info("start method /famillestatusutil/getByCriteria");
        Response<ReferenceDTO> response = controllerFactory.getByCriteria(referenceBusines, request, FunctionalityEnum.VIEW_REFERENCE);
        log.info("end method /famillestatusutil/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getReferenceByReferenceFamille",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceDTO> getReferenceByReferenceFamille(@RequestBody Request<RechercherReferenceDTO> request) {
        log.info("start method create");
        Response<ReferenceDTO> response = new Response<ReferenceDTO>();
        requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("La langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try{
            response=referenceBusines.getReferenceByReferenceFamilleDesignation(request,locale);
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
