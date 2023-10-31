package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.PrixOffreVoyageBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
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
@RequestMapping(value="/prixOffreVoyages")
public class PrixOffreVoyageController {


    private final ControllerFactory<PrixOffreVoyageDTO> controllerFactory;
    private final PrixOffreVoyageBusiness prixOffreVoyageBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public PrixOffreVoyageController(ControllerFactory<PrixOffreVoyageDTO> controllerFactory, PrixOffreVoyageBusiness prixOffreVoyageBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.prixOffreVoyageBusiness = prixOffreVoyageBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<PrixOffreVoyageDTO> create(@RequestBody Request<PrixOffreVoyageDTO> request) {
        log.info("start method create");
        Response<PrixOffreVoyageDTO> response = controllerFactory.create(prixOffreVoyageBusiness, request, FunctionalityEnum.CREATE_PRIXOFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<PrixOffreVoyageDTO> update(@RequestBody Request<PrixOffreVoyageDTO> request) {
        log.info("start method update");
        Response<PrixOffreVoyageDTO> response = controllerFactory.update(prixOffreVoyageBusiness, request, FunctionalityEnum.UPDATE_PRIXOFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<PrixOffreVoyageDTO> delete(@RequestBody Request<PrixOffreVoyageDTO> request) {
        log.info("start method delete");
        Response<PrixOffreVoyageDTO> response = controllerFactory.delete(prixOffreVoyageBusiness, request, FunctionalityEnum.DELETE_PRIXOFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<PrixOffreVoyageDTO> getByCriteria(@RequestBody Request<PrixOffreVoyageDTO> request) {
        log.info("start method /prixOffreVoyage/getByCriteria");
        Response<PrixOffreVoyageDTO> response = controllerFactory.getByCriteria(prixOffreVoyageBusiness, request, FunctionalityEnum.VIEW_PRIXOFFREVOYAGE);
        log.info("end method /prixOffreVoyage/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getPrixTravelOfferByOffreVoyageDesignation",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<PrixOffreVoyageDTO> getPrixTravelOfferByOffreVoyageDesignation(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method getPrixTravelOfferByOffreVoyageDesignation");
        Response<PrixOffreVoyageDTO> response = new Response<PrixOffreVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=prixOffreVoyageBusiness.getPrixTravelOfferByOffreVoyageDesignation(request,locale);
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
