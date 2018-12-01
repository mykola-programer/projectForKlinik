package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.VisitDate;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisitDateVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface VisitDateDAO {

    VisitDateVO findVisitDate(int visitDateID) throws BusinessException;


    VisitDateVO addVisitDate(VisitDateVO visitDateVO) throws BusinessException;

    List<VisitDateVO> addVisitDates(List<VisitDateVO> visitDateVOList) throws BusinessException;

    boolean lockVisitDate(int visitDateID);

    boolean lockAllVisitDates();

    boolean unlockVisitDate(int visitDateID);

    List<VisitDateVO> getListUnlockedVisitDates();

    List<VisitDateVO> getVisitDates();

    List<VisitVO> getListVisitsOfVisitDate(int visitDateID) throws BusinessException;

    VisitDateVO transformToVisitDateVO(VisitDate visitDate);

}
