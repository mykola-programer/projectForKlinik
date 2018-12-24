package ua.nike.project.spring.dao;

import javax.persistence.Query;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DAOImpl<T extends EntityObject> implements DAO<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> classEO;

    @Override
    public void setClassEO(Class<T> classEO) {
        this.classEO = classEO;
    }

    @Override
    public T findByID(int entityID) throws ApplicationException {
        T entity = entityManager.find(classEO, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<T> findAll(String nQuery, Map<String, Object> parameters) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery(nQuery, classEO);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                namedQuery.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
//        parameters.forEach((key, value) -> namedQuery.setParameter(key, value));
        return namedQuery.getResultList();
    }

    @Override
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
            entityManager.remove(entityManager.getReference(classEO, entityID));
            return true;
    }

    @Override
    public boolean remove(String hqlQuery, List<Integer> entityIDs){
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("IDs", entityIDs);
        query.executeUpdate();
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass) {
        TypedQuery<? extends Object> query = entityManager.createQuery(hqlQuery, oClass);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return query.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass) {
        TypedQuery<? extends Object> query = entityManager.createQuery(hqlQuery, oClass);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return query.getSingleResult();
    }
}
