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
public class VisitDateRESTController implements RESTController<VisitDateVO> {

    @Autowired
    private VisitDateService visitDateService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO getByID(@PathVariable("id") int visitDateID) {
        return visitDateService.findByID(visitDateID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitDateVO> getAll() {
        return visitDateService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO add(@RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitDateService.create(visitDateVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitDateVO editByID(@PathVariable("id") int visitDateID, @RequestBody @NotNull @Valid VisitDateVO visitDateVO, BindingResult bindingResult) throws ValidationException {
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
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Deprecated
    public List<VisitDateVO> putVisitDates(@RequestBody @NotNull @Valid MyObjectVOList<VisitDateVO> visitDatesVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Objects are not valid", bindingResult);
        return visitDateService.putVisitDates(visitDatesVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/list/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Deprecated
    public boolean deleteByIDs(@PathVariable("ids") List<Integer> visitDateIDs) {
        return visitDateService.deleteByIDs(visitDateIDs);
    }

}