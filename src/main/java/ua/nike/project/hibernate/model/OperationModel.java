package ua.nike.project.hibernate.model;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class OperationModel {

    public static List<OperationBean> getResultOperation(LocalDate selectedDate, EntityManager entityManager) throws NoResultException {

        Query query = entityManager.createNamedQuery("Visit.findAllOperationBeanByOperationDate", OperationBean.class);
        query.setParameter("operationDate", Date.valueOf(selectedDate));
        List<OperationBean> result = query.getResultList();

        if (result.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }
        return result;
    }
}
