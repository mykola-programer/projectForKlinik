package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.OperationVO;

import java.util.List;
import java.util.Set;

@Component
public interface OperationDAO {

    OperationVO findOperation(int operationID) throws BusinessException;

    List<OperationVO> listOperations();

    Integer editOperation(OperationVO operation);

    Integer saveOperation(OperationVO operation);

    List<String> getSurgeons();

    List<String> getManagers();


}
