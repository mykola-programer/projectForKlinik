package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.Operation;

import java.util.List;

public interface OperationDAO {

    int saveOperation(Operation operation);

    Operation findOperation(int operationID);

    List<Operation> list();
}
