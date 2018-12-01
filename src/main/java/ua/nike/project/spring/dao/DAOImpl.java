package ua.nike.project.spring.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DAOImpl<T extends EntityObject> implements DAO<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public T findByID(int entityID, Class<T> tClass) throws BusinessException {
        T entity = this.entityManager.find(tClass, entityID);
        if (entity == null) throw new BusinessException("This object is not find in database !");
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T> findAll(String namedQuery, Class<T> tClass) {
        return entityManager.createNamedQuery(namedQuery, tClass).getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T save(T entity) {
        entityManager.persist(entity);
        entityManager.flush();  // maybe not need
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T update(T entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean remove(int entityID, Class<T> tClass) throws BusinessException {
        try {
            entityManager.remove(findByID(entityID, tClass));
            return true;
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            throw new BusinessException("Проблеми з доступом до бази даних.");
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T> getEntitiesByNamedQuery(String nQuery, Map<String, Object> parameters, Class<T> tClass) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery(nQuery, tClass);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                namedQuery.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
//        parameters.forEach((key, value) -> namedQuery.setParameter(key, value));
        return namedQuery.getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T> getEntitiesByQuery(String hqlQuery, Map<String, Object> parameters, Class<T> tClass) {
        TypedQuery<T> query = entityManager.createQuery(hqlQuery, tClass);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return query.getResultList();
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
    public Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters) {
        Query query = entityManager.createQuery(hqlQuery);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return query.getSingleResult();
    }
}
