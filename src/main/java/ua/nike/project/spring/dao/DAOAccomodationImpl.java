package ua.nike.project.spring.dao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.exceptions.ApplicationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DAOAccomodationImpl implements DAO<Accomodation> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Accomodation findByID(int entityID) throws ApplicationException {
        Accomodation entity = this.entityManager.find(Accomodation.class, entityID);
        if (entity == null) throw new ApplicationException("This object is not find in database !");
        return entity;
    }

    @Override
    public List<Accomodation> findAll() {
        return entityManager.createNamedQuery("Accomodation.findAll", Accomodation.class).getResultList();
    }

    @Override
    public List<Accomodation> findAllActive() {
        return entityManager.createNamedQuery("Accomodation.getAllActive", Accomodation.class).getResultList();
    }

    @Override
    public List<Ward> getActiveWards() {
        return entityManager.createNamedQuery("Accomodation.getActiveWards", Ward.class).getResultList();
    }

    @Override
    public Accomodation save(Accomodation entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Accomodation update(Accomodation entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Override
    public boolean remove(int entityID) {
        entityManager.remove(entityManager.getReference(Accomodation.class, entityID));
        return true;
    }

}