package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.OperationDay;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class OperationDayDAOImpl implements OperationDayDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int saveOperationDay(OperationDay operationDay) {
        this.entityManager.persist(operationDay);
        return operationDay.getOperationDayId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OperationDay findOperationDay(int operationDayID) {
        return this.entityManager.find(OperationDay.class, operationDayID);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationDay> listOperationDays() {
        return this.entityManager.createNamedQuery("OperationDay.findAll", OperationDay.class).getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<Date> getOperationDates() {
        return new HashSet<Date>(entityManager.createNamedQuery("OperationDay.getUniqueOperationDates").getResultList());
    }
}
