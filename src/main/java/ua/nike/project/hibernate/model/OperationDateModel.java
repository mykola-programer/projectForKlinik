package ua.nike.project.hibernate.model;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class OperationDateModel {

    public static List<Date> getOperationDates() throws NoResultException {
//        System.out.println(Thread.currentThread().getName());
        String jpql = "SELECT DISTINCT od.operationDate FROM OperationDay od ORDER BY od.operationDate ";

        EntityManager entityManager = EntityManagerFactorySingleton.getEntityManager();
        List<Date> result = new ArrayList<Date>(entityManager.createQuery(jpql).getResultList());
        if (result.isEmpty()) throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");

        return result;
    }

}
