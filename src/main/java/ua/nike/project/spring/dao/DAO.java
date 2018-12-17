package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.spring.exceptions.ApplicationException;

import java.util.List;
import java.util.Map;

public interface DAO<T extends EntityObject> {

    void setClassEO(Class<T> classEO);

    T findByID(int entityID) throws ApplicationException;

    List<T> findAll(String namedQuery, Map<String, Object> parameters);

    T save(T entity);

    T update(T entity);

    boolean remove(int entityID);

    List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass);

    Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass);


}
