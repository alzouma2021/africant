package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.VilleEscaleBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
import com.africanb.africanb.helper.dto.offreVoyage.OffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.VilleEscaleDTO;
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
@RequestMapping(value="/villeEscales")
public class VilleEscaleController {

    @Autowired
    private ControllerFactory<VilleEscaleDTO> controllerFactory;
    @Autowired
    private VilleEscaleBusiness villeEscaleBusiness;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> create(@RequestBody Request<VilleEscaleDTO> request) {
        log.info("start method create");
        Response<VilleEscaleDTO> response = controllerFactory.create(villeEscaleBusiness, request, FunctionalityEnum.CREATE_VILLEESCALE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> update(@RequestBody Request<VilleEscaleDTO> request) {
        log.info("start method update");
        Response<VilleEscaleDTO> response = controllerFactory.update(villeEscaleBusiness, request, FunctionalityEnum.UPDATE_VILLEESCALE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> delete(@RequestBody Request<VilleEscaleDTO> request) {
        log.info("start method delete");
        Response<VilleEscaleDTO> response = controllerFactory.delete(villeEscaleBusiness, request, FunctionalityEnum.DELETE_VILLEESCALE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleEscaleDTO> getByCriteria(@RequestBody Request<VilleEscaleDTO> request) {
        log.info("start method /villeEscale/getByCriteria");
        Response<VilleEscaleDTO> response = controllerFactory.getByCriteria(villeEscaleBusiness, request, FunctionalityEnum.VIEW_VILLEESCALE);
        log.info("end method /villeEscale/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getVilleByOffreVoyageDesignation",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleDTO> getVilleByOffreVoyageDesignation(@RequestBody Request<OffreVoyageDTO> request) {
        log.info("start method getVilleByOffreVoyageDesignation");
        Response<VilleDTO> response = new Response<VilleDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
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
