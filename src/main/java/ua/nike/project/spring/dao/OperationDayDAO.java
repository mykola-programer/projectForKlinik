package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.OperationDay;

import java.util.List;

public interface OperationDayDAO {

    void saveOperationDay(OperationDay operationDay);

    OperationDay findOperationDay(int operationDayID);

    List<OperationDay> list();
}
