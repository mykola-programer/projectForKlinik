package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.Operation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OperationDAOImpl implements OperationDAO {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveOperation(Operation operation) {
        EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        this.entityManager.persist(operation);
        transaction.commit();
    }

    @Override
    public Operation findOperation(int operationID) {
        return this.entityManager.find(Operation.class, operationID);
    }

    @Override
    public List<Operation> list() {
        return this.entityManager.createNamedQuery("Operation.findAll", Operation.class).getResultList();
    }
}
