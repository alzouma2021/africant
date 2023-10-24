package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.OffreVoyageBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.CompagnieTransportDTO;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.RechercheCritereOffreVoyageDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import com.africanb.africanb.rest.fact.ControllerFactory;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/offreVoyages")
public class OffreVoyageController {

    @Autowired
    private ControllerFactory<OffreVoyageDTO> controllerFactory;
    @Autowired
    private OffreVoyageBusiness offreVoyageBusiness;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<OffreVoyageDTO> create(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method create");
        Response<OffreVoyageDTO> response = controllerFactory.create(offreVoyageBusiness, request, FunctionalityEnum.CREATE_OFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<OffreVoyageDTO> update(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method update");
        Response<OffreVoyageDTO> response = controllerFactory.update(offreVoyageBusiness, request, FunctionalityEnum.UPDATE_OFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<OffreVoyageDTO> delete(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method delete");
        Response<OffreVoyageDTO> response = controllerFactory.delete(offreVoyageBusiness, request, FunctionalityEnum.DELETE_OFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<OffreVoyageDTO> getByCriteria(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method /offreVoyage/getByCriteria");
        Response<OffreVoyageDTO> response = controllerFactory.getByCriteria(offreVoyageBusiness, request, FunctionalityEnum.VIEW_OFFREVOYAGE);
        log.info("end method /offreVoyage/getByCriteria");
        return response;
    }

    @RequestMapping(value="/toActiveTravelOffer",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<Boolean> validateAdhesionRequestCompagny(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method validateAdhesionRequestCompagny");
        Response<Boolean> response = new Response<Boolean>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=offreVoyageBusiness.toActiveTravelOffer(request,locale);
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


    @RequestMapping(value="/getTravelOfferByCompagnieTransport",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<OffreVoyageDTO> getTravelOfferByCompagnieTransport(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method validateAdhesionRequestCompagny");
        Response<OffreVoyageDTO> response = new Response<OffreVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=offreVoyageBusiness.getTravelOfferByCompagnieTransport(request,locale);
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


    @RequestMapping(value="/getOffreVoyageByCriteria",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<OffreVoyageDTO> getOffreVoyageByCriteria(@RequestBody Request<RechercheCritereOffreVoyageDTO> request) {
        Response<OffreVoyageDTO> response = new Response<OffreVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=offreVoyageBusiness.getOffreVoyageByCriteria(request,locale);
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
