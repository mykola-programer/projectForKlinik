package ua.nike.project.spring.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DAOClientImpl implements DAO<Client> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Client findByID(int entityID) throws ApplicationException {
        Client entity = entityManager.find(Client.class, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<Client> findAll() {
        return entityManager.createNamedQuery("Client.findAll", Client.class).getResultList();
    }

    @Override
    public Client save(Client entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Client update(Client entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
        entityManager.remove(entityManager.getReference(Client.class, entityID));
        return true;
    }
}










/*

    //    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getEntitiesByNamedQuery(String nQuery, Map<String, Object> parameters) {
        TypedQuery<Client> namedQuery = entityManager.createNamedQuery(nQuery, Client.class);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                namedQuery.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
//        parameters.forEach((key, value) -> namedQuery.setParameter(key, value));
        return namedQuery.getResultList();
    }

    //    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getEntitiesByQuery(String hqlQuery, Map<String, Object> parameters) {
        TypedQuery<Client> query = entityManager.createQuery(hqlQuery, Client.class);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return query.getResultList();
    }

    //    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass) {
        TypedQuery<? extends Object> query = entityManager.createQuery(hqlQuery, oClass);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return query.getResultList();
    }

    //    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters) {
        Query query = entityManager.createQuery(hqlQuery);
        if (parameters != null) {
            for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                query.setParameter(parameter.getKey(), parameter.getValue());
            }
        }
        return query.getSingleResult();
    }
*/

