package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.OperationTypeDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.OperationTypeVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/operation_types/")
public class ControllerOperationTypeREST {

    @Autowired
    OperationTypeDAO operationTypeDAO;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationTypeVO> getOperationTypes() {
        return operationTypeDAO.getListOperationTypes();
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO getOperationType(@PathVariable("id") int operationTypeID) throws BusinessException {
        return operationTypeDAO.findOperationType(operationTypeID);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfOperationType(@PathVariable("id") int operationTypeID) throws BusinessException {
        return operationTypeDAO.getListVisitsOfOperationType(operationTypeID);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean lockOperationTypePlace(@PathVariable("id") int operationTypeID) throws BusinessException {
        return operationTypeDAO.lockOperationType(operationTypeID);
    }
}
