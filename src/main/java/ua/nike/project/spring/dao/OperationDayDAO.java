package ua.nike.project.spring.dao;

import ua.nike.project.hibernate.entity.OperationDay;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface OperationDayDAO {

    int saveOperationDay(OperationDay operationDay);

    OperationDay findOperationDay(int operationDayID);

    List<OperationDay> listOperationDays();

    Set<Date> getOperationDates();
}
