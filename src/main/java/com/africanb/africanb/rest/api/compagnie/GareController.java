package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.GareBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.GareDTO;
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
@RequestMapping(value="/gares")
public class GareController {


    private final ControllerFactory<GareDTO> controllerFactory;
    private final GareBusiness gareBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public GareController(ControllerFactory<GareDTO> controllerFactory, GareBusiness gareBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.gareBusiness = gareBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<GareDTO> create(@RequestBody Request<GareDTO> request) {
        log.info("start method create");
        Response<GareDTO> response = controllerFactory.create(gareBusiness, request, FunctionalityEnum.CREATE_PRIXOFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<GareDTO> update(@RequestBody Request<GareDTO> request) {
        log.info("start method update");
        Response<GareDTO> response = controllerFactory.update(gareBusiness, request, FunctionalityEnum.UPDATE_PRIXOFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<GareDTO> delete(@RequestBody Request<GareDTO> request) {
        log.info("start method delete");
        Response<GareDTO> response = controllerFactory.delete(gareBusiness, request, FunctionalityEnum.DELETE_PRIXOFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<GareDTO> getByCriteria(@RequestBody Request<GareDTO> request) {
        log.info("start method /prixOffreVoyage/getByCriteria");
        Response<GareDTO> response = controllerFactory.getByCriteria(gareBusiness, request, FunctionalityEnum.VIEW_PRIXOFFREVOYAGE);
        log.info("end method /prixOffreVoyage/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getGareByCompagnieTransportRaisonScoiale",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<GareDTO> getGareByCompagnieTransportRaisonScoiale(@RequestBody Request<GareDTO> request) {
        Response<GareDTO> response = new Response<GareDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= gareBusiness.getGareByCompagnieTransportRaisonScoiale(request,locale);
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
