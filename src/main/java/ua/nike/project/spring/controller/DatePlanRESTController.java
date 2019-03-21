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
@RequestMapping("/date_plans")
public class DatePlanRESTController implements RESTController<DatePlanVO> {

    @Autowired
    private DatePlanService datePlanService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DatePlanVO getByID(@PathVariable("id") int datePlanID) {
        return datePlanService.findByID(datePlanID);
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
    public DatePlanVO editByID(@PathVariable("id") int datePlanID, @RequestBody @NotNull @Valid DatePlanVO datePlanVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return datePlanService.update(datePlanID, datePlanVO);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int datePlanID) {
        return datePlanService.deleteById(datePlanID);
    }

}
