package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.service.ServiceSurgeon;
import ua.nike.project.spring.vo.SurgeonVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/surgeons")
public class ControllerSurgeonREST {

    @Autowired
    private ServiceSurgeon serviceSurgeon;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO getSurgeon(@PathVariable("id") int surgeonID) throws ApplicationException {
        return serviceSurgeon.findByID(surgeonID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonVO> getSurgeons() {
        return serviceSurgeon.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<SurgeonVO> getActiveSurgeons() {
        return serviceSurgeon.findAllActive();
    }


    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO addSurgeon(@RequestBody @NotNull @Valid SurgeonVO surgeonVO, BindingResult bindingResult) {
        //TODO Valid
        return serviceSurgeon.create(surgeonVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO editSurgeon(@PathVariable("id") int surgeonID, @RequestBody @NotNull @Valid SurgeonVO surgeonVO, BindingResult bindingResult) throws ApplicationException {
        //TODO Valid
        return serviceSurgeon.update(surgeonID, surgeonVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int surgeonID) {
        return serviceSurgeon.deleteById(surgeonID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO deactivateByID(@PathVariable("id") int surgeonID) throws ApplicationException {
        return serviceSurgeon.deactivateByID(surgeonID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SurgeonVO activateByID(@PathVariable("id") int surgeonID) throws ApplicationException {
        return serviceSurgeon.activateByID(surgeonID);
    }
}
