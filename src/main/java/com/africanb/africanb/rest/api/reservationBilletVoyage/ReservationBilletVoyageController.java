package com.africanb.africanb.rest.api.reservationBilletVoyage;

import com.africanb.africanb.Business.reservationBilletVoyage.ReservationBilletVoyageBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.GareDTO;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.ReservationBilletVoyageDTO;
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
@RequestMapping(value="/reservationBilletVoyages")
public class ReservationBilletVoyageController {

    private final ControllerFactory<ReservationBilletVoyageDTO> controllerFactory;
    private final ReservationBilletVoyageBusiness reservationBilletVoyageBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public ReservationBilletVoyageController(ControllerFactory<ReservationBilletVoyageDTO> controllerFactory, ReservationBilletVoyageBusiness reservationBilletVoyageBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.reservationBilletVoyageBusiness = reservationBilletVoyageBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> create(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        log.info("start method create");
        Response<ReservationBilletVoyageDTO> response = controllerFactory.create(reservationBilletVoyageBusiness, request, FunctionalityEnum.CREATE_OFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> update(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        log.info("start method update");
        Response<ReservationBilletVoyageDTO> response = controllerFactory.update(reservationBilletVoyageBusiness, request, FunctionalityEnum.UPDATE_OFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> delete(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        log.info("start method delete");
        Response<ReservationBilletVoyageDTO> response = controllerFactory.delete(reservationBilletVoyageBusiness, request, FunctionalityEnum.DELETE_OFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> getByCriteria(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        log.info("start method /reservationBilletVoyage/getByCriteria");
        Response<ReservationBilletVoyageDTO> response = controllerFactory.getByCriteria(reservationBilletVoyageBusiness, request, FunctionalityEnum.VIEW_OFFREVOYAGE);
        log.info("end method /reservationBilletVoyage/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getReservationByUserSample",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> getReservationByUserSample(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= reservationBilletVoyageBusiness.getReservationBilletVoyageBySampleUser(request,locale);
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


    @RequestMapping(value="/getReservationByUserGare",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> getReservationByUserGare(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= reservationBilletVoyageBusiness.getReservationBilletVoyageByUserGareCompagnieTransport(request,locale);
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


    @RequestMapping(value="/getReservationByAdminCompagnieTransport",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> getReservationByAdminCompagnieTransport(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= reservationBilletVoyageBusiness.getReservationBilletVoyageByAdminCompagnieTransport(request,locale);
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


    @RequestMapping(value="/getReservationByAdminCompagnieTransportAndGare",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReservationBilletVoyageDTO> getReservationByAdminCompagnieTransportAndGare(@RequestBody Request<GareDTO> request) {
        Response<ReservationBilletVoyageDTO> response = new Response<ReservationBilletVoyageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= reservationBilletVoyageBusiness.getReservationBilletVoyageByAdminCompagnieTransportAndGare(request,locale);
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
