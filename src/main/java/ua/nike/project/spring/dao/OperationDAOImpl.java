package ua.nike.project.spring.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Operation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class OperationDAOImpl implements OperationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional (propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public int saveOperation(Operation operation) {
        this.entityManager.persist(operation);
        return operation.getOperationId();
    }

    @Override
    @Transactional (propagation = Propagation.SUPPORTS ,readOnly = true)
    public Operation findOperation(int operationID) {
        return this.entityManager.find(Operation.class, operationID);
    }

    @Override
    @Transactional (propagation = Propagation.SUPPORTS ,readOnly = true)
    public List<Operation> listOperations() {
        return this.entityManager.createNamedQuery("Operation.findAll", Operation.class).getResultList();
    }
}
