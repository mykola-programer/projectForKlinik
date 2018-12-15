package ua.nike.project.spring.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ua.nike.project.hibernate.entity.OperationType;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DAOOperationTypeImpl implements DAO<OperationType> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OperationType findByID(int entityID) throws ApplicationException {
        OperationType entity = this.entityManager.find(OperationType.class, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<OperationType> findAll() {
        return entityManager.createNamedQuery("OperationType.findAll", OperationType.class).getResultList();
    }

    @Override
    public List<OperationType> findAllActive() {
        return entityManager.createNamedQuery("OperationType.getAllActive", OperationType.class).getResultList();
    }

    @Override
    public OperationType save(OperationType entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public OperationType update(OperationType entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
        entityManager.remove(entityManager.getReference(OperationType.class, entityID));
        return true;
    }

}