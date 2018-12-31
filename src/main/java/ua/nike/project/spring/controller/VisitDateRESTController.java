package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.VisitDateService;
import ua.nike.project.spring.vo.MyObjectVOList;
import ua.nike.project.spring.vo.VisitDateVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/visit_dates")
public class VisitDateRESTController {

    @Autowired
    private VisitDateService visitDateService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO getVisitDate(@PathVariable("id") int visitDateID) {
        return visitDateService.findByID(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getVisitDates() {
        return visitDateService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getActiveVisitDates() {
        return visitDateService.findAllActive();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO addVisitDate(@RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitDateService.create(visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> putVisitDates(@RequestBody @NotNull @Valid MyObjectVOList<VisitDateVO> visitDatesVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Objects are not valid", bindingResult);
        return visitDateService.putVisitDates(visitDatesVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO editVisitDate(@PathVariable("id") int visitDateID, @RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitDateService.update(visitDateID, visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int visitDateID) {
        return visitDateService.deleteById(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "/list/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByIDs(@PathVariable("ids") List<Integer> visitDateIDs) {
        return visitDateService.deleteByIDs(visitDateIDs);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO deactivateByID(@PathVariable("id") int visitDateID) {
        return visitDateService.deactivateByID(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO activateByID(@PathVariable("id") int visitDateID) {
        return visitDateService.activateByID(visitDateID);
    }

}