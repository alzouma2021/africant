package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.VilleBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
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
@RequestMapping(value="/villes")
public class VilleController {


    private final ControllerFactory<VilleDTO> controllerFactory;
    private final VilleBusiness villeBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public VilleController(ControllerFactory<VilleDTO> controllerFactory, VilleBusiness villeBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.villeBusiness = villeBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleDTO> create(@RequestBody Request<VilleDTO> request) {
        log.info("start method create");
        Response<VilleDTO> response = controllerFactory.create(villeBusiness, request, FunctionalityEnum.CREATE_PAYS);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleDTO> update(@RequestBody Request<VilleDTO> request) {
        log.info("start method update");
        Response<VilleDTO> response = controllerFactory.update(villeBusiness, request, FunctionalityEnum.UPDATE_PAYS);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleDTO> delete(@RequestBody Request<VilleDTO> request) {
        log.info("start method delete");
        Response<VilleDTO> response = controllerFactory.delete(villeBusiness, request, FunctionalityEnum.DELETE_PAYS);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleDTO> getByCriteria(@RequestBody Request<VilleDTO> request) {
        log.info("start method /pays/getByCriteria");
        Response<VilleDTO> response = controllerFactory.getByCriteria(villeBusiness, request, FunctionalityEnum.VIEW_PAYS);
        log.info("end method /pays/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getAllCities",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<VilleDTO> getAllCities(@RequestBody Request<VilleDTO> request) {
        log.info("start method create");
        Response<VilleDTO> response = new Response<VilleDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=villeBusiness.getAllCities(request,locale);
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
