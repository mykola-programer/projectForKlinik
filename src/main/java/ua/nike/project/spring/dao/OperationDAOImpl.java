package ua.nike.project.spring.dao;

import ua.nike.project.spring.beans.entity.Operation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OperationDAOImpl implements OperationDAO {

    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void saveOperation(Operation operation) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(operation);
        transaction.commit();
        entityManager.close();
    }

    @Override
    public Operation findOperation(int operationID) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        Operation operation = entityManager.find(Operation.class, operationID);
        entityManager.close();
        return operation;
    }

    @Override
    public List<Operation> list() {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        List<Operation> operations = entityManager.createNamedQuery("Operation.findAll").getResultList();
        entityManager.close();
        return operations;
    }
}
