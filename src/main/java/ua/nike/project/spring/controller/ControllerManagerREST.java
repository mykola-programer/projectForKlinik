package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ServiceManager;
import ua.nike.project.spring.vo.ManagerVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/managers")
public class ControllerManagerREST {

    @Autowired
    private ServiceManager serviceManager;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO getManager(@PathVariable("id") int managerID) throws ApplicationException {
        return serviceManager.findByID(managerID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ManagerVO> getManagers() {
        return serviceManager.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ManagerVO> getActiveSurgeons() {
        return serviceManager.findAllActive();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO addManager(@RequestBody @NotNull @Valid ManagerVO managerVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null) throw new ValidationException("Object is not valid", bindingResult);
        return serviceManager.create(managerVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO editManager(@PathVariable("id") int managerID, @RequestBody @NotNull @Valid ManagerVO managerVO, BindingResult bindingResult) throws ApplicationException, ValidationException {
        if (bindingResult != null) throw new ValidationException("Object is not valid", bindingResult); // TODO Validate
        return serviceManager.update(managerID, managerVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int managerID) {
        return serviceManager.deleteById(managerID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO deactivateByID(@PathVariable("id") int managerID) throws ApplicationException {
        return serviceManager.deactivateByID(managerID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO activateByID(@PathVariable("id") int managerID) throws ApplicationException {
        return serviceManager.activateByID(managerID);
    }

}