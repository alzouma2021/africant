package com.africanb.africanb.rest.api.reservationBilletVoyage;

import com.africanb.africanb.Business.reservationBilletVoyage.HistoriquePaiementBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.HistoriquePaiementDTO;
import com.africanb.africanb.helper.dto.reservationBilletVoyage.ReservationBilletVoyageDTO;
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
@RequestMapping(value="/historiquePaiements")
public class HistoriquePaiementController {


    private final ControllerFactory<HistoriquePaiementDTO> controllerFactory;
    private final HistoriquePaiementBusiness historiquePaiementBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public HistoriquePaiementController(ControllerFactory<HistoriquePaiementDTO> controllerFactory, HistoriquePaiementBusiness historiquePaiementBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.historiquePaiementBusiness = historiquePaiementBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriquePaiementDTO> create(@RequestBody Request<HistoriquePaiementDTO> request) {
        log.info("start method create");
        Response<HistoriquePaiementDTO> response = controllerFactory.create(historiquePaiementBusiness, request, FunctionalityEnum.CREATE_OFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriquePaiementDTO> update(@RequestBody Request<HistoriquePaiementDTO> request) {
        log.info("start method update");
        Response<HistoriquePaiementDTO> response = controllerFactory.update(historiquePaiementBusiness, request, FunctionalityEnum.UPDATE_OFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriquePaiementDTO> delete(@RequestBody Request<HistoriquePaiementDTO> request) {
        log.info("start method delete");
        Response<HistoriquePaiementDTO> response = controllerFactory.delete(historiquePaiementBusiness, request, FunctionalityEnum.DELETE_OFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriquePaiementDTO> getByCriteria(@RequestBody Request<HistoriquePaiementDTO> request) {
        log.info("start method /reservationBilletVoyage/getByCriteria");
        Response<HistoriquePaiementDTO> response = controllerFactory.getByCriteria(historiquePaiementBusiness, request, FunctionalityEnum.VIEW_OFFREVOYAGE);
        log.info("end method /reservationBilletVoyage/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getHistoriquePaiementByIdentifiantUnique",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriquePaiementDTO> getReservationByUserSample(@RequestBody Request<HistoriquePaiementDTO> request) {
        Response<HistoriquePaiementDTO> response = new Response<HistoriquePaiementDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= historiquePaiementBusiness.getHistoriquePaiementByIdentifiantUnique(request,locale);
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


    @RequestMapping(value="/getHistoriquePaiementByReservationBilletVoyage",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<HistoriquePaiementDTO> getReservationByUserGare(@RequestBody Request<ReservationBilletVoyageDTO> request) {
        Response<HistoriquePaiementDTO> response = new Response<HistoriquePaiementDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response= historiquePaiementBusiness.getHistoriquePaiementByReservationBilletVoyage(request,locale);
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
