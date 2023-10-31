package com.africanb.africanb.rest.api.compagnie;

import com.africanb.africanb.Business.compagnie.BagageBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.BagageDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.helper.status.StatusCode;
import com.africanb.africanb.helper.status.StatusMessage;
import com.africanb.africanb.rest.fact.ControllerFactory;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/bagages")
public class BagageController {


    private final ControllerFactory<BagageDTO> controllerFactory;
    private final BagageBusiness bagageBusiness;
    private final TechnicalError technicalError;
    private final ExceptionUtils exceptionUtils;
    private final HttpServletRequest requestBasic;

    public BagageController(ControllerFactory<BagageDTO> controllerFactory, BagageBusiness bagageBusiness, TechnicalError technicalError, ExceptionUtils exceptionUtils, HttpServletRequest requestBasic) {
        this.controllerFactory = controllerFactory;
        this.bagageBusiness = bagageBusiness;
        this.technicalError = technicalError;
        this.exceptionUtils = exceptionUtils;
        this.requestBasic = requestBasic;
    }

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    @ApiOperation("Création de bagages")
    public Response<BagageDTO> create(@RequestBody Request<BagageDTO> request) {
        log.info("start method create");
        Response<BagageDTO> response = controllerFactory.create(bagageBusiness, request, FunctionalityEnum.CREATE_PRIXOFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    @ApiOperation("Modification de bagages")
    public Response<BagageDTO> update(@RequestBody Request<BagageDTO> request) {
        log.info("start method update");
        Response<BagageDTO> response = controllerFactory.update(bagageBusiness, request, FunctionalityEnum.UPDATE_PRIXOFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<BagageDTO> delete(@RequestBody Request<BagageDTO> request) {
        log.info("start method delete");
        Response<BagageDTO> response = controllerFactory.delete(bagageBusiness, request, FunctionalityEnum.DELETE_PRIXOFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<BagageDTO> getByCriteria(@RequestBody Request<BagageDTO> request) {
        log.info("start method /bagage/getByCriteria");
        Response<BagageDTO> response = controllerFactory.getByCriteria(bagageBusiness, request, FunctionalityEnum.VIEW_PRIXOFFREVOYAGE);
        log.info("end method /bagage/getByCriteria");
        return response;
    }

    @RequestMapping(value="/getBagageByCompagnieTransportRaisonSociale",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    @ApiOperation("Obtenir la liste de bagages d'une compagnie de transport en fonction de sa raison sociale")
    public Response<BagageDTO> getBagageByCompagnieTransportRaisonSociale(@RequestBody Request<BagageDTO> request) {
        Response<BagageDTO> response = new Response<BagageDTO>();
        //requestBasic.setAttribute("CURRENT_LANGUAGE_IDENTIFIER", "fr");
        String languageID = (String) requestBasic.getAttribute("CURRENT_LANGUAGE_IDENTIFIER");
        Locale locale = new Locale(languageID, "");
        try{
            response=bagageBusiness.getAllBagageByCompagnieTransportRaisonSociale(request,locale);
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
