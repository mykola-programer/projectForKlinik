package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.OperationDay;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public List<LocalDate> getOperationDates() {
        return new ArrayList<LocalDate>(entityManager.createNamedQuery("OperationDate.getOperationDates").getResultList());
    }
}
