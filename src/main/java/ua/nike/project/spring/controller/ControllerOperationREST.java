package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.OperationDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.OperationVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/operations")
public class ControllerOperationREST {

    @Autowired
    OperationDAO operationDAO;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationVO> getOperations() {
        return operationDAO.listOperations();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationVO getOperation(@PathVariable("id") int operationID) throws BusinessException {
        return operationDAO.findOperation(operationID);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationVO editOperation(@RequestBody OperationVO operation) {
        operation.setOperationId(operationDAO.editOperation(operation));
        return operation;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationVO addOperation(@RequestBody OperationVO operation) {
        operation.setOperationId(operationDAO.saveOperation(operation));
        return operation;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/surgeons/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> getSurgeons() {
        return operationDAO.getSurgeons();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/managers/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> getManagers() {
        return operationDAO.getManagers();
    }
}
