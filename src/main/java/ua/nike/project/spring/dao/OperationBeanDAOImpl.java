package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.model.OperationBean;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class OperationBeanDAOImpl implements OperationBeanDAO {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<OperationBean> list(LocalDate selectedDate) {
        Query query = this.entityManager.createNamedQuery("Operation.findAllOperationBeanByOperationDate", OperationBean.class);
        query.setParameter("operationDate", Date.valueOf(selectedDate));
        List<OperationBean> operationBeans = query.getResultList();

        if (operationBeans.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }

        return operationBeans;
    }
}
