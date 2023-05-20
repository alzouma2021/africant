package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.PaysBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.PaysDTO;
import com.africanb.africanb.helper.dto.compagnie.VilleDTO;
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
@RequestMapping(value="/pays")
public class PaysController {

    @Autowired
    private ControllerFactory<PaysDTO> controllerFactory;
    @Autowired
    private PaysBusiness paysBusiness;

    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;


    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<PaysDTO> create(@RequestBody Request<PaysDTO> request) {
        log.info("start method create");
        Response<PaysDTO> response = controllerFactory.create(paysBusiness, request, FunctionalityEnum.CREATE_PAYS);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<PaysDTO> update(@RequestBody Request<PaysDTO> request) {
        log.info("start method update");
        Response<PaysDTO> response = controllerFactory.update(paysBusiness, request, FunctionalityEnum.UPDATE_PAYS);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<PaysDTO> delete(@RequestBody Request<PaysDTO> request) {
        log.info("start method delete");
        Response<PaysDTO> response = controllerFactory.delete(paysBusiness, request, FunctionalityEnum.DELETE_PAYS);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<PaysDTO> getByCriteria(@RequestBody Request<PaysDTO> request) {
        log.info("start method /pays/getByCriteria");
        Response<PaysDTO> response = controllerFactory.getByCriteria(paysBusiness, request, FunctionalityEnum.VIEW_PAYS);
        log.info("end method /pays/getByCriteria");
        return response;
    }


    @RequestMapping(value="/getAllPays",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<PaysDTO> getAllCities(@RequestBody Request<PaysDTO> request) {
        log.info("start method create");
        Response<PaysDTO> response = new Response<PaysDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=paysBusiness.getAllPays(request,locale);
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
