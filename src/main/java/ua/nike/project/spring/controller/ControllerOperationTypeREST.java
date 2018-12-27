package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ServiceOperationType;
import ua.nike.project.spring.vo.OperationTypeVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/operation_types")
public class ControllerOperationTypeREST {

    @Autowired
    private ServiceOperationType serviceOperationType;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO getOperationType(@PathVariable("id") int operationTypeID) throws ApplicationException {
        return serviceOperationType.findByID(operationTypeID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationTypeVO> getOperationTypes() {
        return serviceOperationType.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationTypeVO> getActiveOperationTypes() {
        return serviceOperationType.findAllActive();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO addOperationType(@RequestBody @NotNull @Valid OperationTypeVO operationTypeVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) throw new ValidationException("Object is not valid", bindingResult);
        return serviceOperationType.create(operationTypeVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO editOperationType(@PathVariable("id") int operationTypeID, @RequestBody @NotNull @Valid OperationTypeVO operationTypeVO, BindingResult bindingResult) throws ApplicationException, ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) throw new ValidationException("Object is not valid", bindingResult);
        return serviceOperationType.update(operationTypeID, operationTypeVO);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int operationTypeID) {
        return serviceOperationType.deleteById(operationTypeID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO deactivateByID(@PathVariable("id") int operationTypeID) throws ApplicationException {
        return serviceOperationType.deactivateByID(operationTypeID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO activateByID(@PathVariable("id") int operationTypeID) throws ApplicationException {
        return serviceOperationType.activateByID(operationTypeID);
    }
}
