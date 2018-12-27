package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ServiceMassage;
import ua.nike.project.spring.service.ServiceVisitDate;
import ua.nike.project.spring.vo.MyObjectVOList;
import ua.nike.project.spring.vo.VisitDateVO;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/visit_dates")
public class ControllerVisitDateREST {

    @Autowired
    private ServiceVisitDate serviceVisitDate;
    @Autowired
    private ServiceMassage serviceMass;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO getVisitDate(@PathVariable("id") int visitDateID) throws ApplicationException {
        return serviceVisitDate.findByID(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getVisitDates() {
        return serviceVisitDate.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getActiveVisitDates() {
        return serviceVisitDate.findAllActive();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO addVisitDate(@RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) throw  new ValidationException("Object is not valid", bindingResult);
        return serviceVisitDate.create(visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> putVisitDates(@RequestBody @NotNull @Valid MyObjectVOList<VisitDateVO> visitDatesVO, BindingResult bindingResult) throws ValidationException, ApplicationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Objects are not valid", bindingResult);
        return serviceVisitDate.putVisitDates(visitDatesVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO editVisitDate(@PathVariable("id") int visitDateID, @RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ApplicationException, ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) throw  new ValidationException("Object is not valid", bindingResult);
        return serviceVisitDate.update(visitDateID, visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int visitDateID) {
        return serviceVisitDate.deleteById(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "/list/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByIDs(@PathVariable("ids") List<Integer> visitDateIDs) throws ApplicationException {
        try {
            return serviceVisitDate.deleteByIDs(visitDateIDs);
        } catch (PersistenceException ex) {
            throw new ApplicationException(serviceMass.value("visit_dates.has.relatives"));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO deactivateByID(@PathVariable("id") int visitDateID) throws ApplicationException {
        return serviceVisitDate.deactivateByID(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO activateByID(@PathVariable("id") int visitDateID) throws ApplicationException {
        return serviceVisitDate.activateByID(visitDateID);
    }

}