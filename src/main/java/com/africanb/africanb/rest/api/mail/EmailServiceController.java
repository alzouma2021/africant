package com.africanb.africanb.rest.api.mail;


import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import com.africanb.africanb.utils.emailService.EmailDTO;
import com.africanb.africanb.utils.emailService.EmailServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

//@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/mails")
public class EmailServiceController {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceController.class);

    private final EmailServiceInterface emailServiceInterface;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public EmailServiceController(EmailServiceInterface emailServiceInterface, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.emailServiceInterface = emailServiceInterface;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="/sendSimpleEmail",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<EmailDTO> sendSimpleEmail(@RequestBody Request<EmailDTO> request) {
        log.info("start method create");
        Response<EmailDTO> response = new Response<EmailDTO>();
        requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("La langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try{
            response=emailServiceInterface.sendSimpleEmail(request,locale);
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

    @RequestMapping(value="/sendEmailWithAttachment",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<EmailDTO> sendEmailWithAttachment(@RequestBody Request<EmailDTO> request) {
        log.info("start method create");
        Response<EmailDTO> response = new Response<EmailDTO>();
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        log.info("La langue " + languageID);
        Locale locale = new Locale(languageID, "");
        try{
            response=emailServiceInterface.sendEmailWithAttachment(request,locale);
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