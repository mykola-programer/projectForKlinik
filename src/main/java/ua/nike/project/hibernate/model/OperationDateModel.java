package ua.nike.project.hibernate.model;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public class OperationDateModel {

    public static List<Date> getOperationDates(EntityManager entityManager) throws NoResultException {
        List<Date> result = new ArrayList<Date>(entityManager.createNamedQuery("getUniqueOperationDates").getResultList());
        if (result.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }
        return result;
    }

}
