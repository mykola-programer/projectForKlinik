package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.ManagerVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/managers")
public class ControllerManagerREST {

    @Autowired
    ServiceDAO<ManagerVO, Manager> managerServiceDAO;

    @Autowired
    ServiceDAO<VisitVO, Visit> visitServiceDAO;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO getManager(@PathVariable("id") int managerId) throws BusinessException {
        return managerServiceDAO.findByID(managerId, Manager.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ManagerVO> getManagers() throws BusinessException {
        return managerServiceDAO.findAll("Manager.findAll", Manager.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO addManager(@RequestBody ManagerVO managerVO) throws BusinessException {
        ControllerValidation.validate(managerVO);
        return managerServiceDAO.create(managerVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO editManager(@PathVariable("id") int managerId, @RequestBody ManagerVO managerVO) throws BusinessException {
        ControllerValidation.validate(managerVO);
        return managerServiceDAO.update(managerId, managerVO, Manager.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteManagerByID(@PathVariable("id") int managerId) throws BusinessException {
        return managerServiceDAO.deleteById(managerId, Manager.class);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfManager(@PathVariable("id") int managerId) throws BusinessException {
        Manager manager = managerServiceDAO.getEntityByID(managerId, Manager.class);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("manager", manager);

        return visitServiceDAO.getListByNamedQuery("Visit.findByManager", parameters, Visit.class);
    }

}
