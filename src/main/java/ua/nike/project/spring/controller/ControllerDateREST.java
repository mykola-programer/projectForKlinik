package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.VisitDateDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisitDateVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/visit_dates")
public class ControllerDateREST {

    @Autowired
    VisitDateDAO visitDateDAO;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getVisitDates() {
        return visitDateDAO.getListUnlockedVisitDates();
    }

    @CrossOrigin
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getAllVisitDates() {
        return visitDateDAO.getVisitDates();
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO getVisitDate(@PathVariable("id") int visitDateID) throws BusinessException {
        return visitDateDAO.findVisitDate(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfVisitDate(@PathVariable("id") int visitDateID) throws BusinessException {
        return visitDateDAO.getListVisitsOfVisitDate(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> addVisitDates(@RequestBody List<VisitDateVO> visitDateVOList) throws BusinessException {
        return visitDateDAO.addVisitDates(visitDateVOList);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean removeVisitDate(@PathVariable("id") int visitDateID) {
        return visitDateDAO.lockVisitDate(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean removeVisitDates() {
        return visitDateDAO.lockAllVisitDates();
    }

}