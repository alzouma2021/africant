package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.ModePaiement.ModePaiementBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModePaiement.ModePaiementDTO;
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
@RequestMapping(value="/modePaiements")
public class ModePaiementController {

    private final ControllerFactory<ModePaiementDTO> controllerFactory;
    private final ModePaiementBusiness modePaiementBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public ModePaiementController(ControllerFactory<ModePaiementDTO> controllerFactory, ModePaiementBusiness modePaiementBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.modePaiementBusiness = modePaiementBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ModePaiementDTO> create(@RequestBody Request<ModePaiementDTO> request) {
        log.info("start method create");
        Response<ModePaiementDTO> response = controllerFactory.create(modePaiementBusiness, request, FunctionalityEnum.CREATE_PRIXOFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ModePaiementDTO> update(@RequestBody Request<ModePaiementDTO> request) {
        log.info("start method update");
        Response<ModePaiementDTO> response = controllerFactory.update(modePaiementBusiness, request, FunctionalityEnum.UPDATE_PRIXOFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ModePaiementDTO> delete(@RequestBody Request<ModePaiementDTO> request) {
        log.info("start method delete");
        Response<ModePaiementDTO> response = controllerFactory.delete(modePaiementBusiness, request, FunctionalityEnum.DELETE_PRIXOFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ModePaiementDTO> getByCriteria(@RequestBody Request<ModePaiementDTO> request) {
        log.info("start method /programme/getByCriteria");
        Response<ModePaiementDTO> response = controllerFactory.getByCriteria(modePaiementBusiness, request, FunctionalityEnum.VIEW_PRIXOFFREVOYAGE);
        log.info("end method /programme/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getModePaiementByCompagnieTransport",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ModePaiementDTO> getModePaiementByCompagnieTransport(@RequestBody Request<ModePaiementDTO> request) {
        log.info("start method validateAdhesionRequestCompagny");
        Response<ModePaiementDTO> response = new Response<ModePaiementDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= modePaiementBusiness.getModePaiementByCompagnieTransport(request,locale);
            log.info("_84 Affichage de la reponse ="+response.getItems().toString());
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