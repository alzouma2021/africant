package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.ProprieteOffreVoyageBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.ProprieteOffreVoyageDTO;
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
@RequestMapping(value="/proprieteOffreVoyages")
public class ProprieteOffreVoyageController {


    private final ControllerFactory<ProprieteOffreVoyageDTO> controllerFactory;
    private final ProprieteOffreVoyageBusiness proprieteOffreVoyageBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public ProprieteOffreVoyageController(ControllerFactory<ProprieteOffreVoyageDTO> controllerFactory, ProprieteOffreVoyageBusiness proprieteOffreVoyageBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.proprieteOffreVoyageBusiness = proprieteOffreVoyageBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ProprieteOffreVoyageDTO> create(@RequestBody Request<ProprieteOffreVoyageDTO> request) {
        log.info("start method create");
        Response<ProprieteOffreVoyageDTO> response = controllerFactory.create(proprieteOffreVoyageBusiness, request, FunctionalityEnum.CREATE_JOURSEMAINE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ProprieteOffreVoyageDTO> update(@RequestBody Request<ProprieteOffreVoyageDTO> request) {
        log.info("start method update");
        Response<ProprieteOffreVoyageDTO> response = controllerFactory.update(proprieteOffreVoyageBusiness, request, FunctionalityEnum.UPDATE_JOURSEMAINE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ProprieteOffreVoyageDTO> delete(@RequestBody Request<ProprieteOffreVoyageDTO> request) {
        log.info("start method delete");
        Response<ProprieteOffreVoyageDTO> response = controllerFactory.delete(proprieteOffreVoyageBusiness, request, FunctionalityEnum.DELETE_JOURSEMAINE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ProprieteOffreVoyageDTO> getByCriteria(@RequestBody Request<ProprieteOffreVoyageDTO> request) {
        log.info("start method /jourSemaine/getByCriteria");
        Response<ProprieteOffreVoyageDTO> response = controllerFactory.getByCriteria(proprieteOffreVoyageBusiness, request, FunctionalityEnum.VIEW_JOURSEMAINE);
        log.info("end method /jourSemaine/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getAllProprieteOffreVoyage",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ProprieteOffreVoyageDTO> getAllProprieteOffreVoyage(@RequestBody Request<ProprieteOffreVoyageDTO> request) {
        Response<ProprieteOffreVoyageDTO> response = new Response<ProprieteOffreVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=proprieteOffreVoyageBusiness.getAllProprieteOffreVoyage(request,locale);
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
