package ua.nike.project.spring.service.transformation;

import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.VisualObject;

public interface ServiceTransformation<T1 extends VisualObject, T2 extends EntityObject> {

    T1 convertToVisualObject(T2 entity) throws ApplicationException;

    T2 copyToEntityObject(T1 original, T2 result) throws ApplicationException;

    boolean isRelated(T2 entity);

}
