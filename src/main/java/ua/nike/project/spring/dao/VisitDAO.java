package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface VisitDAO {

    VisitVO findVisit(int visitID) throws BusinessException;

    List<VisitVO> getVisits();

    Integer editVisit(VisitVO visitVO);

    Integer saveVisit(VisitVO visitVO);

    VisitVO transformToVisitVO(Visit visit);

}
