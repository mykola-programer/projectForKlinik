package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Operation;

import java.util.List;

@Component
public interface OperationDAO {

    int saveOperation(Operation operation);

    Operation findOperation(int operationID);

    List<Operation> listOperations();
}
