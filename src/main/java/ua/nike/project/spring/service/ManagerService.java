package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.ManagerVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ManagerService {

    private DAO<Manager> dao;

    @Autowired
    public void setDao(DAO<Manager> dao) {
        this.dao = dao;
        this.dao.setClassEO(Manager.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ManagerVO findByID(int managerID) {
        return convertToManagerVO(dao.findByID(managerID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Manager findEntityByID(int entityID) {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ManagerVO> findAll() {
        List<Manager> entities = dao.findAll("Manager.findAll", null);
        if (entities == null) return null;
        List<ManagerVO> result = new ArrayList<>();
        for (Manager entity : entities) {
            result.add(convertToManagerVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerVO create(ManagerVO managerVO) {
        Manager entity = copyToManager(managerVO, null);
        return convertToManagerVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerVO update(int managerID, ManagerVO managerVO) {
        Manager originalEntity = dao.findByID(managerID);
        Manager updatedEntity = copyToManager(managerVO, originalEntity);
        return convertToManagerVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int managerID) {
        return dao.remove(managerID);
    }

    private ManagerVO convertToManagerVO(Manager manager) {
        if (manager == null) return null;
        ManagerVO result = new ManagerVO();
        result.setManagerId(manager.getManagerId());
        result.setSurname(manager.getSurname());
        result.setFirstName(manager.getFirstName());
        result.setSecondName(manager.getSecondName());
        result.setSex(manager.getSex().toCharacter());
        result.setCity(manager.getCity());
        result.setDisable(manager.isDisable());
        return result;
    }

    private Manager copyToManager(ManagerVO original, Manager result) {
        if (original != null) {
            if (result == null) result = new Manager();
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            result.setCity(original.getCity());
            result.setSex(Sex.getInstance(original.getSex()));
            result.setDisable(original.isDisable());
        }
        return result;
    }
}










    /*

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ManagerVO> putManagers(List<ManagerVO> managersVO) {
        List<ManagerVO> result = new ArrayList<>();
        for (ManagerVO managerVO : managersVO) {
            if (managerVO != null) {
                if (managerVO.getManagerId() > 0) {
                    result.add(update(managerVO.getManagerId(), managerVO));
                } else {
                    result.add(create(managerVO));
                }
            }
        }
        return result;
    }
*/