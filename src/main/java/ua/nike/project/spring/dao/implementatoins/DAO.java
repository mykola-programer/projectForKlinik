package ua.nike.project.spring.dao.implementatoins;

import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.exceptions.ApplicationException;

import java.time.LocalDate;
import java.util.List;

public interface DAO<T extends EntityObject> {

    T findByID(int entityID) throws ApplicationException;

    List<T> findAll();

    default List<T> findAllActive() {
        return findAll();
    }

    T save(T entity);

    T update(T entity);

    boolean remove(int entityID);

    default List<Ward> getActiveWards() {
        return null;
    }

    default List<Visit> getVisitsByDate(LocalDate date) {
        return null;
    }

}
