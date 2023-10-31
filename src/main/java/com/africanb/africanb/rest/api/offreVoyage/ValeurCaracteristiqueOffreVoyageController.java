package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.ValeurCaracteristiqueOffreVoyageBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ValeurCaracteristiqueOffreVoyageDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import com.africanb.africanb.rest.fact.ControllerFactory;
import lombok.extern.java.Log;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/valeurCaracteristiqueOffreVoyages")
public class ValeurCaracteristiqueOffreVoyageController {


    private final ControllerFactory<ValeurCaracteristiqueOffreVoyageDTO> controllerFactory;
    private final ValeurCaracteristiqueOffreVoyageBusiness valeurCaracteristiqueOffreVoyageBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public ValeurCaracteristiqueOffreVoyageController(ControllerFactory<ValeurCaracteristiqueOffreVoyageDTO> controllerFactory, ValeurCaracteristiqueOffreVoyageBusiness valeurCaracteristiqueOffreVoyageBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.valeurCaracteristiqueOffreVoyageBusiness = valeurCaracteristiqueOffreVoyageBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ValeurCaracteristiqueOffreVoyageDTO> create(@RequestBody Request<ValeurCaracteristiqueOffreVoyageDTO> request) {
        log.info("start method create");
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = controllerFactory.create(valeurCaracteristiqueOffreVoyageBusiness, request, FunctionalityEnum.CREATE_PRIXOFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ValeurCaracteristiqueOffreVoyageDTO> update(@RequestBody Request<ValeurCaracteristiqueOffreVoyageDTO> request) {
        log.info("start method update");
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = controllerFactory.update(valeurCaracteristiqueOffreVoyageBusiness, request, FunctionalityEnum.UPDATE_PRIXOFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ValeurCaracteristiqueOffreVoyageDTO> delete(@RequestBody Request<ValeurCaracteristiqueOffreVoyageDTO> request) {
        log.info("start method delete");
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = controllerFactory.delete(valeurCaracteristiqueOffreVoyageBusiness, request, FunctionalityEnum.DELETE_PRIXOFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getByCriteria(@RequestBody Request<ValeurCaracteristiqueOffreVoyageDTO> request) {
        log.info("start method /programme/getByCriteria");
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = controllerFactory.getByCriteria(valeurCaracteristiqueOffreVoyageBusiness, request, FunctionalityEnum.VIEW_PRIXOFFREVOYAGE);
        log.info("end method /programme/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getAllValeurCaracteristiqueOffreVoyageByOffreVoyageDesignation",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ValeurCaracteristiqueOffreVoyageDTO> getAllValeurCaracteristiqueOffreVoyageByOffreVoyageDesignation(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method validateAdhesionRequestCompagny");
        Response<ValeurCaracteristiqueOffreVoyageDTO> response = new Response<ValeurCaracteristiqueOffreVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=valeurCaracteristiqueOffreVoyageBusiness.getAllValeurCaracteristiqueOffreVoyageByOffreVoyageDesignation(request,locale);
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