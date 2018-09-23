package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.AccomodationDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.AccomodationVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/accomodations/")
public class ControllerAccomodationREST {

    @Autowired
    AccomodationDAO accomodationDAO;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccomodationVO> getAccomodations() {
        return accomodationDAO.getListUnlockedAccomodations();
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO getAccomodation(@PathVariable("id") int accomodationID) throws BusinessException {
        return accomodationDAO.findAccomodation(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfAccomodation(@PathVariable("id") int accomodationID) throws BusinessException {
        return accomodationDAO.getListVisitsOfAccomodation(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean lockAccomodationPlace(@PathVariable("id") int accomodationID) throws BusinessException {
        return accomodationDAO.lockAccomodationPlace(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "wards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Integer> getWards() {
        return accomodationDAO.getListUnlockedWards();
    }

}
