package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Role;
import ua.nike.project.hibernate.entity.Role;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ValidationException;
import ua.nike.project.spring.vo.RoleVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RoleService {

    private DAO<Role> dao;

    @Autowired
    public void setDao(DAO<Role> dao) {
        this.dao = dao;
        this.dao.setClassEO(Role.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RoleVO findByID(int roleID) {
        return convertToRoleVO(dao.findByID(roleID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Role findRoleByName(String name) {
        return (Role) dao.getObjectByQuery("Role.findByName", Role.class, name);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<RoleVO> findAll() {
        List<Role> entities = dao.findAll("Role.findAll");
        if (entities == null) return null;
        List<RoleVO> result = new ArrayList<>();
        for (Role entity : entities) {
            result.add(convertToRoleVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RoleVO create(RoleVO roleVO) {
        Role entity = copyToRole(roleVO, null);
        return convertToRoleVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RoleVO update(int roleID, RoleVO roleVO) {
        Role currentEntity = dao.findByID(roleID);
        Role updatedEntity = copyToRole(roleVO, currentEntity);
        return convertToRoleVO(dao.update(updatedEntity));
    }

    private RoleVO convertToRoleVO(Role role) {
        if (role == null) return null;
        RoleVO result = new RoleVO();
        result.setRoleId(role.getRoleID());
        result.setName(role.getName());
        return result;
    }

    private Role copyToRole(RoleVO original, Role result) {
        if (original != null) {
            if (result == null) result = new Role();
            result.setName(original.getName());
        }
        return result;
    }
}