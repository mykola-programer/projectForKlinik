package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.VisitDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/operations")
public class ControllerOperationREST {

    @Autowired
    VisitDAO visitDAO;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getOperations() {
        return visitDAO.getVisits();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO getOperation(@PathVariable("id") int operationID) throws BusinessException {
        return visitDAO.findVisit(operationID);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO editOperation(@RequestBody VisitVO operation) {
        operation.setVisitId(visitDAO.editVisit(operation));
        return operation;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO addOperation(@RequestBody VisitVO operation) {
        operation.setVisitId(visitDAO.saveVisit(operation));
        return operation;
    }

}
