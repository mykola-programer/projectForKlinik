package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.AccomodationVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accomodations")
public class ControllerAccomodationREST {

    @Autowired
    ServiceDAO<AccomodationVO, Accomodation> accomodationServiceDAO;

    @Autowired
    ServiceDAO<VisitVO, Visit> visitServiceDAO;


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO getAccomodation(@PathVariable("id") int accomodationID) throws BusinessException {
        return accomodationServiceDAO.findByID(accomodationID, Accomodation.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccomodationVO> getAccomodations() throws BusinessException {
        return accomodationServiceDAO.findAll("Accomodation.findAll", Accomodation.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccomodationVO> getActiveAccomodations() throws BusinessException {
        return accomodationServiceDAO.findAll("Accomodation.getAllActive", Accomodation.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO addAccomodation(@RequestBody AccomodationVO accomodationVO) throws BusinessException {
        ControllerValidation.validate(accomodationVO);
        return accomodationServiceDAO.create(accomodationVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO editAccomodation(@PathVariable("id") int accomodationID, @RequestBody AccomodationVO accomodationVO) throws BusinessException {
        ControllerValidation.validate(accomodationVO);
        return accomodationServiceDAO.update(accomodationID, accomodationVO, Accomodation.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteAccomodationByID(@PathVariable("id") int accomodationID) throws BusinessException {
        return accomodationServiceDAO.deleteById(accomodationID, Accomodation.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO deactivateAccomodationPlace(@PathVariable("id") int accomodationID) throws BusinessException {
        AccomodationVO accomodationVO = getAccomodation(accomodationID);
        accomodationVO.setInactive(true);
        return accomodationServiceDAO.update(accomodationID, accomodationVO, Accomodation.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO activateAccomodationPlace(@PathVariable("id") int accomodationID) throws BusinessException {
        AccomodationVO accomodationVO = getAccomodation(accomodationID);
        accomodationVO.setInactive(false);
        return accomodationServiceDAO.update(accomodationID, accomodationVO, Accomodation.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByAccomodation(@PathVariable("id") int accomodationID) throws BusinessException {
        Accomodation accomodation = accomodationServiceDAO.getEntityByID(accomodationID, Accomodation.class);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("accomodation", accomodation);

        return visitServiceDAO.getListByNamedQuery("Visit.findByAccomodation", parameters, Visit.class);
    }


    @CrossOrigin
    @RequestMapping(value = "/wards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Integer> getWards() {
        List<Ward> wards = (List<Ward>) accomodationServiceDAO.getObjectsByQuery("SELECT DISTINCT acc.ward FROM Accomodation acc where acc.inactive = false ORDER BY acc.ward", null, Ward.class);
        List<Integer> result = new ArrayList<>();
        for (Ward ward : wards) result.add(Integer.valueOf(ward.toString().substring(1)));
        return result;
    }


}
