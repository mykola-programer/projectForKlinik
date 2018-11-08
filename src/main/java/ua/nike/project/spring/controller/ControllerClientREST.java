package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.dao.ClientDAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.ClientVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ControllerClientREST {

    @Autowired
    ClientDAO clientDAO;

    /**
     * return all clients from database
     */
    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> getClients() {
        return clientDAO.getClients();
    }

    /**
     * return unlock clients from database
     */
    @CrossOrigin
    @RequestMapping(value = "/unlock", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> getUnlockClients() {
        return clientDAO.getUnlockClients();
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO getClient(@PathVariable("id") int clientId) throws BusinessException {
        return clientDAO.findClient(clientId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO editClient(@PathVariable("id") int clientId, @RequestBody ClientVO clientVO) throws BusinessException {
        ControllerValidation.validate(clientVO);
        return clientDAO.editClient(clientId, clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO addClient(@RequestBody ClientVO clientVO) throws BusinessException {
        ControllerValidation.validate(clientVO);
        return clientDAO.addClient(clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfClient(@PathVariable("id") int clientId) throws BusinessException {
        return clientDAO.getVisitsOfClient(clientId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteClient(@PathVariable("id") int clientId) {
        return clientDAO.removeClient(clientId);
    }

}
