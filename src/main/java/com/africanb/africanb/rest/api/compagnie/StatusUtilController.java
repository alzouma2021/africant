package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.StatusUtilBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.StatusUtilDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.rest.fact.ControllerFactory;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/statusUtil")
public class StatusUtilController {


    private final ControllerFactory<StatusUtilDTO> controllerFactory;
    private final StatusUtilBusiness statusUtilBusiness;

    public StatusUtilController(ControllerFactory<StatusUtilDTO> controllerFactory, StatusUtilBusiness statusUtilBusiness) {
        this.controllerFactory = controllerFactory;
        this.statusUtilBusiness = statusUtilBusiness;
    }


    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<StatusUtilDTO> create(@RequestBody Request<StatusUtilDTO> request) {
        log.info("start method create");
        Response<StatusUtilDTO> response = controllerFactory.create(statusUtilBusiness, request, FunctionalityEnum.CREATE_PAYS);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<StatusUtilDTO> update(@RequestBody Request<StatusUtilDTO> request) {
        log.info("start method update");
        Response<StatusUtilDTO> response = controllerFactory.update(statusUtilBusiness, request, FunctionalityEnum.UPDATE_PAYS);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<StatusUtilDTO> delete(@RequestBody Request<StatusUtilDTO> request) {
        log.info("start method delete");
        Response<StatusUtilDTO> response = controllerFactory.delete(statusUtilBusiness, request, FunctionalityEnum.DELETE_PAYS);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<StatusUtilDTO> getByCriteria(@RequestBody Request<StatusUtilDTO> request) {
        log.info("start method /statusUtil/getByCriteria");
        Response<StatusUtilDTO> response = controllerFactory.getByCriteria(statusUtilBusiness, request, FunctionalityEnum.VIEW_PAYS);
        log.info("end method /statusUtil/getByCriteria");
        return response;
    }

}
