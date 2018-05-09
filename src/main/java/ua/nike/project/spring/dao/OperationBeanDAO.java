package ua.nike.project.spring.dao;


import ua.nike.project.spring.beans.OperationBean;

import java.time.LocalDate;
import java.util.List;

public interface OperationBeanDAO {

    List<OperationBean> list(LocalDate selectedDate);
}
