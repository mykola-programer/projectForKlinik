package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.VisitVO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visits")
public class ControllerVisitREST {
    private final String DATE_FORMAT = "dd.MM.yyyy";

    @Autowired
    ServiceDAO<VisitVO, Visit> visitServiceDAO;


    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisits() throws BusinessException {
        return visitServiceDAO.findAll("Visit.findAll", Visit.class);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO getVisitByID(@PathVariable("id") int visitID) throws BusinessException {
        return visitServiceDAO.findByID(visitID, Visit.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO addVisit(@RequestBody VisitVO visitVO) throws BusinessException {
        return visitServiceDAO.create(visitVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO editVisit(@PathVariable("id") int visitID, @RequestBody VisitVO visitVO) throws BusinessException {
        return visitServiceDAO.update(visitID, visitVO, Visit.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteVisit(@PathVariable("id") int visitID) throws BusinessException {
        return visitServiceDAO.deleteById(visitID, Visit.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/all/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDate(@PathVariable("date") String reqDate) throws BusinessException {
        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate, formatter);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("date", date);

            return visitServiceDAO.getListByNamedQuery("Visit.findAllByDate", parameters, Visit.class);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/all_wards/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDateWithWards(@PathVariable("date") String reqDate) throws BusinessException {

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate, formatter);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("date", date);

            return visitServiceDAO.getListByNamedQuery("Visit.findAllByDateWithWards", parameters, Visit.class);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/no_wards/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDateWithoutWard(@PathVariable("date") String reqDate) throws BusinessException {

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate, formatter);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("date", date);

            return visitServiceDAO.getListByNamedQuery("Visit.findAllByDateWithoutWards", parameters, Visit.class);
        } else {
            throw new BusinessException("Dates is not correct !");
        }
    }

/*    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> addVisits(@RequestBody List<VisitVO> visitsVO) {
        return visitDAO.saveVisits(visitsVO);
    }*/

}
