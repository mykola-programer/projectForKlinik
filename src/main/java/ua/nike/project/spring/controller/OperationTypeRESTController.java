package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.OperationTypeService;
import ua.nike.project.spring.vo.OperationTypeVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/operation_types")
public class OperationTypeRESTController implements RESTController<OperationTypeVO>{

    @Autowired
    private OperationTypeService operationTypeService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO getByID(@PathVariable("id") int operationTypeID) {
        return operationTypeService.findByID(operationTypeID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationTypeVO> getAll() {
        return operationTypeService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO add(@RequestBody @NotNull @Valid OperationTypeVO operationTypeVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return operationTypeService.create(operationTypeVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO editByID(@PathVariable("id") int operationTypeID, @RequestBody @NotNull @Valid OperationTypeVO operationTypeVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return operationTypeService.update(operationTypeID, operationTypeVO);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int operationTypeID) {
        return operationTypeService.deleteById(operationTypeID);
    }

}
