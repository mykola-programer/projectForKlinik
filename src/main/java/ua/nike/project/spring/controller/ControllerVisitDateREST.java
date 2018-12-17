package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ServiceVisitDate;
import ua.nike.project.spring.vo.VisitDateVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/visit_dates")
public class ControllerVisitDateREST {

    @Autowired
    private ServiceVisitDate serviceVisitDate;

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

    private VisitDateVO addVisitDate(@RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null) throw  new ValidationException("Object is not valid", bindingResult); // TODO Validate ?
        return serviceVisitDate.create(visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> addVisitDates(@RequestBody @NotNull @Valid List<VisitDateVO> visitDatesVO, BindingResult bindingResult) throws ValidationException {
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDateVO visitDateVO : visitDatesVO) {
            if (visitDateVO != null && visitDateVO.getDate() != null) {
                result.add(addVisitDate(visitDateVO, bindingResult));
            }
        }
        return result;
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO editVisitDate(@PathVariable("id") int visitDateID, @RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ApplicationException, ValidationException {
        if (bindingResult != null) throw  new ValidationException("Object is not valid", bindingResult);
        return serviceVisitDate.update(visitDateID, visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int visitDateID) {
        return serviceVisitDate.deleteById(visitDateID);
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