package ua.nike.project.spring.dao;

import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.OperationDay;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class OperationDayDAOImpl implements OperationDayDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public int saveOperationDay(OperationDay operationDay) {
        this.entityManager.persist(operationDay);
        return operationDay.getOperationDayId();
    }

    @Override
    @Transactional
    public OperationDay findOperationDay(int operationDayID) {
        return this.entityManager.find(OperationDay.class, operationDayID);
    }

    @Override
    @Transactional
    public List<OperationDay> listOperationDays() {
        return this.entityManager.createNamedQuery("OperationDay.findAll", OperationDay.class).getResultList();
    }

    @Override
    public Set<Date> getOperationDates() {
        return new TreeSet<Date>(this.entityManager.createNamedQuery("OperationDay.getUniqueOperationDates").getResultList());
    }
}
