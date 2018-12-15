package ua.nike.project.spring.service.transformation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.OperationType;
import ua.nike.project.spring.vo.OperationTypeVO;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceTransformationOperationTypeImpl implements ServiceTransformation<OperationTypeVO, OperationType> {

    @Override
    public OperationTypeVO convertToVisualObject(OperationType operationType) {
        if (operationType == null) return null;
        OperationTypeVO operationTypeVO = new OperationTypeVO();
        operationTypeVO.setOperationTypeId(operationType.getOperationTypeId());
        operationTypeVO.setName(operationType.getName());
        operationTypeVO.setInactive(operationType.isInactive());
        return operationTypeVO;
    }

    @Override
    public OperationType copyToEntityObject(OperationTypeVO original, OperationType result) {
        if (original != null) {
            result.setName(original.getName());
            result.setInactive(original.isInactive());
        }
        return result;
    }

    @Override
    public boolean isRelated(OperationType entity) {
        return !(entity != null && entity.getVisits().size() == 0);
    }
}
