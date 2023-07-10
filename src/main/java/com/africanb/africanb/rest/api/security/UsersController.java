package com.africanb.africanb.rest.api.security;

import com.africanb.africanb.Business.security.UsersBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.FunctionalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.CompagnieTransportDTO;
import com.africanb.africanb.helper.dto.security.RoleDTO;
import com.africanb.africanb.helper.dto.security.UsersDTO;
import com.africanb.africanb.helper.dto.security.UsersPassWordDTO;
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
@RequestMapping(value="/users")
public class UsersController {

    @Autowired
    private ControllerFactory<UsersDTO> controllerFactory;
    @Autowired
    private com.africanb.africanb.Business.security.UsersBusiness usersBusiness;
    @Autowired
    private FunctionalError functionalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UsersDTO> create(@RequestBody Request<UsersDTO> request) {
        log.info("start method /Users/create");
        Response<UsersDTO> response = controllerFactory.create(usersBusiness, request, FunctionalityEnum.CREATE_USER);
        log.info("end method /Users/create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<UsersDTO> update(@RequestBody Request<UsersDTO> request) {
        log.info("start method /Users/update");
        Response<UsersDTO> response = controllerFactory.update(usersBusiness, request, FunctionalityEnum.UPDATE_USER);
        log.info("end method /Users/update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<UsersDTO> delete(@RequestBody Request<UsersDTO> request) {
        log.info("start method /Users/delete");
        Response<UsersDTO> response = controllerFactory.delete(usersBusiness, request, FunctionalityEnum.DELETE_USER);
        log.info("end method /Users/delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UsersDTO> getByCriteria(@RequestBody Request<UsersDTO> request) {
        log.info("start method /Users/getByCriteria");
        Response<UsersDTO> response = controllerFactory.getByCriteria(usersBusiness, request, FunctionalityEnum.VIEW_USER);
        log.info("end method /Users/getByCriteria");
        return response;
    }

    @RequestMapping(value="/login",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UsersDTO> login(@RequestBody Request<UsersDTO> request) {
        log.info("start method /login");
        Response<UsersDTO> response = controllerFactory.login(usersBusiness, request, FunctionalityEnum.VIEW_USER);
        log.info("end method /login");
        return response;
    }

    @RequestMapping(value = "/resetPasswordUser", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public Response<UsersDTO> resetPassWordUser(@RequestBody Request<UsersPassWordDTO> request) {
        Response<UsersDTO> response = new Response<UsersDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("la langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try {
            response = usersBusiness.resetPassWordUser(request, locale);
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

    @RequestMapping(value="/activeUser",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<UsersDTO> activeUser(@RequestBody Request<UsersDTO> request) {
        log.info("start method active user");
        Response<UsersDTO> response = new Response<UsersDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=usersBusiness.activeUser(request,locale);
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

    @RequestMapping(value = "/getAll", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    public Response<UsersDTO> getAll(@RequestBody Request<UsersDTO> request) {
        Response<UsersDTO> response = new Response<UsersDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("la langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try {
            response = usersBusiness.getAll(locale) ; //Appel
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
