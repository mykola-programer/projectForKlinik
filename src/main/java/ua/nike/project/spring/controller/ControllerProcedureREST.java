package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Procedure;
import ua.nike.project.spring.dao.OperationDAO;
import ua.nike.project.spring.dao.ProcedureDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.OperationVO;
import ua.nike.project.spring.vo.ProcedureVO;

import java.util.List;

@RestController
@RequestMapping("/procedures/")
public class ControllerProcedureREST {

    @Autowired
    ProcedureDAO procedureDAO;

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ProcedureVO> getProcedures() {
        return procedureDAO.getListProcedures();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ProcedureVO getProcedure(@PathVariable("id") int procedureID) throws BusinessException {
        return procedureDAO.findProcedure(procedureID);
    }

}
