package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.VilleEscaleBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.VilleEscaleDTO;
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
@RequestMapping(value="/villeEscales")
public class VilleEscaleController {

    private final ControllerFactory<VilleEscaleDTO> controllerFactory;
    private final VilleEscaleBusiness villeEscaleBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public VilleEscaleController(ControllerFactory<VilleEscaleDTO> controllerFactory, VilleEscaleBusiness villeEscaleBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.villeEscaleBusiness = villeEscaleBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> create(@RequestBody Request<VilleEscaleDTO> request) {
        Response<VilleEscaleDTO> response = controllerFactory.create(villeEscaleBusiness, request, FunctionalityEnum.CREATE_VILLEESCALE);
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> update(@RequestBody Request<VilleEscaleDTO> request) {
        Response<VilleEscaleDTO> response = controllerFactory.update(villeEscaleBusiness, request, FunctionalityEnum.UPDATE_VILLEESCALE);
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> delete(@RequestBody Request<VilleEscaleDTO> request) {
        Response<VilleEscaleDTO> response = controllerFactory.delete(villeEscaleBusiness, request, FunctionalityEnum.DELETE_VILLEESCALE);
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> getByCriteria(@RequestBody Request<VilleEscaleDTO> request) {
        Response<VilleEscaleDTO> response = controllerFactory.getByCriteria(villeEscaleBusiness, request, FunctionalityEnum.VIEW_VILLEESCALE);
        return response;
    }

    @RequestMapping(value="/getVilleByOffreVoyageDesignation",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> getVilleByOffreVoyageDesignation(@RequestBody Request<OffreVoyageDTO> request) {
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        Response<VilleEscaleDTO> response = new Response<>();
        try{
            response=villeEscaleBusiness.getVilleByOffreVoyageDesignation(request,locale);
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
