package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.User;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.vo.UserVO;

import javax.persistence.EntityNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class UserService {

    private DAO<User> dao;

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
    public boolean loginUser(UserVO userVO) throws ValidationException {
        User user = dao.findByID(userVO.getUserId());
        if (user == null) {
            throw new ValidationException("Uncorrected User !", null);
        } else {
            try {
                return checkPassword(userVO.getPassword(), user.getHashedPassword());
            } catch (NoSuchAlgorithmException e) {
                throw new ValidationException("Error to check password !", null);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<UserVO> findAll() {
        List<User> entities = dao.findAll("User.findAll", null);
        if (entities == null) return null;
        List<UserVO> result = new ArrayList<>();
        for (User entity : entities) {
            result.add(convertToUserVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserVO create(UserVO userVO) throws ValidationException {
        try {
            User entity = copyToUser(userVO, null);
            return convertToUserVO(dao.save(entity));
        } catch (NoSuchAlgorithmException e) {
            throw new ValidationException("Error to save password !", null);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserVO update(int userID, UserVO userVO) throws ValidationException {
        User originalEntity = dao.findByID(userID);
        try {
            if (originalEntity == null) {
                throw new EntityNotFoundException();
            } else if (userVO != null && checkPassword(userVO.getPassword(), originalEntity.getHashedPassword())) {
                userVO.setPassword(userVO.getNewPassword());
                User updatedEntity = copyToUser(userVO, originalEntity);
                return convertToUserVO(dao.update(updatedEntity));
            } else {
                throw new ValidationException("Error to check password !", null);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new ValidationException("Error to check password !", null);
        }
    }


    private UserVO convertToUserVO(User user) {
        if (user == null) return null;
        UserVO result = new UserVO();
        result.setUserId(user.getUserId());
        result.setLogin(user.getLogin());
        result.setPassword(null);
        result.setNewPassword(null);
        return result;
    }

    private User copyToUser(UserVO original, User result) throws NoSuchAlgorithmException {
        if (original != null) {
            if (result == null) result = new User();
            result.setLogin(original.getLogin());
            result.setHashedPassword(encryptPasswordBySHA512(original.getPassword()));
        }
        return result;
    }

    private String encryptPasswordBySHA512(String passwordToHash) throws NoSuchAlgorithmException {
        if (passwordToHash == null || passwordToHash.equals("") || passwordToHash.equals(" ")) {
            passwordToHash = getSalt();
        }
        String salt = getSalt();
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes());
        byte[] bytes = md.digest(passwordToHash.getBytes());
        StringBuilder hashedPassword = new StringBuilder();
        for (byte aByte : bytes) {
            hashedPassword.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return hashedPassword.toString();
    }

    private boolean checkPassword(String password, String shaHash) throws NoSuchAlgorithmException {
        return shaHash.equals(encryptPasswordBySHA512(password));
    }

    private String getSalt() {
        String salt = "96BqJAw2n4J2p#S-G+W_E_%$53dwn-@Qz@^*p=S4hJn7e=m+6DE4VwYuvZhyxUp9eekNaVkd8-RDKYWCUP@XVWpQm!kGNp#gm^Qp_J*UfzLTBLU8*3r#!t=+P4EcR_rH";
        return salt;
    }
}
