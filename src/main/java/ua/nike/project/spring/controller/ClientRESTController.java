package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.ClientService;
import ua.nike.project.spring.vo.ClientVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientRESTController implements RESTController<ClientVO> {

    @Autowired
    private ClientService clientService;

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> getAll() {
        return clientService.getAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/search",
            params = {"search", "limit", "offset", "sort"},
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ClientVO> searchWithLimit(
            @RequestParam("search") String search,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @RequestParam("sort") String sort) throws ValidationException {
        search = search.replace("%2B", "+");
        if (search == null || search.equals("undefined") || search.equals("null")) {
            throw new ValidationException("incorrect.search", null);
        }
        if (limit == null || limit < 0) {
            throw new ValidationException("incorrect.limit", null);
        }
        if (offset == null || offset < 0) {
            throw new ValidationException("incorrect.offset", null);
        }
        return clientService.search(search, limit, offset, sort);
    }

    @CrossOrigin
    @RequestMapping(value = "/search/count", params = {"search"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Integer getCount(@RequestParam("search") String search) throws ValidationException {
        search = search.replace("%2B", "+");
        return searchWithLimit(search, 0, 0, "ASC").size();
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO getByID(@PathVariable("id") int clientID) {
        return clientService.findByID(clientID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO add(@RequestBody @NotNull @Valid ClientVO clientVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new ValidationException("Object is not valid", bindingResult);
        }
        return clientService.create(clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientVO editByID(@PathVariable("id") int clientID, @RequestBody @NotNull @Valid ClientVO clientVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return clientService.update(clientID, clientVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int clientID) {
        return clientService.deleteById(clientID);
    }
}


 /*   @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Deprecated
    public List<ClientVO> putClients(@RequestBody @NotNull @Valid MyObjectVOList<ClientVO> clientsVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new ValidationException("Object is not valid", bindingResult);
        }
        return clientService.putClients(clientsVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/list/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Deprecated
    public boolean deleteByIDs(@PathVariable("ids") List<Integer> clientIDs) {
        return clientService.deleteByIDs(clientIDs);
    }*/











/*

    final int indexOfACSUserFriendlyException = ExceptionUtils.indexOfThrowable(exception, ACSUserFriendlyException.class);
if(indexOfACSUserFriendlyException == -1) {Throwable pSQLException = getThrowableByFQFN("com.edb.util.PSQLException", exception);
private static Throwable getThrowableByFQFN(String fqfn, Throwable throwable) {
        for (Throwable thrwbl : ExceptionUtils.getThrowables(throwable)) {
        if(fqfn.equals(thrwbl.getClass().getName())) {
        return thrwbl;
        }
        }
        return null;
        }

        final int indexOfACSUserFriendlyException = ExceptionUtils.indexOfThrowable(exception, ACSUserFriendlyException.class);
        if(indexOfACSUserFriendlyException == -1) {
        Throwable pSQLException = getThrowableByFQFN("com.edb.util.PSQLException", exception);
        if (pSQLException == null) {
final ACSException firstAcsException = (ACSException) getThrowableChildByFQFN(ACSException.class, exception);
        if(firstAcsException == null) {
// generic case, both userMessage and technical information should be added, but only userMessage should be shown on UI
        jsonResponseBody.put(USER_MESSAGE_RESPONSE_KEY, USER_MESSAGE);
        jsonResponseBody.put(ERROR_MESSAGE_RESPONSE_KEY, exception.getMessage());
        } else {	private static Throwable getThrowableChildByFQFN(Class clazz, Throwable throwable) {
        for (Throwable thrwbl : ExceptionUtils.getThrowables(throwable)) {
        if(clazz.isAssignableFrom(thrwbl.getClass())) {
        return thrwbl;
        }
        }
        return null;
        }Ivan, 00:10	final String pSQLExMessage = pSQLException.getMessage();
        if(pSQLExMessage.contains("violates unique constraint")) {
        jsonResponseBody.put(USER_MESSAGE_RESPONSE_KEY, "Duplicate record exist.");
        } else if(pSQLExMessage.contains("violates not-null constraint")){
        jsonResponseBody.put(USER_MESSAGE_RESPONSE_KEY, "Not all required fields are entered.");
        } else {
        jsonResponseBody.put(USER_MESSAGE_RESPONSE_KEY, USER_MESSAGE);
        }
        */
