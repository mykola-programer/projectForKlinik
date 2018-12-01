package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.hibernate.entity.VisitDate;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.AccomodationVO;
import ua.nike.project.spring.vo.VisitDateVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visit_dates")
public class ControllerVisitDateREST {

    @Autowired
    ServiceDAO<VisitDateVO, VisitDate> visitDateServiceDAO;

    @Autowired
    ServiceDAO<VisitVO, Visit> visitServiceDAO;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO getVisitDate(@PathVariable("id") int visitDateID) throws BusinessException {
        return visitDateServiceDAO.findByID(visitDateID, VisitDate.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getVisitDates() throws BusinessException {
        return visitDateServiceDAO.findAll("VisitDate.findAll", VisitDate.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getActiveVisitDates() throws BusinessException {
        return visitDateServiceDAO.findAll("VisitDate.getAllActive", VisitDate.class);
    }

//    @CrossOrigin
//    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO addVisitDate(@RequestBody VisitDateVO visitDateVO) throws BusinessException {
        ControllerValidation.validate(visitDateVO);
        return visitDateServiceDAO.create(visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> addVisitDates(@RequestBody List<VisitDateVO> visitDateVOList) throws BusinessException {
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDateVO visitDateVO : visitDateVOList) {
            if (visitDateVO != null && visitDateVO.getDate() != null) {
                result.add(addVisitDate(visitDateVO));
            }
        }

        return result;
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO editVisitDate(@PathVariable("id") int visitDateID, @RequestBody VisitDateVO visitDateVO) throws BusinessException {
        ControllerValidation.validate(visitDateVO);
        return visitDateServiceDAO.update(visitDateID, visitDateVO, VisitDate.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteVisitDateByID(@PathVariable("id") int visitDateID) throws BusinessException {
        return visitDateServiceDAO.deleteById(visitDateID, VisitDate.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO deactivateVisitDate(@PathVariable("id") int visitDateID) throws BusinessException {
        VisitDateVO visitDateVO = getVisitDate(visitDateID);
        visitDateVO.setInactive(true);
        return visitDateServiceDAO.update(visitDateID, visitDateVO, VisitDate.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO activateVisitDate(@PathVariable("id") int visitDateID) throws BusinessException {
        VisitDateVO visitDateVO = getVisitDate(visitDateID);
        visitDateVO.setInactive(false);
        return visitDateServiceDAO.update(visitDateID, visitDateVO, VisitDate.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByVisitDate(@PathVariable("id") int visitDateID) throws BusinessException {
        VisitDate visitDate = visitDateServiceDAO.getEntityByID(visitDateID, VisitDate.class);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("visitDate", visitDate);

        return visitServiceDAO.getListByNamedQuery("Visit.findByVisitDate", parameters, Visit.class);
    }

}