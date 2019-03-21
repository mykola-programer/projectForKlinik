package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.AccomodationVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class AccomodationService {


    private DAO<Accomodation> dao;

    @Autowired
    public void setDao(DAO<Accomodation> dao) {
        this.dao = dao;
        this.dao.setClassEO(Accomodation.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AccomodationVO findByID(int accomodationID) {
        return convertToAccomodationVO(dao.findByID(accomodationID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Accomodation findEntityByID(int entityID) {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AccomodationVO> findAll() {
        List<Accomodation> entities = dao.findAll("Accomodation.findAll");
        if (entities == null) return null;
        List<AccomodationVO> result = new ArrayList<>();
        for (Accomodation entity : entities) {
            result.add(convertToAccomodationVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccomodationVO create(AccomodationVO accomodationVO) {
        Accomodation entity = copyToAccomodation(accomodationVO, null);
        return convertToAccomodationVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccomodationVO update(int accomodationID, AccomodationVO accomodationVO) {
        Accomodation originalEntity = dao.findByID(accomodationID);
        Accomodation updatedEntity = copyToAccomodation(accomodationVO, originalEntity);
        return convertToAccomodationVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int accomodationID) {
        return dao.remove(accomodationID);
    }


    private Accomodation copyToAccomodation(AccomodationVO original, Accomodation result) {
        if (original != null) {
            if (result == null) result = new Accomodation();
            result.setWard(Ward.valueOf("N" + original.getWard()));
            result.setWardPlace(original.getWardPlace());
            result.setDisable(original.isDisable());
        }
        return result;
    }


    private AccomodationVO convertToAccomodationVO(Accomodation accomodation) {
        if (accomodation == null) return null;
        AccomodationVO result = new AccomodationVO();
        result.setAccomodationId(accomodation.getAccomodationId());
        result.setWard(accomodation.getWard().toInteger());
        result.setWardPlace(accomodation.getWardPlace());
        result.setDisable(accomodation.isDisable());
        return result;
    }
}



/*
        @Transactional(propagation = Propagation.REQUIRED)
        public AccomodationVO deactivateByID(int accomodationID) {
            Accomodation accomodation = dao.findByID(accomodationID);
            accomodation.setDisable(true);
            return convertToAccomodationVO(dao.update(accomodation));
        }

        @Transactional(propagation = Propagation.REQUIRED)
        public AccomodationVO activateByID(int accomodationID) {
            Accomodation accomodation = dao.findByID(accomodationID);
            accomodation.setDisable(false);
            return convertToAccomodationVO(dao.update(accomodation));
        }
    */
/*
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AccomodationVO> findAllActive() {
        List<Accomodation> entities = dao.findAll("Accomodation.getAllActive", null);
        if (entities == null) return null;
        List<AccomodationVO> result = new ArrayList<>();
        for (Accomodation entity : entities) {
            result.add(convertToAccomodationVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Integer> getWards() {
        List<Ward> wards = (List<Ward>) dao.getObjectsByQuery("Accomodation.getWards", null, Ward.class);
        if (wards == null) return null;
        List<Integer> result = new ArrayList<>();
        for (Ward ward : wards) result.add(ward.toInteger());
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Integer> getActiveWards() {
        List<Ward> wards = (List<Ward>) dao.getObjectsByQuery("Accomodation.getActiveWards", null, Ward.class);
        if (wards == null) return null;
        List<Integer> result = new ArrayList<>();
        for (Ward ward : wards) result.add(ward.toInteger());
        return result;
    }
*/
