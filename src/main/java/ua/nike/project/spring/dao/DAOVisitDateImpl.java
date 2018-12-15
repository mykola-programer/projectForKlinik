package ua.nike.project.spring.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ua.nike.project.hibernate.entity.VisitDate;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DAOVisitDateImpl implements DAO<VisitDate> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public VisitDate findByID(int entityID) throws ApplicationException {
        VisitDate entity = entityManager.find(VisitDate.class, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<VisitDate> findAll() {
        return entityManager.createNamedQuery("VisitDate.findAll", VisitDate.class).getResultList();
    }

    @Override
    public List<VisitDate> findAllActive() {
        return entityManager.createNamedQuery("VisitDate.findAllActive", VisitDate.class).getResultList();
    }
    @Override
    public VisitDate save(VisitDate entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public VisitDate update(VisitDate entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
        entityManager.remove(entityManager.getReference(VisitDate.class, entityID));
        return true;
    }
}