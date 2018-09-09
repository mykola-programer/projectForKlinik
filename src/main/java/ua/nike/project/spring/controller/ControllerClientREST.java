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

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> getClients() {
        return clientDAO.getClients();
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO getClient(@PathVariable("id") int clientId) throws BusinessException {
        return clientDAO.findClient(clientId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO editClient(@PathVariable("id") int clientId, @RequestBody ClientVO clientVO) throws BusinessException {
        return clientDAO.editClient(clientId, clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO addClient(@RequestBody ClientVO client) {
        client.setClientId(clientDAO.addClient(client));
        return client;
    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getListVisitsOfClient(@PathVariable("id") int clientId) throws BusinessException {
        return clientDAO.getListVisitsOfClient(clientId);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteClient(@PathVariable("id") int clientId) {
         return clientDAO.removeClient(clientId);
    }

}
