package ua.nike.project.spring.dao;


import ua.nike.project.hibernate.model.OperationBean;

import java.sql.Date;
import java.util.List;

public interface OperationBeanDAO {

    List<OperationBean> list(Date selectedDate);
}
