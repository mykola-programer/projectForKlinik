package ua.nike.project.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.OperationType;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.OperationTypeVO;
import ua.nike.project.spring.vo.VisitVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OperationTypeDAOImpl implements OperationTypeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VisitDAO visitDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addOperationType(OperationTypeVO operationTypeVO) {
        OperationType operationType = new OperationType();
        try {
            operationType = this.entityManager.createQuery("FROM OperationType acc WHERE acc.name=? ", OperationType.class)
                    .setParameter(0, operationTypeVO.getName())
                    .getSingleResult();
            operationType.setInactive(false);

        } catch (PersistenceException e) {
            this.copyToOperationType(operationTypeVO, operationType);
            operationType.setInactive(false);
            this.entityManager.persist(operationType);
            this.entityManager.flush();
        }
        return operationType.getOperationTypeId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean lockOperationType(int operationTypeID) throws BusinessException {
        OperationType operationType = this.entityManager.find(OperationType.class, operationTypeID);
        if (operationType == null) throw new BusinessException("This operationType is not find in database !");
        operationType.setInactive(true);
        this.entityManager.flush();
        return operationType.isInactive();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean unlockOperationType(int operationTypeID) throws BusinessException {
        OperationType operationType = this.entityManager.find(OperationType.class, operationTypeID);
        if (operationType == null) throw new BusinessException("This operationType is not find in database !");
        operationType.setInactive(false);
        this.entityManager.flush();
        return operationType.isInactive();
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OperationTypeVO findOperationType(int operationTypeID) {
        return transformToOperationTypeVO(this.entityManager.find(OperationType.class, operationTypeID));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationTypeVO> getListUnlockedOperationTypes() {
        List<OperationType> operationTypes = this.entityManager.createNamedQuery("OperationType.getAllUnlock", OperationType.class).getResultList();
        List<OperationTypeVO> result = new ArrayList<>();
        for (OperationType operationType : operationTypes) {
            result.add(transformToOperationTypeVO(operationType));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationTypeVO> getListOperationTypes() {
        List<OperationType> operationTypes = this.entityManager.createNamedQuery("OperationType.findAll", OperationType.class).getResultList();
        List<OperationTypeVO> result = new ArrayList<>();
        for (OperationType operationType : operationTypes) {
            result.add(transformToOperationTypeVO(operationType));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getListVisitsOfOperationType(int operationTypeID) throws BusinessException {
        OperationType operationType = this.entityManager.find(OperationType.class, operationTypeID);
        if (operationType == null) throw new BusinessException("This operationType is not find in database !");
        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : operationType.getVisits()) {
            result.add(this.visitDAO.transformToVisitVO(visit));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OperationTypeVO transformToOperationTypeVO(OperationType operationType) {
        if (operationType == null) return null;
        OperationTypeVO operationTypeVO = new OperationTypeVO();
        operationTypeVO.setOperationTypeId(operationType.getOperationTypeId());
        operationTypeVO.setName(operationType.getName());
        operationTypeVO.setLockType(operationType.isInactive());
        return operationTypeVO;
    }

    private void copyToOperationType(OperationTypeVO original, OperationType result) {
        if (original != null) {
            result.setName(original.getName());
            result.setInactive(original.isLockType());
        }
    }
}
