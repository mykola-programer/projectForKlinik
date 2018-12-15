package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.EntityObject;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.service.transformation.ServiceTransformation;
import ua.nike.project.spring.vo.VisualObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceDAOImpl<T1 extends VisualObject, T2 extends EntityObject> implements ServiceDAO<T1, T2> {

    @Autowired
    private DAO<T2> dao;

    private ServiceTransformation<T1, T2> serviceTransformation;
    private Class<T1> classVO;
    private Class<T2> classEO;

    @Override
    public void setClasses(Class<T1> classVO, Class<T2> classEO, ServiceTransformation<T1, T2> serviceTransformation) {
        this.classVO = classVO;
        this.classEO = classEO;
        this.dao.setClassEO(this.classEO);
        this.serviceTransformation = serviceTransformation;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public T1 findByID(int entityID) throws ApplicationException {
        T2 e = dao.findByID(entityID);

//        ((Client) e).getVisitsForClient().forEach((visit -> System.out.println(visit)));
        return serviceTransformation.convertToVisualObject(e);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public T2 getEntityByID(int entityID) throws ApplicationException {
        return dao.findByID(entityID);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T1> findAll(String namedQuery) throws ApplicationException {
        List<T2> entities = dao.findAll(namedQuery);
        List<T1> result = new ArrayList<>();
        for (T2 entity : entities) {
            result.add(serviceTransformation.convertToVisualObject(entity));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T1 create(T1 objectVO) throws ApplicationException {
        T2 entity = serviceTransformation.copyToEntityObject(objectVO, null);
        return serviceTransformation.convertToVisualObject(dao.save(entity));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T1 update(int entityID, T1 objectVO) throws ApplicationException {
        T2 originalEntity = dao.findByID(entityID);
        T2 updatedEntity = serviceTransformation.copyToEntityObject(objectVO, originalEntity);
        return serviceTransformation.convertToVisualObject(dao.update(updatedEntity));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int entityID) throws ApplicationException {
//        T2 entity = dao.findByID(entityID);
//        if (!serviceTransformation.isRelated(entity)) {
            return dao.remove(entityID);
//        }
//        return false;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T1> getListByNamedQuery(String nQuery, Map<String, Object> parameters) throws ApplicationException {
        List<T2> entities = dao.getEntitiesByNamedQuery(nQuery, parameters);
        List<T1> result = new ArrayList<>();
        for (T2 entity : entities) {
            result.add(serviceTransformation.convertToVisualObject(entity));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<T1> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws ApplicationException {
        List<T2> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<T1> result = new ArrayList<>();
        for (T2 entity : entities) {
            result.add(serviceTransformation.convertToVisualObject(entity));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends Object> oClass) {
        return dao.getObjectsByQuery(hqlQuery, parameters, oClass);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters) {
        return dao.getObjectByQuery(hqlQuery, parameters);
    }

}













    /*
        private T1 serviceTransformation.convertToVisualObject(T2 entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "Client":
                    return (T1) transformToClientVO((Client) entity);
                case "Visit":
                    return (T1) transformToVisitVO((Visit) entity);
                case "Accomodation":
                    return (T1) transformToAccomodationVO((Accomodation) entity);
                case "Manager":
                    return (T1) transformToManagerVO((Manager) entity);
                case "OperationType":
                    return (T1) transformToOperationTypeVO((OperationType) entity);
                case "Surgeon":
                    return (T1) transformToSurgeonVO((Surgeon) entity);
                case "VisitDate":
                    return (T1) transformToVisitDateVO((VisitDate) entity);

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private VisitVO transformToVisitVO(Visit visit) {
            if (visit == null) return null;

            VisitVO result = new VisitVO();
            result.setVisitId(visit.getVisitId());
            result.setVisitDate(transformToVisitDateVO(visit.getVisitDate()));
            result.setTimeForCome(visit.getTimeForCome());
            result.setOrderForCome(visit.getOrderForCome());
            result.setClient(transformToClientVO(visit.getClient()));
            result.setPatient(transformToClientVO(visit.getPatient()));
            result.setStatus(convertClientStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setManager(transformToManagerVO(visit.getManager()));
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setNote(visit.getNote());
            result.setInactive(visit.getInactive());
            return result;
        }

        private ClientVO transformToClientVO(Client client) {
            if (client == null) return null;
            ClientVO result = new ClientVO();
            result.setClientId(client.getClientId());
            result.setSurname(client.getSurname());
            result.setFirstName(client.getFirstName());
            result.setSecondName(client.getSecondName());
            result.setSex(convertSex(client.getSex()));
            result.setBirthday(client.getBirthday());
            result.setTelephone(client.getTelephone());
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
            if (manager == null) return null;
            ManagerVO result = new ManagerVO();
            result.setManagerId(manager.getManagerId());
            result.setSurname(manager.getSurname());
            result.setFirstName(manager.getFirstName());
            result.setSecondName(manager.getSecondName());
            result.setSex(convertSex(manager.getSex()));
            result.setCityFrom(manager.getCityFrom());
            result.setInactive(manager.isInactive());
            return result;
        }

        private SurgeonVO transformToSurgeonVO(Surgeon surgeon) {
            if (surgeon == null) return null;
            SurgeonVO result = new SurgeonVO();
            result.setSurgeonId(surgeon.getSurgeonId());
            result.setSurname(surgeon.getSurname());
            result.setFirstName(surgeon.getFirstName());
            result.setSecondName(surgeon.getSecondName());
            result.setSex(convertSex(surgeon.getSex()));
            result.setInactive(surgeon.isInactive());
            return result;
        }

        private OperationTypeVO transformToOperationTypeVO(OperationType operationType) {
            if (operationType == null) return null;
            OperationTypeVO operationTypeVO = new OperationTypeVO();
            operationTypeVO.setOperationTypeId(operationType.getOperationTypeId());
            operationTypeVO.setName(operationType.getName());
            operationTypeVO.setInactive(operationType.isInactive());
            return operationTypeVO;
        }

        private VisitDateVO transformToVisitDateVO(VisitDate visitDate) {
            if (visitDate == null) return null;
            VisitDateVO result = new VisitDateVO();
            result.setVisitDateId(visitDate.getVisitDateId());
            result.setDate(visitDate.getDate());
            result.setInactive(visitDate.isInactive());
            return result;
        }

        private T2 serviceTransformation.copyToEntityObject(T1 objectVO, T2 entity) throws ApplicationException {
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
                case "ManagerVO": {
                    if (entity == null) entity = (T2) new Manager();
                    return (T2) copyToManager((ManagerVO) objectVO, (Manager) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (T2) new OperationType();
                    return (T2) copyToOperationType((OperationTypeVO) objectVO, (OperationType) entity);
                }
                case "SurgeonVO": {
                    if (entity == null) entity = (T2) new Surgeon();
                    return (T2) copyToSurgeon((SurgeonVO) objectVO, (Surgeon) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (T2) new Visit();
                    return (T2) copyToVisit((VisitVO) objectVO, (Visit) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (T2) new VisitDate();
                    return (T2) copyToVisitDate((VisitDateVO) objectVO, (VisitDate) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertClientStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getPatient() != null && original.getPatient().getClientId() > 0) {
                    Client patient = new Client();
                    patient.setClientId(original.getPatient().getClientId());
                    result.setPatient(copyToClient(original.getPatient(), patient));
                } else result.setPatient(null);

                if (original.getClient() != null && original.getClient().getClientId() > 0) {
                    Client client = new Client();
                    client.setClientId(original.getClient().getClientId());
                    result.setClient(copyToClient(original.getClient(), client));
                } else result.setClient(null);


                if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                    OperationType operationType = new OperationType();
                    operationType.setOperationTypeId(original.getOperationType().getOperationTypeId());
                    result.setOperationType(copyToOperationType(original.getOperationType(), operationType));
                } else result.setOperationType(null);

                if (original.getSurgeon() != null && original.getSurgeon().getSurgeonId() > 0) {
                    Surgeon surgeon = new Surgeon();
                    surgeon.setSurgeonId(original.getSurgeon().getSurgeonId());
                    result.setSurgeon(copyToSurgeon(original.getSurgeon(), surgeon));
                } else result.setSurgeon(null);

                if (original.getManager() != null && original.getManager().getManagerId() > 0) {
                    Manager manager = new Manager();
                    manager.setManagerId(original.getManager().getManagerId());
                    result.setManager(copyToManager(original.getManager(), manager));
                } else result.setManager(null);

                if (original.getAccomodation() != null && original.getAccomodation().getAccomodationId() > 0) {
                    Accomodation accomodation = new Accomodation();
                    accomodation.setAccomodationId(original.getAccomodation().getAccomodationId());
                    result.setAccomodation(copyToAccomodation(original.getAccomodation(), accomodation));
                } else result.setAccomodation(null);

            }
            return result;
        }

        private VisitDate copyToVisitDate(VisitDateVO original, VisitDate result) {
            if (original != null) {
                result.setDate(original.getDate());
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private Surgeon copyToSurgeon(SurgeonVO original, Surgeon result) {
            if (original != null) {
                result.setSurname(original.getSurname());
                result.setFirstName(original.getFirstName());
                result.setSecondName(original.getSecondName());
                result.setSex(convertSex(original.getSex()));
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private OperationType copyToOperationType(OperationTypeVO original, OperationType result) {
            if (original != null) {
                result.setName(original.getName());
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private Manager copyToManager(ManagerVO original, Manager result) {
            if (original != null) {
                result.setSurname(original.getSurname());
                result.setFirstName(original.getFirstName());
                result.setSecondName(original.getSecondName());
                result.setCityFrom(original.getCityFrom());
                result.setSex(convertSex(original.getSex()));
                result.setInactive(original.isInactive());
            }
            return result;
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
                result.setSex(convertSex(original.getSex()));
                result.setBirthday(original.getBirthday());
                result.setTelephone(original.getTelephone());
            }
            return result;
        }

        private Character convertSex(Sex sex) {
            if (sex == null) return null;
            try {
                switch (sex.toString().charAt(0)) {
                    case 'W':
                    case 'w':
                        return 'Ж';
                    case 'M':
                    case 'm':
                    default:
                        return 'Ч';
                }
            } catch (IndexOutOfBoundsException e) {
                return 'Ч';
            }
        }

        private Sex convertSex(Character symbol) {
            if (symbol == null) return null;
            switch (symbol) {
                case 'Ж':
                case 'ж':
                    return Sex.W;
                case 'Ч':
                case 'ч':
                default:
                    return Sex.M;
            }
        }

        private ClientStatus convertClientStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return ClientStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return ClientStatus.RELATIVE;
            }
        }

        private String convertClientStatus(ClientStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(T2 entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "Client": {
//                Map<String, Object> parametersClient = new HashMap<>();
//                parametersClient.put("client", (Client) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (Client) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.client = :client", parametersClient) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<T2>) Visit.class).size() != 0);
//            }
//            case "Manager": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("manager", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByManager", parameters, (Class<T2>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<T2>) Visit.class).size() != 0;
//            }
//            case "Surgeon": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("surgeon", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findBySurgeon", parameters, (Class<T2>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<T2>) Visit.class).size() != 0;
//            }
//            case "Accomodation": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("accomodation", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByAccomodation", parameters, (Class<T2>) Visit.class).size() != 0;
//            }
//            case "Visit": {
//                return false;
//            }
////-----------------------------------------------------------
//
//            default:
//                throw new ApplicationException("Class not find.");
//        }
        return true;
    }
*/