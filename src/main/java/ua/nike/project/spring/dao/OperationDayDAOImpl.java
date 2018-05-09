package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.OperationDay;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OperationDayDAOImpl implements OperationDayDAO {

    private EntityManagerFactory entityManagerFactory;

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void saveOperationDay(OperationDay operationDay) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(operationDay);
        transaction.commit();
        entityManager.close();
    }

    @Override
    public OperationDay findOperationDay(int operationDayID) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        OperationDay operationDay = entityManager.find(OperationDay.class, operationDayID);
        entityManager.close();
        return operationDay;
    }

    @Override
    public List<OperationDay> list() {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        List<OperationDay> operationDays = entityManager.createNamedQuery("OperationDay.findAll").getResultList();
        entityManager.close();
        return operationDays;
    }
}
