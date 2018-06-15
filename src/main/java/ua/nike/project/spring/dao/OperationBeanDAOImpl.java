package ua.nike.project.spring.dao;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.model.OperationBean;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.List;

public class OperationBeanDAOImpl implements OperationBeanDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationBean> list(Date selectedDate) {
        Query query = this.entityManager.createNamedQuery("Operation.findAllOperationBeanByOperationDate", OperationBean.class);
        query.setParameter("operationDate", selectedDate);
        List<OperationBean> operationBeans = query.getResultList();

        if (operationBeans.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }

        return operationBeans;
    }
}
