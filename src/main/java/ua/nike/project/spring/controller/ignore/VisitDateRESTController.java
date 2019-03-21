package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.DatePlanService;
import ua.nike.project.spring.vo.DatePlanVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/visit_dates")
public class VisitDateRESTController implements RESTController<DatePlanVO> {

    @Autowired
    private DatePlanService datePlanService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DatePlanVO getByID(@PathVariable("id") int visitDateID) {
        return datePlanService.findByID(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<DatePlanVO> getAll() {
        return datePlanService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DatePlanVO add(@RequestBody @NotNull @Valid DatePlanVO datePlanVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return datePlanService.create(datePlanVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DatePlanVO editByID(@PathVariable("id") int visitDateID, @RequestBody @NotNull @Valid DatePlanVO datePlanVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return datePlanService.update(visitDateID, datePlanVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int visitDateID) {
        return datePlanService.deleteById(visitDateID);
    }

/*    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Deprecated
    public List<DatePlanVO> putVisitDates(@RequestBody @NotNull @Valid MyObjectVOList<DatePlanVO> visitDatesVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Objects are not valid", bindingResult);
        return datePlanService.putVisitDates(visitDatesVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/list/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Deprecated
    public boolean deleteByIDs(@PathVariable("ids") List<Integer> visitDateIDs) {
        return datePlanService.deleteByIDs(visitDateIDs);
    }*/

}