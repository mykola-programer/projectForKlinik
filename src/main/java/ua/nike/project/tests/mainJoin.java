package ua.nike.project.tests;

import ua.nike.project.hibernate.model.EntityManagerFactorySingleton;
import ua.nike.project.hibernate.model.OperationBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class mainJoin {
    public static void main(String[] args) {
        EntityManager em = EntityManagerFactorySingleton.getEntityManagerFactory().createEntityManager();

        Query query = em.createNamedQuery("Operation.selectOperationInDate", OperationBean.class);
        query.setParameter("operationDate", Date.valueOf(LocalDate.of(2018, 4, 15)));
        List<OperationBean> operationBeanList = query.getResultList();

        for (OperationBean operBean : operationBeanList){
            System.out.println(operBean);
        }
        em.close();
        EntityManagerFactorySingleton.getEntityManagerFactory().close();
    }
}
