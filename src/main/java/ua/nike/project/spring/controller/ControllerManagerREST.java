package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.ManagerDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.ManagerVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/managers")
public class ControllerManagerREST {

    @Autowired
    ManagerDAO managerDAO;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ManagerVO> getManagers() {
        return managerDAO.getManagers();
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO getManager(@PathVariable("id") int managerId) throws BusinessException {
        return managerDAO.findManager(managerId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO editManager(@PathVariable("id") int managerId, @RequestBody ManagerVO managerVO) throws BusinessException {
        return managerDAO.editManager(managerId, managerVO);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ManagerVO addManager(@RequestBody ManagerVO manager) {
        manager.setManagerId(managerDAO.addManager(manager));
        return manager;
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfManager(@PathVariable("id") int managerId) throws BusinessException {
        return managerDAO.getListVisitsOfManager(managerId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteManager(@PathVariable("id") int managerId) {
         return managerDAO.removeManager(managerId);
    }

}
