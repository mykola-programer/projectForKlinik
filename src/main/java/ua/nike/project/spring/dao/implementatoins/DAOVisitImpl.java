package ua.nike.project.spring.dao.implementatoins;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DAOVisitImpl implements DAO<Visit> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Visit findByID(int entityID) throws ApplicationException {
        Visit entity = entityManager.find(Visit.class, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<Visit> findAll() {
        return entityManager.createNamedQuery("Visit.findAll", Visit.class).getResultList();
    }

    @Override
    public List<Visit> findAllActive() {
        return entityManager.createNamedQuery("Visit.findAllActive", Visit.class).getResultList();
    }
    @Override
    public Visit save(Visit entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Visit update(Visit entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
        entityManager.remove(entityManager.getReference(Visit.class, entityID));
        return true;
    }

    @Override
    public List<Visit> getVisitsByDate(LocalDate date) {
        return entityManager.createNamedQuery("Visit.findAllActiveByDate", Visit.class).setParameter("date", date).getResultList();
    }
}