package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.spring.vo.HospitalizationBeanVO;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public interface HospitalizationBeanDAO {

    List<HospitalizationBeanVO> listHospitalizations(LocalDate selectedDate);

    List<HospitalizationBeanVO> listNoHospitalizations(LocalDate selectedDate);

}
