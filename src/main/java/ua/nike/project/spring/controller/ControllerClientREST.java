package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ServiceClient;
import ua.nike.project.spring.vo.ClientVO;
import ua.nike.project.spring.vo.VisitVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ControllerClientREST {

    @Autowired
    private ServiceClient serviceClient;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO getClient(@PathVariable("id") int clientId) throws ApplicationException {
        return serviceClient.findByID(clientId);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> getClients() {
        return serviceClient.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO addClient(@RequestBody @NotNull @Valid ClientVO clientVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null) throw  new ValidationException("Object is not valid", bindingResult);
        return serviceClient.create(clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO editClient(@PathVariable("id") int clientId, @RequestBody @NotNull @Valid ClientVO clientVO, BindingResult bindingResult) throws ApplicationException, ValidationException {
        if (bindingResult != null) throw  new ValidationException("Object is not valid", bindingResult);
        return serviceClient.update(clientId, clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteClientByID(@PathVariable("id") int clientId) {
        return serviceClient.deleteById(clientId);
    }
}