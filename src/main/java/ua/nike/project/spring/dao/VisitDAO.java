package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisitVO;

import java.time.LocalDate;
import java.util.List;

@Component
public interface VisitDAO {

    VisitVO findVisit(int visitID) throws BusinessException;

    List<VisitVO> getVisits();
    List<VisitVO> getVisitsInDateOfWard(LocalDate date);
    List<VisitVO> getVisitsInDateOfNoWard(LocalDate date);

    Integer editVisit(VisitVO visitVO);

    Integer saveVisit(VisitVO visitVO);

    VisitVO transformToVisitVO(Visit visit);

}
