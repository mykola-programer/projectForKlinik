package ua.nike.project.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.ManagerVO;
import ua.nike.project.spring.vo.VisitVO;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ManagerDAOImpl implements ManagerDAO {

    @Autowired
    private VisitDAO visitDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addManager(ManagerVO managerVO) {
        Manager manager = new Manager();
        this.copyToManager(managerVO, manager);
        this.entityManager.persist(manager);
        this.entityManager.flush();
        return manager.getManagerId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ManagerVO editManager(int managerId, ManagerVO managerVO) throws BusinessException {
        Manager manager = this.entityManager.find(Manager.class, managerId);
        if (manager == null) throw new BusinessException("This manager is not find in database !");
        this.copyToManager(managerVO, manager);
        this.entityManager.flush();
        return transformToManagerVO(manager);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ManagerVO findManager(int managerID) throws BusinessException {
        Manager manager = this.entityManager.find(Manager.class, managerID);
        if (manager == null) throw new BusinessException("This manager is not find in database !");
        return transformToManagerVO(manager);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ManagerVO> getManagers() {
        List<Manager> managers = this.entityManager.createNamedQuery("Manager.findAll", Manager.class).getResultList();
        List<ManagerVO> result = new ArrayList<>();
        for (Manager manager : managers) {
            result.add(transformToManagerVO(manager));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean removeManager(int managerId) {
        try {
            this.entityManager.remove(entityManager.getReference(Manager.class, managerId));
            return true;

        } catch (EntityNotFoundException e) {
            return false;
        }

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getListVisitsOfManager(int managerId) throws BusinessException {
        Manager manager = this.entityManager.find(Manager.class, managerId);
        if (manager == null) throw new BusinessException("This manager is not find in database !");

        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : manager.getVisits()) {
            result.add(this.visitDAO.transformToVisitVO(visit));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ManagerVO transformToManagerVO(Manager manager) {
        if (manager == null) return null;
        ManagerVO result = new ManagerVO();
        result.setManagerId(manager.getManagerId());
        result.setSurname(manager.getSurname());
        result.setFirstName(manager.getFirstName());
        result.setSecondName(manager.getSecondName());
        result.setCityFrom(manager.getCityFrom());
        return result;
    }

    private void copyToManager(ManagerVO original, Manager result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            result.setCityFrom(original.getCityFrom());
        } else result = new Manager();
    }

}
