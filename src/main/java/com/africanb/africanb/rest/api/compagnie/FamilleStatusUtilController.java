package com.africanb.africanb.rest.api.compagnie;


import com.africanb.africanb.Business.compagnie.FamilleStatusUtilBusiness;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.compagnie.FamilleStatusUtilDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.rest.fact.ControllerFactory;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/familleStatusUtil")
public class FamilleStatusUtilController {

    @Autowired
    private ControllerFactory<FamilleStatusUtilDTO> controllerFactory;
    @Autowired
    private FamilleStatusUtilBusiness familleStatusUtilBusiness;
    
    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<FamilleStatusUtilDTO> create(@RequestBody Request<FamilleStatusUtilDTO> request) {
        log.info("start method create");
        Response<FamilleStatusUtilDTO> response = controllerFactory.create(familleStatusUtilBusiness, request, FunctionalityEnum.CREATE_FAMILLESTATUSUTIL);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<FamilleStatusUtilDTO> update(@RequestBody Request<FamilleStatusUtilDTO> request) {
        log.info("start method update");
        Response<FamilleStatusUtilDTO> response = controllerFactory.update(familleStatusUtilBusiness, request, FunctionalityEnum.UPDATE_FAMILLESTATUSUTIL);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<FamilleStatusUtilDTO> delete(@RequestBody Request<FamilleStatusUtilDTO> request) {
        log.info("start method delete");
        Response<FamilleStatusUtilDTO> response = controllerFactory.delete(familleStatusUtilBusiness, request, FunctionalityEnum.DELETE_FAMILLESTATUSUTIL);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<FamilleStatusUtilDTO> getByCriteria(@RequestBody Request<FamilleStatusUtilDTO> request) {
        log.info("start method /famillestatusutil/getByCriteria");
        Response<FamilleStatusUtilDTO> response = controllerFactory.getByCriteria(familleStatusUtilBusiness, request, FunctionalityEnum.VIEW_FAMILLESTATUSUTIL);
        log.info("end method /famillestatusutil/getByCriteria");
        return response;
    }

}
