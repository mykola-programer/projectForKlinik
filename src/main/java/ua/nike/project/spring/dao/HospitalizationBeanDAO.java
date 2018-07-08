package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.spring.vo.HospitalizationBeanVO;

import java.sql.Date;
import java.util.List;

@Component
public interface HospitalizationBeanDAO {

    List<HospitalizationBeanVO> listHospitalizations(Date selectedDate);

    List<HospitalizationBeanVO> listNoHospitalizations(Date selectedDate);

}
