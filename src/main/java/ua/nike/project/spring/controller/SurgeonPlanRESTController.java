package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.SurgeonPlanService;
import ua.nike.project.spring.vo.SurgeonPlanVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/surgeon_plans")
public class SurgeonPlanRESTController implements RESTController<SurgeonPlanVO> {

    @Autowired
    private SurgeonPlanService surgeonPlanService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonPlanVO getByID(@PathVariable("id") int surgeonPlanID) {
        return surgeonPlanService.findByID(surgeonPlanID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonPlanVO> getAll() {
        return surgeonPlanService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonPlanVO add(@RequestBody @NotNull @Valid SurgeonPlanVO surgeonPlanVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return surgeonPlanService.create(surgeonPlanVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonPlanVO editByID(@PathVariable("id") int surgeonPlanID, @RequestBody @NotNull @Valid SurgeonPlanVO surgeonPlanVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return surgeonPlanService.update(surgeonPlanID, surgeonPlanVO);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int surgeonPlanID) {
        return surgeonPlanService.deleteById(surgeonPlanID);
    }

}
