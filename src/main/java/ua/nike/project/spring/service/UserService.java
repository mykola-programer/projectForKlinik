package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.User;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.vo.UserVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private DAO<User> dao;
    @Autowired
    private RoleService roleService;

    @Autowired
    public void setDao(DAO<User> dao) {
        this.dao = dao;
        this.dao.setClassEO(User.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserVO findByID(int userID) {
        return convertToUserVO(dao.findByID(userID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public User findUserByUsername(String username) {
        return (User) dao.getObjectByQuery("User.findByName", User.class, username);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserVO loginUser(UserVO userVO) throws ValidationException {
        User user = findUserByUsername(userVO.getUsername());
        if (user != null && passwordEncoder.matches(userVO.getPassword(), user.getPassword())) {
            return convertToUserVO(user);
        } else {
            throw new ValidationException("Uncorrected User !", null);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserVO> findAll() {
        List<User> entities = dao.findAll("User.findAll");
        if (entities == null) return null;
        List<UserVO> result = new ArrayList<>();
        for (User entity : entities) {
            result.add(convertToUserVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserVO create(UserVO userVO) {
        User entity = copyToUser(userVO, null);
        return convertToUserVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserVO update(int userID, UserVO userVO) {
        User currentEntity = dao.findByID(userID);
        User updatedEntity = copyToUser(userVO, currentEntity);
        return convertToUserVO(dao.update(updatedEntity));
    }

    private UserVO convertToUserVO(User user) {
        if (user == null) return null;
        UserVO result = new UserVO();
        result.setUserId(user.getUserId());
        result.setUsername(user.getUsername());
        result.setPassword(null);
        result.setRole(user.getRole().getName());
        result.setEnabled(user.isEnabled());
        return result;
    }

    private User copyToUser(UserVO original, User result) {
        if (original != null) {
            if (result == null) result = new User();
            result.setUsername(original.getUsername());
            result.setPassword(passwordEncoder.encode(original.getPassword()));
            result.setRole(roleService.findRoleByName(original.getRole()));
            result.setEnabled(original.isEnabled());
        }
        return result;
    }

/*    @Deprecated
    private String encryptPasswordByBCrypt(@NotNull UserVO userVO) throws NoSuchAlgorithmException {
        if (userVO.getPassword() == null) {
            userVO.setPassword("");
        }
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(getSalt().getBytes());
        md.update(userVO.getUsername().getBytes());
        byte[] bytes = md.digest(userVO.getPassword().getBytes());
        StringBuilder hashedPassword = new StringBuilder();
        for (byte aByte : bytes) {
            hashedPassword.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return hashedPassword.toString();
    }*/

/*    @Deprecated
    private boolean checkPassword(UserVO userVO, User user) throws NoSuchAlgorithmException {
        return user.getPassword().equals(encryptPasswordByBCrypt(userVO));
    }*/

/*    @Deprecated
    private String getSalt() {
        String salt = "96BqJAw2n4J2p#S-G+W_E_%$53dwn-@Qz@^*p=S4hJn7e=m+6DE4VwYuvZhyxUp9eekNaVkd8-RDKYWCUP@XVWpQm!kGNp#gm^Qp_J*UfzLTBLU8*3r#!t=+P4EcR_rH";
        return salt;
    }*/
}
