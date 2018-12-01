package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.OperationType;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.OperationTypeVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface OperationTypeDAO {

    OperationTypeVO findOperationType(int operationTypeID) throws BusinessException;


    int addOperationType(OperationTypeVO operationTypeVO);

    boolean lockOperationType(int operationTypeID) throws BusinessException;

    boolean unlockOperationType(int operationTypeID) throws BusinessException;

    List<OperationTypeVO> getListUnlockedOperationTypes();

    List<OperationTypeVO> getListOperationTypes();

    List<VisitVO> getListVisitsOfOperationType(int operationTypeID) throws BusinessException;

    OperationTypeVO transformToOperationTypeVO(OperationType operationType);


}
