package com.africanb.africanb.rest.api.security;

import com.africanb.africanb.Business.security.FunctionalityBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.security.FunctionalityDTO;
import com.africanb.africanb.helper.dto.security.RoleDTO;
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
@RequestMapping(value="/functionalities")
public class FunctionalityController {


    private final ControllerFactory<FunctionalityDTO> controllerFactory;
    private final FunctionalityBusiness functionalityBusiness;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public FunctionalityController(ControllerFactory<FunctionalityDTO> controllerFactory, FunctionalityBusiness functionalityBusiness, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.functionalityBusiness = functionalityBusiness;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<FunctionalityDTO> create(@RequestBody Request<FunctionalityDTO> request) {
        log.info("start method /Functionality/create"+request.getDatas() + functionalityBusiness.toString());
        Response<FunctionalityDTO> response = controllerFactory.create(functionalityBusiness, request, FunctionalityEnum.CREATE_FUNCTIONALITY);
        log.info("end method /Functionality/create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<FunctionalityDTO> update(@RequestBody Request<FunctionalityDTO> request) {
        log.info("start method /Functionality/update");
        Response<FunctionalityDTO> response = controllerFactory.update(functionalityBusiness, request, FunctionalityEnum.UPDATE_FUNCTIONALITY);
        log.info("end method /Functionality/update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<FunctionalityDTO> delete(@RequestBody Request<FunctionalityDTO> request) {
        log.info("start method /Functionality/delete");
        Response<FunctionalityDTO> response = controllerFactory.delete(functionalityBusiness, request, FunctionalityEnum.DELETE_FUNCTIONALITY);
        log.info("end method /Functionality/delete");
        return response;
    }

    @RequestMapping(value="/forceDelete",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<FunctionalityDTO> forceDelete(@RequestBody Request<FunctionalityDTO> request) {
        log.info("start method /Functionality/forceDelete");
        Response<FunctionalityDTO> response = controllerFactory.forceDelete(functionalityBusiness, request, FunctionalityEnum.DELETE_FUNCTIONALITY);
        log.info("end method /Functionality/forceDelete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<FunctionalityDTO> getByCriteria(@RequestBody Request<FunctionalityDTO> request) {
        log.info("start method /Functionality/getByCriteria");
        Response<FunctionalityDTO> response = controllerFactory.getByCriteria(functionalityBusiness, request, FunctionalityEnum.VIEW_FUNCTIONALITY);
        log.info("end method /Functionality/getByCriteria");
        return response;
    }

    @RequestMapping(value = "/getFunctionalitiesByRole", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public Response<FunctionalityDTO> getFunctionalitiesByRole(@RequestBody Request<RoleDTO> request) {
        Response<FunctionalityDTO> response = new Response<FunctionalityDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("la langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try {
            response = functionalityBusiness.getFunctionalitiesByRole(request,locale) ; //Appel
            if (response.isHasError()) {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                return response;
            }
            log.info(String.format("code: {} -  message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        }
        return response;
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public Response<FunctionalityDTO> getAll(@RequestBody Request<FunctionalityDTO> request) {
        Response<FunctionalityDTO> response = new Response<FunctionalityDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("la langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try {
            response = functionalityBusiness.getAll(locale) ; //Appel
            if (response.isHasError()) {
                log.info(String.format("Erreur| code: {} -  message: {}", response.getStatus().getCode(), response.getStatus().getMessage()));
                return response;
            }
            log.info(String.format("code: {} -  message: {}", StatusCode.SUCCESS, StatusMessage.SUCCESS));
        } catch (CannotCreateTransactionException e) {
            exceptionUtils.CANNOT_CREATE_TRANSACTION_EXCEPTION(response, locale, e);
        } catch (TransactionSystemException e) {
            exceptionUtils.TRANSACTION_SYSTEM_EXCEPTION(response, locale, e);
        } catch (RuntimeException e) {
            exceptionUtils.RUNTIME_EXCEPTION(response, locale, e);
        } catch (Exception e) {
            exceptionUtils.EXCEPTION(response, locale, e);
        }
        return response;
    }

}
