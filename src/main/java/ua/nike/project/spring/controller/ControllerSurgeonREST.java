package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.SurgeonDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.SurgeonVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/surgeons")
public class ControllerSurgeonREST {

    @Autowired
    SurgeonDAO surgeonDAO;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonVO> getSurgeons() {
        return surgeonDAO.getSurgeons();
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO getSurgeon(@PathVariable("id") int surgeonId) throws BusinessException {
        return surgeonDAO.findSurgeon(surgeonId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO editSurgeon(@PathVariable("id") int surgeonId, @RequestBody SurgeonVO surgeonVO) throws BusinessException {
        return surgeonDAO.editSurgeon(surgeonId, surgeonVO);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO addSurgeon(@RequestBody SurgeonVO surgeon) {
        surgeon.setSurgeonId(surgeonDAO.addSurgeon(surgeon));
        return surgeon;
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfSurgeon(@PathVariable("id") int surgeonId) throws BusinessException {
        return surgeonDAO.getListVisitsOfSurgeon(surgeonId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteSurgeon(@PathVariable("id") int surgeonId) {
         return surgeonDAO.removeSurgeon(surgeonId);
    }

}
