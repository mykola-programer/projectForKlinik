package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ManagerService;
import ua.nike.project.spring.vo.ManagerVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/managers")
public class ManagerRESTController {

    @Autowired
    private ManagerService managerService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO getManager(@PathVariable("id") int managerID) {
        return managerService.findByID(managerID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ManagerVO> getManagers() {
        return managerService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ManagerVO> getActiveSurgeons() {
        return managerService.findAllActive();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO addManager(@RequestBody @NotNull @Valid ManagerVO managerVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return managerService.create(managerVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO editManager(@PathVariable("id") int managerID, @RequestBody @NotNull @Valid ManagerVO managerVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return managerService.update(managerID, managerVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int managerID) {
        return managerService.deleteById(managerID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO deactivateByID(@PathVariable("id") int managerID) {
        return managerService.deactivateByID(managerID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO activateByID(@PathVariable("id") int managerID) {
        return managerService.activateByID(managerID);
    }

}