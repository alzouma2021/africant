package com.africanb.africanb.rest.api.security;


import com.africanb.africanb.Business.security.RoleBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
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
@RequestMapping(value="/roles")
public class RoleController {

    private final ControllerFactory<RoleDTO> controllerFactory;
    private final RoleBusiness roleBusiness;
    private final FunctionalError functionalError;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public RoleController(ControllerFactory<RoleDTO> controllerFactory, RoleBusiness roleBusiness, FunctionalError functionalError, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.roleBusiness = roleBusiness;
        this.functionalError = functionalError;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }


    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<RoleDTO> create(@RequestBody Request<RoleDTO> request) {
        log.info("start method /Role/create");
        Response<RoleDTO> response = controllerFactory.create(roleBusiness, request, FunctionalityEnum.CREATE_ROLE);
        log.info("end method /Role/create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<RoleDTO> update(@RequestBody Request<RoleDTO> request) {
        log.info("start method /Roles/update");
        Response<RoleDTO> response = controllerFactory.update(roleBusiness, request, FunctionalityEnum.UPDATE_ROLE);
        log.info("end method /Role/update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<RoleDTO> delete(@RequestBody Request<RoleDTO> request) {
        log.info("start method /Role/delete");
        Response<RoleDTO> response = controllerFactory.delete(roleBusiness, request, FunctionalityEnum.DELETE_ROLE);
        log.info("end method /Role/delete");
        return response;
    }

    @RequestMapping(value="/forceDelete",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<RoleDTO> forceDelete(@RequestBody Request<RoleDTO> request) {
        log.info("start method /Role/forceDelete");
        Response<RoleDTO> response = controllerFactory.forceDelete(roleBusiness, request, FunctionalityEnum.DELETE_FUNCTIONALITY);
        log.info("end method /Role/forceDelete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<RoleDTO> getByCriteria(@RequestBody Request<RoleDTO> request) {
        log.info("start method /Role/getByCriteria");
        Response<RoleDTO> response = controllerFactory.getByCriteria(roleBusiness, request, FunctionalityEnum.VIEW_ROLE);
        log.info("end method /Role/getByCriteria");
        return response;
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public Response<RoleDTO> getAll(@RequestBody Request<RoleDTO> request) {
        Response<RoleDTO> response = new Response<RoleDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("la langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try {
            response = roleBusiness.getAll(locale) ; //Appel
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
