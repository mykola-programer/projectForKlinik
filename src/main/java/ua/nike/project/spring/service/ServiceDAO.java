package ua.nike.project.spring.service;

import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisualObject;

import java.util.List;
import java.util.Map;

public interface ServiceDAO<T1 extends VisualObject, T2 extends EntityObject> {

    T1 findByID(int entityID, Class<T2> eoClass) throws BusinessException;

    T2 getEntityByID(int entityID, Class<T2> eoClass) throws BusinessException;

    List<T1> findAll(String namedQuery, Class<T2> eoClass) throws BusinessException;

    T1 create(final T1 entity) throws BusinessException;

    T1 update(final int entityID, final T1 objectVO, Class<T2> eoClass) throws BusinessException;

    boolean deleteById(final int entityID, Class<T2> eoClass) throws BusinessException;

    List<T1> getListByNamedQuery(String nQuery, Map<String, Object> parameters, Class<T2> eoClass) throws BusinessException;

    List<T1> getListByQuery(String hqlQuery, Map<String, Object> parameters, Class<T2> eoClass) throws BusinessException;

}
