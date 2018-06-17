package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.OperationDay;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Component
public interface OperationDayDAO {

    int saveOperationDay(OperationDay operationDay);

    OperationDay findOperationDay(int operationDayID);

    List<OperationDay> listOperationDays();

    Set<Date> getOperationDates();
}
