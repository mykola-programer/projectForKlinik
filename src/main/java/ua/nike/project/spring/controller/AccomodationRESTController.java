package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.AccomodationService;
import ua.nike.project.spring.vo.AccomodationVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/accomodations")
public class AccomodationRESTController implements RESTController<AccomodationVO> {

    @Autowired
    private AccomodationService accomodationService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO getByID(@PathVariable("id") int accomodationID) {
        return accomodationService.findByID(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccomodationVO> getAll() {
        return accomodationService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO add(@RequestBody @NotNull @Valid AccomodationVO accomodationVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new ValidationException("Object is not valid", bindingResult);
        }
        return accomodationService.create(accomodationVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO editByID(@PathVariable("id") int accomodationID, @RequestBody @NotNull @Valid AccomodationVO accomodationVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new ValidationException("Object is not valid", bindingResult);
        }
        return accomodationService.update(accomodationID, accomodationVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int accomodationID) {
        return accomodationService.deleteById(accomodationID);
    }

}