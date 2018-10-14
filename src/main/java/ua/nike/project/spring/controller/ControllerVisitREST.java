package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.VisitDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisitVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/visits")
public class ControllerVisitREST {
    private final String DATE_FORMAT = "dd.MM.yyyy";

    @Autowired
    VisitDAO visitDAO;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisits() {
        return visitDAO.getVisits();
    }

    @CrossOrigin
    @RequestMapping(value = "/all_wards/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsInDateOfWard(@PathVariable("date") String reqDate) throws BusinessException {

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate,formatter);
            return visitDAO.getVisitsInDateOfWard(date);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/no_ward/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsInDateOfNoWard(@PathVariable("date") String reqDate) throws BusinessException {

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate,formatter);
            return visitDAO.getVisitsInDateOfNoWard(date);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO getVisit(@PathVariable("id") int visitID) throws BusinessException {
        return visitDAO.findVisit(visitID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO addVisit(@RequestBody VisitVO visit) {
        return visitDAO.saveVisit(visit);
    }

/*    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> addVisits(@RequestBody List<VisitVO> visitsVO) {
        return visitDAO.saveVisits(visitsVO);
    }*/

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO editVisit(@PathVariable("id") int visitID, @RequestBody VisitVO visit) {
        return visitDAO.editVisit(visitID, visit);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteVisit(@PathVariable("id") int visitID) {
        return visitDAO.removeVisit(visitID);
    }

}
