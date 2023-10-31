package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.BusBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.BusDTO;
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
@RequestMapping(value="/bus")
public class BusController {


    private final ControllerFactory<BusDTO> controllerFactory;
    private final BusBusiness busBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public BusController(ControllerFactory<BusDTO> controllerFactory, BusBusiness busBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.busBusiness = busBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<BusDTO> create(@RequestBody Request<BusDTO> request) {
        log.info("start method create");
        Response<BusDTO> response = controllerFactory.create(busBusiness, request, FunctionalityEnum.CREATE_BUS);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<BusDTO> update(@RequestBody Request<BusDTO> request) {
        log.info("start method update");
        Response<BusDTO> response = controllerFactory.update(busBusiness, request, FunctionalityEnum.UPDATE_BUS);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<BusDTO> delete(@RequestBody Request<BusDTO> request) {
        log.info("start method delete");
        Response<BusDTO> response = controllerFactory.delete(busBusiness, request, FunctionalityEnum.DELETE_BUS);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<BusDTO> getByCriteria(@RequestBody Request<BusDTO> request) {
        log.info("start method /bus/getByCriteria");
        Response<BusDTO> response = controllerFactory.getByCriteria(busBusiness, request, FunctionalityEnum.VIEW_BUS);
        log.info("end method /bus/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getBusByOffreVoyage",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<BusDTO> get(@RequestBody Request<BusDTO> request) {
        log.info("start method getBusByOffrevoyage");
        Response<BusDTO> response = new Response<BusDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= busBusiness.getBusByOffreVoyage(request,locale);
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
