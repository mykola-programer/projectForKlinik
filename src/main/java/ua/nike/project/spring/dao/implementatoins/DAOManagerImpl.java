package ua.nike.project.spring.dao.implementatoins;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DAOManagerImpl implements DAO<Manager> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Manager findByID(int entityID) throws ApplicationException {
        Manager entity = entityManager.find(Manager.class, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<Manager> findAll() {
        return entityManager.createNamedQuery("Manager.findAll", Manager.class).getResultList();
    }

    @Override
    public List<Manager> findAllActive() {
        return entityManager.createNamedQuery("Manager.findAllActive", Manager.class).getResultList();
    }
    @Override
    public Manager save(Manager entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Manager update(Manager entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
        entityManager.remove(entityManager.getReference(Manager.class, entityID));
        return true;
    }
}