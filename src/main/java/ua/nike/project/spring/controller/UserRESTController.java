package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.service.UserService;
import ua.nike.project.spring.vo.UserVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserRESTController {

    @Autowired
    private UserService userService;

    @CrossOrigin
//    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserVO getByID(@PathVariable("id") int userID) {
        return userService.findByID(userID);
    }

    @CrossOrigin
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UserVO> getAll() {
        List<UserVO> users = userService.findAll();
        return users;
    }

    @CrossOrigin
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserVO add(@RequestBody @NotNull @Valid UserVO userVO, BindingResult bindingResult) throws ValidationException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new ValidationException("Object is not valid", bindingResult);
        }
        return userService.create(userVO);
    }


    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean deleteByID(@PathVariable("id") int userID) {
        // TODO
        return false;
    }

    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserVO loginUser(@RequestBody @NotNull @Valid UserVO userVO, BindingResult bindingResult) throws ValidationException {
        return userService.loginUser(userVO);
    }



/*    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserVO editByID(@PathVariable("id") int userID, @RequestBody @NotNull @Valid Map<String, UserVO> mapUsers, BindingResult bindingResult) throws ValidationException {
        if (!(bindingResult != null && bindingResult.hasErrors())
                && userID == mapUsers.get("currentUser").getUserId()
                && userID == mapUsers.get("editedUser").getUserId()
                && userService.loginUser(mapUsers.get("currentUser"))) {
            return userService.update(userID, mapUsers.get("editedUser"));
        } else {
            throw new ValidationException("Objects is not valid", bindingResult);
        }
    }*/

//    @CrossOrigin
//    @RequestMapping(value = "/login2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public UserVO loginUser2() {
//        System.out.println(userService.findUserByUsername("user"));
//        return new UserVO();
//    }


}
