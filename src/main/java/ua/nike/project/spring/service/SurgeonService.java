package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.SurgeonVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SurgeonService {

    private DAO<Surgeon> dao;

    @Autowired
    public void setDao(DAO<Surgeon> dao) {
        this.dao = dao;
        this.dao.setClassEO(Surgeon.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurgeonVO findByID(int surgeonID) {
        return convertToSurgeonVO(dao.findByID(surgeonID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Surgeon findEntityByID(int entityID) {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurgeonVO> findAll() {
        List<Surgeon> entities = dao.findAll("Surgeon.findAll", null);
        if (entities == null) return null;
        List<SurgeonVO> result = new ArrayList<>();
        for (Surgeon entity : entities) {
            result.add(convertToSurgeonVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SurgeonVO create(SurgeonVO surgeonVO) {
        Surgeon entity = copyToSurgeon(surgeonVO, null);
        return convertToSurgeonVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SurgeonVO update(int surgeonID, SurgeonVO surgeonVO) {
        Surgeon originalEntity = dao.findByID(surgeonID);
        Surgeon updatedEntity = copyToSurgeon(surgeonVO, originalEntity);
        return convertToSurgeonVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int surgeonID) {
        return dao.remove(surgeonID);
    }


    private SurgeonVO convertToSurgeonVO(Surgeon surgeon) {
        if (surgeon == null) return null;
        SurgeonVO result = new SurgeonVO();
        result.setSurgeonId(surgeon.getSurgeonId());
        result.setSurname(surgeon.getSurname());
        result.setFirstName(surgeon.getFirstName());
        result.setSecondName(surgeon.getSecondName());
        result.setCity(surgeon.getCity());
        result.setSex(surgeon.getSex().toCharacter());
        result.setDisable(surgeon.isDisable());
        return result;
    }

    private Surgeon copyToSurgeon(SurgeonVO original, Surgeon result) {
        if (original == null) return null;
        if (result == null) result = new Surgeon();
        result.setSurname(original.getSurname());
        result.setFirstName(original.getFirstName());
        result.setSecondName(original.getSecondName());
        result.setCity(original.getCity());
        result.setSex(Sex.getInstance(original.getSex()));
        result.setDisable(original.isDisable());
        return result;
    }

}

