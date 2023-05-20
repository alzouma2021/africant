package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.ModeAbonnementBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.ModeAbonnement.ModeAbonnementDTO;
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
@RequestMapping(value="/modeAbonnements")
public class ModeAbonnementController {

    @Autowired
    private ControllerFactory<ModeAbonnementDTO> controllerFactory;
    @Autowired
    private ModeAbonnementBusiness modeAbonnementBusiness;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ModeAbonnementDTO> create(@RequestBody Request<ModeAbonnementDTO> request) {
        log.info("start method create");
        Response<ModeAbonnementDTO> response = controllerFactory.create(modeAbonnementBusiness, request, FunctionalityEnum.CREATE_PRIXOFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ModeAbonnementDTO> update(@RequestBody Request<ModeAbonnementDTO> request) {
        log.info("start method update");
        Response<ModeAbonnementDTO> response = controllerFactory.update(modeAbonnementBusiness, request, FunctionalityEnum.UPDATE_PRIXOFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ModeAbonnementDTO> delete(@RequestBody Request<ModeAbonnementDTO> request) {
        log.info("start method delete");
        Response<ModeAbonnementDTO> response = controllerFactory.delete(modeAbonnementBusiness, request, FunctionalityEnum.DELETE_PRIXOFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ModeAbonnementDTO> getByCriteria(@RequestBody Request<ModeAbonnementDTO> request) {
        log.info("start method /programme/getByCriteria");
        Response<ModeAbonnementDTO> response = controllerFactory.getByCriteria(modeAbonnementBusiness, request, FunctionalityEnum.VIEW_PRIXOFFREVOYAGE);
        log.info("end method /programme/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getModeAbonnementByCompagnieTransport",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ModeAbonnementDTO> getModeAbonnementByCompagnieTransport(@RequestBody Request<ModeAbonnementDTO> request) {
        log.info("start method validateAdhesionRequestCompagny");
        Response<ModeAbonnementDTO> response = new Response<ModeAbonnementDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= modeAbonnementBusiness.getModeAbonnementByCompagnieTransport(request,locale);
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