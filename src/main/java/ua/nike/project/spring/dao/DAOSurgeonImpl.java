package ua.nike.project.spring.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DAOSurgeonImpl implements DAO<Surgeon> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Surgeon findByID(int entityID) throws ApplicationException {
        Surgeon entity = entityManager.find(Surgeon.class, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<Surgeon> findAll() {
        return entityManager.createNamedQuery("Surgeon.findAll", Surgeon.class).getResultList();
    }

    @Override
    public List<Surgeon> findAllActive() {
        return entityManager.createNamedQuery("Surgeon.findAllActive", Surgeon.class).getResultList();
    }
    @Override
    public Surgeon save(Surgeon entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Surgeon update(Surgeon entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
        entityManager.remove(entityManager.getReference(Surgeon.class, entityID));
        return true;
    }
}