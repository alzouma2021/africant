package com.africanb.africanb.rest.api.offreVoyage;


import com.africanb.africanb.Business.offreVoyage.ProgrammeBusiness;
import com.africanb.africanb.helper.ExceptionUtils;
import com.africanb.africanb.helper.TechnicalError;
import com.africanb.africanb.helper.contrat.Request;
import com.africanb.africanb.helper.contrat.Response;
import com.africanb.africanb.helper.dto.offreVoyage.PrixOffreVoyageDTO;
import com.africanb.africanb.helper.dto.offreVoyage.ProgrammeDTO;
import com.africanb.africanb.helper.enums.FunctionalityEnum;
import com.africanb.africanb.rest.fact.ControllerFactory;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Log
@CrossOrigin("*")
@RestController
@RequestMapping(value="/programmes")
public class ProgrammeController {

    @Autowired
    private ControllerFactory<ProgrammeDTO> controllerFactory;
    @Autowired
    private ProgrammeBusiness programmeBusiness;
    @Autowired
    private TechnicalError technicalError;
    @Autowired
    private ExceptionUtils exceptionUtils;
    @Autowired
    private HttpServletRequest requestBasic;

    @RequestMapping(value="",method= RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ProgrammeDTO> create(@RequestBody Request<ProgrammeDTO> request) {
        log.info("start method create");
        Response<ProgrammeDTO> response = controllerFactory.create(programmeBusiness, request, FunctionalityEnum.CREATE_PRIXOFFREVOYAGE);
        log.info("end method create");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.PUT,consumes = {"application/json"},produces={"application/json"})
    public Response<ProgrammeDTO> update(@RequestBody Request<ProgrammeDTO> request) {
        log.info("start method update");
        Response<ProgrammeDTO> response = controllerFactory.update(programmeBusiness, request, FunctionalityEnum.UPDATE_PRIXOFFREVOYAGE);
        log.info("end method update");
        return response;
    }

    @RequestMapping(value="",method=RequestMethod.DELETE,consumes = {"application/json"},produces={"application/json"})
    public Response<ProgrammeDTO> delete(@RequestBody Request<ProgrammeDTO> request) {
        log.info("start method delete");
        Response<ProgrammeDTO> response = controllerFactory.delete(programmeBusiness, request, FunctionalityEnum.DELETE_PRIXOFFREVOYAGE);
        log.info("end method delete");
        return response;
    }

    @RequestMapping(value="/getByCriteria",method=RequestMethod.POST,consumes = {"application/json"},produces={"application/json"})
    public Response<ProgrammeDTO> getByCriteria(@RequestBody Request<ProgrammeDTO> request) {
        log.info("start method /programme/getByCriteria");
        Response<ProgrammeDTO> response = controllerFactory.getByCriteria(programmeBusiness, request, FunctionalityEnum.VIEW_PRIXOFFREVOYAGE);
        log.info("end method /programme/getByCriteria");
        return response;
    }
}
