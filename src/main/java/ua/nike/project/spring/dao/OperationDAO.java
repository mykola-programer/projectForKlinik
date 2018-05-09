package ua.nike.project.spring.dao;

import ua.nike.project.spring.beans.entity.Operation;

import java.util.List;

public interface OperationDAO {

    void saveOperation(Operation operation);

    Operation findOperation(int operationID);

    List<Operation> list();
}
