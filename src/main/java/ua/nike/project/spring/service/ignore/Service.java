package ua.nike.project.spring.service;

import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.service.transformation.ServiceTransformation;
import ua.nike.project.spring.vo.VisualObject;

import java.util.List;
import java.util.Map;

public interface Service<T1 extends VisualObject, T2 extends EntityObject> {

    T1 findByID(int entityID) throws ApplicationException;

    T2 getEntityByID(int entityID) throws ApplicationException;

    List<T1> findAll(String namedQuery) throws ApplicationException;

    T1 create(final T1 objectVO) throws ApplicationException;

    T1 update(final int entityID, final T1 objectVO) throws ApplicationException;

    boolean deleteById(final int entityID) throws ApplicationException;

}
