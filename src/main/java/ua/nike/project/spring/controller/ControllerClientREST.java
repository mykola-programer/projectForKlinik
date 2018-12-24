package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ServiceClient;
import ua.nike.project.spring.service.ServiceMassage;
import ua.nike.project.spring.vo.ClientVO;
import ua.nike.project.spring.vo.MyObjectVOList;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ControllerClientREST {

    @Autowired
    ServiceMassage serviceMass;
    @Autowired
    private ServiceClient serviceClient;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO getClient(@PathVariable("id") int clientID) throws ApplicationException {
        return serviceClient.findByID(clientID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> getClients() {
        return serviceClient.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO addClient(@RequestBody @NotNull @Valid ClientVO clientVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return serviceClient.create(clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO editClient(@PathVariable("id") int clientID, @RequestBody @NotNull @Valid ClientVO clientVO, BindingResult bindingResult) throws ApplicationException, ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return serviceClient.update(clientID, clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> putClients(@RequestBody @NotNull @Valid MyObjectVOList<ClientVO> clientsVO, BindingResult bindingResult) throws ApplicationException, ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return serviceClient.putClients(clientsVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteClientByID(@PathVariable("id") int clientID) throws ApplicationException {
        try {
            return serviceClient.deleteById(clientID);
        } catch (JpaSystemException | PersistenceException ex) {
            throw new ApplicationException(serviceMass.value("client.has.relatives"));
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/list/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteClients(@PathVariable("ids") List<Integer> clientIDs) throws ApplicationException {
        try {
            return serviceClient.deleteByIDs(clientIDs);
        } catch (PersistenceException ex) {
            throw new ApplicationException(serviceMass.value("clients.has.relatives"));
        }
    }
}