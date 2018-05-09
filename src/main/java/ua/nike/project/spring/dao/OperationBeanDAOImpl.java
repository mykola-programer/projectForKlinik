package ua.nike.project.spring.dao;

import org.springframework.transaction.PlatformTransactionManager;
import ua.nike.project.spring.beans.OperationBean;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class OperationBeanDAOImpl implements OperationBeanDAO {

    private EntityManagerFactory entityManagerFactory;
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }


    @Override
    public List<OperationBean> list(LocalDate selectedDate) {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        Query query = entityManager.createNamedQuery("Operation.findAllOperationBeanByOperationDate", OperationBean.class);
        query.setParameter("operationDate", Date.valueOf(selectedDate));
        List<OperationBean> operationBeans = query.getResultList();

        if (operationBeans.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }

        entityManager.close();
        return operationBeans;
    }
}
