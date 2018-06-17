package ua.nike.project.spring.dao;


import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.model.OperationBean;

import java.sql.Date;
import java.util.List;

@Component
public interface OperationBeanDAO {

    List<OperationBean> list(Date selectedDate);
}
