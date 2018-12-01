package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.OperationType;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.OperationTypeVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/operation_types")
public class ControllerOperationTypeREST {

    @Autowired
    ServiceDAO<OperationTypeVO, OperationType> operationTypeServiceDAO;

    @Autowired
    ServiceDAO<VisitVO, Visit> visitServiceDAO;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO getOperationType(@PathVariable("id") int operationTypeID) throws BusinessException {
        return operationTypeServiceDAO.findByID(operationTypeID, OperationType.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationTypeVO> getActiveOperationTypes() throws BusinessException {
        return operationTypeServiceDAO.findAll("OperationType.getAllActive", OperationType.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<OperationTypeVO> getOperationTypes() throws BusinessException {
        return operationTypeServiceDAO.findAll("OperationType.findAll", OperationType.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO addOperationType(@RequestBody OperationTypeVO operationTypeVO) throws BusinessException {
        ControllerValidation.validate(operationTypeVO);
        return operationTypeServiceDAO.create(operationTypeVO);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO editOperationType(@PathVariable("id") int operationTypeID, @RequestBody OperationTypeVO operationTypeVO) throws BusinessException {
        ControllerValidation.validate(operationTypeVO);
        return operationTypeServiceDAO.update(operationTypeID, operationTypeVO, OperationType.class);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteOperationTypeByID(@PathVariable("id") int operationTypeID) throws BusinessException {
        return operationTypeServiceDAO.deleteById(operationTypeID, OperationType.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO deactivateOperationType(@PathVariable("id") int operationTypeID) throws BusinessException {
        OperationTypeVO operationTypeVO = getOperationType(operationTypeID);
        operationTypeVO.setInactive(true);
        return operationTypeServiceDAO.update(operationTypeID, operationTypeVO, OperationType.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public OperationTypeVO activateOperationType(@PathVariable("id") int operationTypeID) throws BusinessException {
        OperationTypeVO operationTypeVO = getOperationType(operationTypeID);
        operationTypeVO.setInactive(false);
        return operationTypeServiceDAO.update(operationTypeID, operationTypeVO, OperationType.class);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByOperationType(@PathVariable("id") int operationTypeID) throws BusinessException {
        OperationType operationType = operationTypeServiceDAO.getEntityByID(operationTypeID, OperationType.class);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("operationType", operationType);

        return visitServiceDAO.getListByNamedQuery("Visit.findByOperationType", parameters, Visit.class);
    }
}
