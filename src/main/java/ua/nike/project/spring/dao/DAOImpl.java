package ua.nike.project.spring.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.EntityObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public T findByID(int entityID) {
        return entityManager.find(classEO, entityID);
    }

    @Override
    public List<T> findAll(String nQuery, Object[] parameters, int limit, int offset) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery(nQuery, classEO);
        if (limit > 0) {
            namedQuery.setMaxResults(limit);
        }
        if (offset >= 0) {
            namedQuery.setFirstResult(offset);
        }
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                namedQuery.setParameter(i + 1, parameters[i]);
            }
        }
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
    @Deprecated
    public boolean remove(String namedQuery, List<Integer> entityIDs) {
        Query query = entityManager.createNamedQuery(namedQuery);
        query.setParameter("IDs", entityIDs);
        query.executeUpdate();
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<? extends Object> getObjectsByQuery(String namedQuery, Map<String, Object> parameters, Class<? extends Object> oClass) {
        TypedQuery<? extends Object> query = entityManager.createNamedQuery(namedQuery, oClass);
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
