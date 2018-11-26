package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.dao.SurgeonDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.SurgeonVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/surgeons")
public class ControllerSurgeonREST {

    @Qualifier("DAOImpl")
    @Autowired
    DAO<Surgeon> daoSurgeon;

    @Autowired
    SurgeonDAO surgeonDAO;


    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Surgeon getSurgeon(@PathVariable("id") int surgeonId) throws BusinessException {

//        return surgeonDAO.findSurgeon(surgeonId);
        Surgeon surgeon = daoSurgeon.findByID(surgeonId, Surgeon.class);
        surgeon.setVisits(null);
        return surgeon;
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonVO> getSurgeons() {
        return surgeonDAO.getSurgeons();
    }

    @CrossOrigin
    @RequestMapping(value = "/unlock", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonVO> getUnlockSurgeons() {
        return surgeonDAO.getUnlockSurgeons();
    }



    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO editSurgeon(@PathVariable("id") int surgeonId, @RequestBody SurgeonVO surgeonVO) throws BusinessException {
        ControllerValidation.validate(surgeonVO);
        return surgeonDAO.editSurgeon(surgeonId, surgeonVO);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO addSurgeon(@RequestBody SurgeonVO surgeonVO) throws BusinessException {
        ControllerValidation.validate(surgeonVO);
        return surgeonDAO.addSurgeon(surgeonVO);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfSurgeon(@PathVariable("id") int surgeonId) throws BusinessException {
        return surgeonDAO.getVisitsOfSurgeon(surgeonId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteSurgeon(@PathVariable("id") int surgeonId) {
        return surgeonDAO.removeSurgeon(surgeonId);
    }

}
