package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.spring.exceptions.BusinessException;

import java.util.List;
import java.util.Map;

public interface DAO<T extends EntityObject> {

    T findByID(int entityID, Class<T> tClass) throws BusinessException;

    List<T> findAll(String namedQuery, Class<T> tClass);

    T save(T entity);

    T update(T entity);

    boolean remove(int entityID, Class<T> tClass) throws BusinessException;

    List<T> getEntitiesByNamedQuery(String query, Map<String, Object> parameters, Class<T> tClass);

    List<T> getEntitiesByQuery(String query, Map<String, Object> parameters, Class<T> tClass);

    List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass);

    Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters);


}
