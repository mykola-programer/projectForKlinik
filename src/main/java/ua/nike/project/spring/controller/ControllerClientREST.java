package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.ClientVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ControllerClientREST {

    @Autowired
    ServiceDAO<ClientVO, Client> clientServiceDAO;

    @Autowired
    ServiceDAO<VisitVO, Visit> visitServiceDAO;

//    @Autowired
//    ClientDAO clientDAO;

//
//    @CrossOrigin
//    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ClientVO getClient2(@PathVariable("id") int clientId) throws BusinessException {
//        return clientDAO.findClient(clientId);
//    }
//

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO getClient(@PathVariable("id") int clientId) throws BusinessException {
        return clientServiceDAO.findByID(clientId, Client.class);
    }

//    /**
//     * return all clients from database
//     */
//    @CrossOrigin
//    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<ClientVO> getClients2() {
//        return clientDAO.getClients();
//    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> getClients() throws BusinessException {
        return clientServiceDAO.findAll("Client.findAll", Client.class);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO addClient(@RequestBody ClientVO clientVO) throws BusinessException {
        ControllerValidation.validate(clientVO);
        return clientServiceDAO.create(clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO editClient(@PathVariable("id") int clientId, @RequestBody ClientVO clientVO) throws BusinessException {
        ControllerValidation.validate(clientVO);
        return clientServiceDAO.update(clientId, clientVO, Client.class);
    }

    @CrossOrigin
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteClientByID(@PathVariable("id") int clientId) throws BusinessException {
        return clientServiceDAO.deleteById(clientId, Client.class);
    }

//    @CrossOrigin
//    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public List<VisitVO> getListVisitsOfClient(@PathVariable("id") int clientId) throws BusinessException {
//        return clientDAO.getVisitsOfClient(clientId);
//    }

    @CrossOrigin
    @RequestMapping(value = "{id}/visits", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByClient(@PathVariable("id") int clientId) throws BusinessException {
        Client client = clientServiceDAO.getEntityByID(clientId, Client.class);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("client", client);

        return visitServiceDAO.getListByNamedQuery("Visit.findByClient", parameters, Visit.class);
    }


}
