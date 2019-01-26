package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.VisitService;
import ua.nike.project.spring.vo.MyObjectVOList;
import ua.nike.project.spring.vo.VisitVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visits")
public class VisitRESTController implements RESTController<VisitVO> {

    @Autowired
    private VisitService visitService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO getByID(@PathVariable("id") int visitID) {
        return visitService.findByID(visitID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getAll() {
        return visitService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO add(@RequestBody @NotNull @Valid VisitVO visitVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitService.create(visitVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO editByID(@PathVariable("id") int visitID, @RequestBody @NotNull @Valid VisitVO visitVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitService.update(visitID, visitVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int visitID) {
        return visitService.deleteById(visitID);
    }

    @CrossOrigin
    @RequestMapping(value = "/all/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDate(@PathVariable("date") String reqDate) throws ApplicationException {
        return visitService.getVisitsByDate(convertToDate(reqDate));
    }

    private LocalDate convertToDate(String reqDate) throws ApplicationException {
        try {
            final String DATE_FORMAT = "dd.MM.yyyy";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            return LocalDate.parse(reqDate, formatter);
        } catch (RuntimeException e) {
            throw new ApplicationException("incorrect.date");
        }
    }
}


















    /*

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> putVisits(@RequestBody @NotNull @Valid MyObjectVOList<VisitVO> visitsVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitService.putVisits(visitsVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/all_wards/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDateWithWards(@PathVariable("date") String reqDate) throws ApplicationException {

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate, formatter);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("date", date);

            return serviceVisit.getListByNamedQuery("Visit.findAllByDateWithWards", parameters);
        } else {
            throw new ApplicationException("Dates is not correct !");
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/no_wards/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDateWithoutWard(@PathVariable("date") String reqDate) throws ApplicationException {

        if (reqDate != null && !reqDate.equals("")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate date = LocalDate.parse(reqDate, formatter);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("date", date);

            return serviceVisit.getListByNamedQuery("Visit.findAllByDateWithoutWards", parameters);
        } else {
            throw new ApplicationException("Dates is not correct !");
        }
    }
*/

/*    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> addVisits(@RequestBody List<VisitVO> visitsVO) {
        return visitDAO.saveVisits(visitsVO);
    }*/


