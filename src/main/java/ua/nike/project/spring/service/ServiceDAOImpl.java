package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.*;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceDAOImpl<T1 extends VisualObject, T2 extends EntityObject> implements ServiceDAO<T1, T2> {

    @Autowired
    DAO<T2> dao;

    @Override
    public T1 findByID(int entityID, Class<T2> eoClass) throws BusinessException {
        return transformToVisualObject(getEntityByID(entityID, eoClass));
    }

    @Override
    public T2 getEntityByID(int entityID, Class<T2> eoClass) throws BusinessException {
        return dao.findByID(entityID, eoClass);
    }

    @Override
    public List<T1> findAll(String namedQuery, Class<T2> eoClass) throws BusinessException {
        List<T2> entities = dao.findAll(namedQuery, eoClass);
        List<T1> result = new ArrayList<>();
        for (T2 entity : entities) {
            result.add(transformToVisualObject(entity));
        }
        return result;
    }

    @Override
    public T1 create(T1 objectVO) throws BusinessException {
        T2 entity = copyToEntity(objectVO, null);
        return transformToVisualObject(dao.save(entity));
    }

    @Override
    public T1 update(int entityID, T1 objectVO, Class<T2> eoClass) throws BusinessException {
        T2 originalEntity = getEntityByID(entityID, eoClass);
        T2 updatedEntity = copyToEntity(objectVO, originalEntity);
        // maybe we should not do "dao.update", because entity auto update(flash) to database
        return transformToVisualObject(dao.update(updatedEntity));
    }

    @Override
    public boolean deleteById(int entityID, Class<T2> eoClass) throws BusinessException {
        T2 entity = getEntityByID(entityID, eoClass);
        if (!isRelated(entity)) {
            return dao.remove(entityID, eoClass);
        }
        return false;
    }

    @Override
    public List<T1> getListByNamedQuery(String nQuery, Map<String, Object> parameters, Class<T2> eoClass) throws BusinessException {
        List<T2> entities = dao.getEntitiesByNamedQuery(nQuery, parameters, eoClass);
        List<T1> result = new ArrayList<>();
        for (T2 entity : entities) {
            result.add(transformToVisualObject(entity));
        }
        return result;
    }

    @Override
    public List<T1> getListByQuery(String hqlQuery, Map<String, Object> parameters, Class<T2> eoClass) throws BusinessException {
        List<T2> entities = dao.getEntitiesByQuery(hqlQuery, parameters, eoClass);
        List<T1> result = new ArrayList<>();
        for (T2 entity : entities) {
            result.add(transformToVisualObject(entity));
        }
        return result;
    }

    @Override
    public List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass) {
        return dao.getObjectsByQuery(hqlQuery, parameters, oClass);
    }

    @Override
    public Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters) {
        return dao.getObjectByQuery(hqlQuery, parameters);
    }

    // ------------------------------------------------ //

    private T1 transformToVisualObject(T2 entity) throws BusinessException {
        if (entity == null) return null;
        switch (entity.getClass().getSimpleName()) {
            case "Client":
                return (T1) transformToClientVO((Client) entity);
            case "Visit":
                return (T1) transformToVisitVO((Visit) entity);
            case "Accomodation":
                return (T1) transformToAccomodationVO((Accomodation) entity);
/*
            case "Manager":
//                return transformToManagerVO(obj);
            case "OperationType":
//                return transformToOperationTypeVO(obj);
            case "Surgeon":
//                return transformToSurgeonVO(obj);
            case "VisitDate":
//                return transformToVisitDateVO(obj);
             */
            default:
                throw new BusinessException("Class not find.");
        }
    }

    private ClientVO transformToClientVO(Client client) {
        if (client == null) return null;
        ClientVO result = new ClientVO();
        result.setClientId(client.getClientId());
        result.setSurname(client.getSurname());
        result.setFirstName(client.getFirstName());
        result.setSecondName(client.getSecondName());
        try {
            switch (client.getSex().toString().charAt(0)) {
                case 'W':
                case 'w':
                    result.setSex('Ж');
                    break;
                case 'M':
                case 'm':
                default:
                    result.setSex('Ч');
            }
        } catch (IndexOutOfBoundsException e) {
            result.setSex('Ч');
        }
        result.setBirthday(client.getBirthday());
        result.setTelephone(client.getTelephone());
        return result;
    }

    private VisitVO transformToVisitVO(Visit visit) {
        if (visit == null) return null;

        VisitVO result = new VisitVO();
        result.setVisitId(visit.getVisitId());
        result.setVisitDate(transformToVisitDateVO(visit.getVisitDate()));
        result.setTimeForCome(visit.getTimeForCome());
        result.setOrderForCome(visit.getOrderForCome());
        try {
            result.setClient((ClientVO) findByID(visit.getClient() != null ? visit.getClient().getClientId() : 0, (Class<T2>) Client.class));
        } catch (BusinessException e) {
            result.setClient(null);
        }

        switch (visit.getStatus().toString().toUpperCase()) {
            case "RELATIVE":
                result.setStatus("супров.");
                break;
            case "PATIENT":
            default:
                result.setStatus("пацієнт");
        }

        try {
            result.setPatient((ClientVO) findByID(visit.getPatient() != null ? visit.getPatient().getClientId() : 0, (Class<T2>) Client.class));
        } catch (BusinessException e) {
            result.setPatient(null);
        }

        result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
        result.setEye(visit.getEye());
        result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
        result.setManager(transformToManagerVO(visit.getManager()));
        result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
        result.setNote(visit.getNote());
        return result;
    }

    private AccomodationVO transformToAccomodationVO(Accomodation accomodation) {
        if (accomodation == null) return null;
        AccomodationVO result = new AccomodationVO();
        result.setAccomodationId(accomodation.getAccomodationId());
        result.setWard(Integer.valueOf(accomodation.getWard().toString().substring(1)));
        result.setWardPlace(accomodation.getWardPlace());
        result.setInactive(accomodation.isInactive());
        return result;
    }

    private ManagerVO transformToManagerVO(Manager manager) {
        return null;
    }

    private SurgeonVO transformToSurgeonVO(Surgeon surgeon) {
        return null;
    }

    private OperationTypeVO transformToOperationTypeVO(OperationType operationType) {
        return null;
    }

    private VisitDateVO transformToVisitDateVO(VisitDate visitDate) {
        return null;
    }

    private T2 copyToEntity(T1 objectVO, T2 entity) throws BusinessException {
        if (objectVO == null) return null;
        switch (objectVO.getClass().getSimpleName()) {
            case "ClientVO": {
                if (entity == null) entity = (T2) new Client();
                return (T2) copyToClient((ClientVO) objectVO, (Client) entity);
            }
            case "AccomodationVO": {
                if (entity == null) entity = (T2) new Accomodation();
                return (T2) copyToAccomodation((AccomodationVO) objectVO, (Accomodation) entity);
            }
//-----------------------------------------------------------

            default:
                throw new BusinessException("Class not find.");
        }
    }

    private Accomodation copyToAccomodation(AccomodationVO original, Accomodation result) {
        if (original != null) {
            result.setWard(Ward.valueOf("N" + original.getWard()));
            result.setWardPlace(original.getWardPlace());
            result.setInactive(original.isInactive());
        }
        return result;
    }

    private Client copyToClient(ClientVO original, Client result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            switch (original.getSex()) {
                case 'Ж':
                case 'ж':
                    result.setSex(Sex.W);
                    break;
                case 'Ч':
                case 'ч':
                default:
                    result.setSex(Sex.M);
            }
            result.setBirthday(original.getBirthday());
            result.setTelephone(original.getTelephone());
        }
        return result;
    }

    private boolean isRelated(T2 entity) throws BusinessException {
        if (entity == null) throw new BusinessException("Entity is not correct.");
        switch (entity.getClass().getSimpleName()) {
            case "Client": {
                Map<String, Object> parametersClient = new HashMap<>();
                parametersClient.put("client", (Client) entity);
                Map<String, Object> parametersPatient = new HashMap<>();
                parametersPatient.put("patient", (Client) entity);
                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.client = :client", parametersClient) != 0
                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<T2>) Visit.class).size() != 0);
            }
//-----------------------------------------------------------

            default:
                throw new BusinessException("Class not find.");
        }
    }
}
