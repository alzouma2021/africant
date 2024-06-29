package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.JourSemaineBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.JourSemaineDTO;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import com.africanb.africanb.rest.fact.ControllerFactory;
import lombok.extern.java.Log;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/jourSemaines")
public class JourSemaineController {

    private final ControllerFactory<JourSemaineDTO> controllerFactory;
    private final JourSemaineBusiness jourSemaineBusiness;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public JourSemaineController(ControllerFactory<JourSemaineDTO> controllerFactory, JourSemaineBusiness jourSemaineBusiness, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.jourSemaineBusiness = jourSemaineBusiness;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<JourSemaineDTO> create(@RequestBody Request<JourSemaineDTO> request) {
        log.info("start method create");
        Response<JourSemaineDTO> response = controllerFactory.create(jourSemaineBusiness, request, FunctionalityEnum.CREATE_JOURSEMAINE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<JourSemaineDTO> update(@RequestBody Request<JourSemaineDTO> request) {
        log.info("start method update");
        Response<JourSemaineDTO> response = controllerFactory.update(jourSemaineBusiness, request, FunctionalityEnum.UPDATE_JOURSEMAINE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<JourSemaineDTO> delete(@RequestBody Request<JourSemaineDTO> request) {
        log.info("start method delete");
        Response<JourSemaineDTO> response = controllerFactory.delete(jourSemaineBusiness, request, FunctionalityEnum.DELETE_JOURSEMAINE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<JourSemaineDTO> getByCriteria(@RequestBody Request<JourSemaineDTO> request) {
        log.info("start method /jourSemaine/getByCriteria");
        Response<JourSemaineDTO> response = controllerFactory.getByCriteria(jourSemaineBusiness, request, FunctionalityEnum.VIEW_JOURSEMAINE);
        log.info("end method /jourSemaine/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getJourSemaineByVoyageDesignation",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<JourSemaineDTO> getJourSemaineByVoyageDesignation(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method getJourSemaineByVoyageDesignation");
        Response<JourSemaineDTO> response = new Response<>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=jourSemaineBusiness.getJourSemaineByVoyageDesignation(request,locale);
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
