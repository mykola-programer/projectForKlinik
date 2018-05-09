package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.OperationDay;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OperationDayDAOImpl implements OperationDayDAO {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveOperationDay(OperationDay operationDay) {
        EntityTransaction transaction = this.entityManager.getTransaction();
        transaction.begin();
        this.entityManager.persist(operationDay);
        transaction.commit();
    }

    @Override
    public OperationDay findOperationDay(int operationDayID) {
        return this.entityManager.find(OperationDay.class, operationDayID);
    }

    @Override
    public List<OperationDay> list() {
        return this.entityManager.createNamedQuery("OperationDay.findAll", OperationDay.class).getResultList();
    }
}
