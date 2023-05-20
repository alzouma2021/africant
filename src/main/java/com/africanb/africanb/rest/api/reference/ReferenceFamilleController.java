package com.africanb.africanb.rest.api.reference;


import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.rest.fact.ControllerFactory;
import com.africanb.africanb.utils.Reference.ReferenceFamilleBusines;
import com.africanb.africanb.utils.Reference.ReferenceFamilleDTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/referencesFamille")
public class ReferenceFamilleController {

    @Autowired
    private ControllerFactory<ReferenceFamilleDTO> controllerFactory;
    @Autowired
    private ReferenceFamilleBusines referenceFamilleBusines;

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceFamilleDTO> create(@RequestBody Request<ReferenceFamilleDTO> request) {
        log.info("start method create");
        Response<ReferenceFamilleDTO> response = controllerFactory.create(referenceFamilleBusines, request, FunctionalityEnum.CREATE_REFERENCEFAMILLE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceFamilleDTO> update(@RequestBody Request<ReferenceFamilleDTO> request) {
        log.info("start method update");
        Response<ReferenceFamilleDTO> response = controllerFactory.update(referenceFamilleBusines, request, FunctionalityEnum.UPDATE_REFERENCEFAMILLE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceFamilleDTO> delete(@RequestBody Request<ReferenceFamilleDTO> request) {
        log.info("start method delete");
        Response<ReferenceFamilleDTO> response = controllerFactory.delete(referenceFamilleBusines, request, FunctionalityEnum.DELETE_REFERENCEFAMILLE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ReferenceFamilleDTO> getByCriteria(@RequestBody Request<ReferenceFamilleDTO> request) {
        log.info("start method /famillestatusutil/getByCriteria");
        Response<ReferenceFamilleDTO> response = controllerFactory.getByCriteria(referenceFamilleBusines, request, FunctionalityEnum.VIEW_REFERENCEFAMILLE);
        log.info("end method /famillestatusutil/getByCriteria");
        return response;
    }

}
