package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.EntityObject;

import java.util.List;
import java.util.Map;

public interface DAO<T extends EntityObject> {

    void setClassEO(Class<T> classEO);

    T findByID(int entityID);

    default List<T> findAll(String nQuery) {
        return findAll(nQuery, null, 0, 0);
    }

    default List<T> findAll(String nQuery, Object[] parameters) {
        return findAll(nQuery, parameters, 0, 0);
    }

    List<T> findAll(String nQuery, Object[] parameters, int limit, int offset);

    T save(T entity);

    T update(T entity);

    boolean remove(int entityID);

    @Deprecated
    boolean remove(String namedQuery, List<Integer> entityIDs);

    List<? extends Object> getObjectsByQuery(String namedQuery, Map<String, Object> parameters, Class<? extends Object> oClass);

    Object getObjectByQuery(String hqlQuery, Object[] parameters, Class<? extends Object> oClass);


}
