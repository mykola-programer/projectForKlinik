package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.AccomodationVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface AccomodationDAO {

    int addAccomodation(AccomodationVO accomodationVO);

    boolean lockAccomodationPlace(int accomodationID) throws BusinessException;

    boolean unlockAccomodationPlace(int accomodationID) throws BusinessException;

    AccomodationVO findAccomodation(int accomodationID) throws BusinessException;

    List<AccomodationVO> getListUnlockedAccomodations();

    List<AccomodationVO> getListAccomodations();

    List<VisitVO> getListVisitsOfAccomodation(int accomodationID) throws BusinessException;

    AccomodationVO transformToAccomodationVO(Accomodation accomodation);

}
