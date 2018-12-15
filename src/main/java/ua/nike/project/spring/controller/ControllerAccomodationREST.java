package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.service.ServiceAccomodation;
import ua.nike.project.spring.vo.AccomodationVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/accomodations")
public class ControllerAccomodationREST {

    //    @Autowired
//    private ControllerValidation controllerValidation;
    @Autowired
    private ServiceAccomodation serviceAccomodation;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO getAccomodation(@PathVariable("id") int accomodationID) throws ApplicationException {
        return serviceAccomodation.findByID(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccomodationVO> getAccomodations() {
        return serviceAccomodation.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<AccomodationVO> getActiveAccomodations() {
        return serviceAccomodation.findAllActive();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO addAccomodation(@RequestBody @NotNull @Valid AccomodationVO accomodationVO, BindingResult bindingResult) {
        // TODO Validate
        return serviceAccomodation.create(accomodationVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO editAccomodation(@PathVariable("id") int accomodationID, @RequestBody @NotNull @Valid AccomodationVO accomodationVO, BindingResult bindingResult) throws ApplicationException {
        // TODO Validate
        return serviceAccomodation.update(accomodationID, accomodationVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteAccomodationByID(@PathVariable("id") int accomodationID) {
        return serviceAccomodation.deleteById(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO deactivateByID(@PathVariable("id") int accomodationID) throws ApplicationException {
        return serviceAccomodation.deactivateByID(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccomodationVO activateByID(@PathVariable("id") int accomodationID) throws ApplicationException {
        return serviceAccomodation.activateByID(accomodationID);
    }

    @CrossOrigin
    @RequestMapping(value = "/wards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Integer> getActiveWards() {
        return serviceAccomodation.getActiveWards();
    }
}






/*
    @CrossOrigin
    @RequestMapping(value = "/{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByAccomodation(@PathVariable("id") int accomodationID) throws ApplicationException {
        Accomodation accomodation = serviceAccomodation.getEntityByID(accomodationID);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("accomodation", accomodation);

        return visitServiceDAO.getListByNamedQuery("Visit.findByAccomodation", parameters);
    }
*/
