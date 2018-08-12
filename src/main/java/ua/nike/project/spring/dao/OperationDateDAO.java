package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.OperationDate;
import ua.nike.project.spring.vo.OperationDateVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
public interface OperationDateDAO {

    int saveOperationDate(OperationDate operationDate);

    OperationDateVO findOperationDate(int operationDateID);

    List<OperationDateVO> getListOperationDates();

    List<LocalDate> getDates();

    OperationDateVO saveDate(LocalDate date);

    List<OperationDateVO> saveDates(Set<LocalDate> dates);

}
