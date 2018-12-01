package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.SurgeonVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/surgeons")
public class ControllerSurgeonREST {

    @Autowired
    ServiceDAO<SurgeonVO, Surgeon> surgeonServiceDAO;

    @Autowired
    ServiceDAO<VisitVO, Visit> visitServiceDAO;


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO getSurgeon(@PathVariable("id") int surgeonId) throws BusinessException {
        return surgeonServiceDAO.findByID(surgeonId, Surgeon.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonVO> getSurgeons() throws BusinessException {
        return surgeonServiceDAO.findAll("Surgeon.findAll", Surgeon.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonVO> getActiveSurgeons() throws BusinessException {
        return surgeonServiceDAO.findAll("Surgeon.findAllActive", Surgeon.class);
    }


    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO addSurgeon(@RequestBody SurgeonVO surgeonVO) throws BusinessException {
        ControllerValidation.validate(surgeonVO);
        return surgeonServiceDAO.create(surgeonVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO editSurgeon(@PathVariable("id") int surgeonId, @RequestBody SurgeonVO surgeonVO) throws BusinessException {
        ControllerValidation.validate(surgeonVO);
        return surgeonServiceDAO.update(surgeonId, surgeonVO, Surgeon.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteSurgeonByID(@PathVariable("id") int surgeonId) throws BusinessException {
        return surgeonServiceDAO.deleteById(surgeonId, Surgeon.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO deactivateSurgeonByID(@PathVariable("id") int surgeonId) throws BusinessException {
        SurgeonVO surgeonVO = getSurgeon(surgeonId);
        surgeonVO.setInactive(true);
        return surgeonServiceDAO.update(surgeonId, surgeonVO, Surgeon.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO activateSurgeonByID(@PathVariable("id") int surgeonId) throws BusinessException {
        SurgeonVO surgeonVO = getSurgeon(surgeonId);
        surgeonVO.setInactive(false);
        return surgeonServiceDAO.update(surgeonId, surgeonVO, Surgeon.class);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsBySurgeon(@PathVariable("id") int surgeonId) throws BusinessException {
        Surgeon surgeon = surgeonServiceDAO.getEntityByID(surgeonId, Surgeon.class);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("surgeon", surgeon);

        return visitServiceDAO.getListByNamedQuery("Visit.findBySurgeon", parameters, Visit.class);
    }

}
