package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.VisitService;
import ua.nike.project.spring.vo.MyObjectVOList;
import ua.nike.project.spring.vo.VisitVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visits")
public class VisitRESTController {

    @Autowired
    private VisitService visitService;

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO getVisit(@PathVariable("id") int visitID) {
        return visitService.findByID(visitID);
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisits() {
        return visitService.findAll();
    }

    @CrossOrigin
    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getActiveVisits() {
        return visitService.findAllActive();
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO addVisit(@RequestBody @NotNull @Valid VisitVO visitVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitService.create(visitVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO editVisit(@PathVariable("id") int visitID, @RequestBody @NotNull @Valid VisitVO visitVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitService.update(visitID, visitVO);
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> putVisits(@RequestBody @NotNull @Valid MyObjectVOList<VisitVO> visitsVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitService.putVisits(visitsVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/list/displace", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> displaceVisits(@RequestBody @NotNull @Valid MyObjectVOList<VisitVO> visitsVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors())
            throw new ValidationException("Object is not valid", bindingResult);
        return visitService.displaceVisits(visitsVO.getObjects());
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int visitID) {
        return visitService.deleteById(visitID);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}/deactivate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO deactivateByID(@PathVariable("id") int visitID) {
        return visitService.deactivateByID(visitID);
    }

    @CrossOrigin
    @RequestMapping(value = "/{id}/activate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public VisitVO activateByID(@PathVariable("id") int visitID) {
        return visitService.activateByID(visitID);
    }


    @CrossOrigin
    @RequestMapping(value = "/all/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDate(@PathVariable("date") String reqDate) throws ApplicationException {
        return visitService.getVisitsByDate(convertToDate(reqDate));
    }

    @CrossOrigin
    @RequestMapping(value = "/all_wards/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDateWithWards(@PathVariable("date") String reqDate) throws ApplicationException {
        return visitService.getVisitsByDate(convertToDate(reqDate))
                .stream()
                .filter((VisitVO visitVO) -> visitVO.getAccomodation() != null)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @RequestMapping(value = "/no_wards/{date}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<VisitVO> getVisitsByDateWithoutWard(@PathVariable("date") String reqDate) throws ApplicationException {
        return visitService.getVisitsByDate(convertToDate(reqDate))
                .stream()
                .filter((VisitVO visitVO) -> visitVO.getAccomodation() == null)
                .collect(Collectors.toList());
    }

    private LocalDate convertToDate(String reqDate) throws ApplicationException {
        if (reqDate != null && !reqDate.equals("")) {
            final String DATE_FORMAT = "dd.MM.yyyy";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            return LocalDate.parse(reqDate, formatter);
        } else {
            throw new ApplicationException("incorrect.date");
        }
    }
}


















    /*


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


