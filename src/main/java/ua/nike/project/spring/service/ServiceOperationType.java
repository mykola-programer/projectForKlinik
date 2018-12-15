package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.entity.OperationType;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.AccomodationVO;
import ua.nike.project.spring.vo.OperationTypeVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ServiceOperationType {

    @Autowired
    private DAO<OperationType> dao;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OperationTypeVO findByID(int operationTypeID) throws ApplicationException {
        return convertToOperationTypeVO(dao.findByID(operationTypeID));
    }
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OperationType findEntytiByID(int entityID) throws ApplicationException {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationTypeVO> findAll() {
        List<OperationType> entities = dao.findAll();
        if (entities == null) return null;
        List<OperationTypeVO> result = new ArrayList<>();
        for (OperationType entity : entities) {
            result.add(convertToOperationTypeVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationTypeVO> findAllActive() {
        List<OperationType> entities = dao.findAllActive();
        if (entities == null) return null;
        List<OperationTypeVO> result = new ArrayList<>();
        for (OperationType entity : entities) {
            result.add(convertToOperationTypeVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OperationTypeVO create(OperationTypeVO operationTypeVO) {
        OperationType entity = copyToOperationType(operationTypeVO, null);
        return convertToOperationTypeVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OperationTypeVO update(int operationTypeID, OperationTypeVO operationTypeVO) throws ApplicationException {
        OperationType originalEntity = dao.findByID(operationTypeID);
        OperationType updatedEntity = copyToOperationType(operationTypeVO, originalEntity);
        return convertToOperationTypeVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int operationTypeID) {
        return dao.remove(operationTypeID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OperationTypeVO deactivateByID(int operationTypeID) throws ApplicationException {
        OperationType operationType = dao.findByID(operationTypeID);
        operationType.setInactive(true);
        return convertToOperationTypeVO(dao.update(operationType));
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public OperationTypeVO activateByID(int operationTypeID) throws ApplicationException {
        OperationType operationType = dao.findByID(operationTypeID);
        operationType.setInactive(false);
        return convertToOperationTypeVO(dao.update(operationType));
    }

    private OperationTypeVO convertToOperationTypeVO(OperationType operationType) {
        if (operationType == null) return null;
        OperationTypeVO operationTypeVO = new OperationTypeVO();
        operationTypeVO.setOperationTypeId(operationType.getOperationTypeId());
        operationTypeVO.setName(operationType.getName());
        operationTypeVO.setInactive(operationType.isInactive());
        return operationTypeVO;
    }

    private OperationType copyToOperationType(OperationTypeVO original, OperationType result) {
        if (original != null) {
            result.setName(original.getName());
            result.setInactive(original.isInactive());
        }
        return result;
    }


}














    /*
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationTypeVO> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws
            ApplicationException {
        List<OperationType> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<OperationTypeVO> result = new ArrayList<>();
        for (OperationType entity : entities) {
            result.add(convertToOperationTypeVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends
            Object> oClass) {
        return dao.getObjectsByQuery(hqlQuery, parameters, oClass);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters) {
        return dao.getObjectByQuery(hqlQuery, parameters);
    }
*/
    /*
    
    
//    @Override
//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public OperationType getEntityByID(int operationTypeID) throws ApplicationException {
//        return dao.findByID(operationTypeID);
//    }

        private OperationTypeVO convertToOperationTypeVO(OperationType entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "OperationType":
                    return (OperationTypeVO) transformToOperationTypeVO((OperationType) entity);
                case "Visit":
                    return (OperationTypeVO) transformToVisitVO((Visit) entity);
                case "OperationType":
                    return (OperationTypeVO) transformToOperationTypeVO((OperationType) entity);
                case "Manager":
                    return (OperationTypeVO) transformToManagerVO((Manager) entity);
                case "OperationType":
                    return (OperationTypeVO) transformToOperationTypeVO((OperationType) entity);
                case "Surgeon":
                    return (OperationTypeVO) transformToSurgeonVO((Surgeon) entity);
                case "VisitDate":
                    return (OperationTypeVO) transformToVisitDateVO((VisitDate) entity);

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
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setPatient(transformToOperationTypeVO(visit.getPatient()));
            result.setStatus(convertOperationTypeStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setManager(transformToManagerVO(visit.getManager()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setNote(visit.getNote());
            result.setInactive(visit.getInactive());
            return result;
        }

        private OperationTypeVO transformToOperationTypeVO(OperationType client) {
            if (client == null) return null;
            OperationTypeVO result = new OperationTypeVO();
            result.setOperationTypeId(client.getOperationTypeId());
            result.setSurname(client.getSurname());
            result.setFirstName(client.getFirstName());
            result.setSecondName(client.getSecondName());
            result.setSex(convertSex(client.getSex()));
            result.setBirthday(client.getBirthday());
            result.setTelephone(client.getTelephone());
            return result;
        }

        private OperationTypeVO transformToOperationTypeVO(OperationType operationType) {
            if (operationType == null) return null;
            OperationTypeVO result = new OperationTypeVO();
            result.setOperationTypeId(operationType.getOperationTypeId());
            result.setWard(Integer.valueOf(operationType.getWard().toString().substring(1)));
            result.setWardPlace(operationType.getWardPlace());
            result.setInactive(operationType.isInactive());
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

        private OperationType copyToEntityObject(OperationTypeVO operationTypeVO, OperationType entity) throws ApplicationException {
            if (operationTypeVO == null) return null;
            switch (operationTypeVO.getClass().getSimpleName()) {
                case "OperationTypeVO": {
                    if (entity == null) entity = (OperationType) new OperationType();
                    return (OperationType) copyToOperationType((OperationTypeVO) operationTypeVO, (OperationType) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (OperationType) new OperationType();
                    return (OperationType) copyToOperationType((OperationTypeVO) operationTypeVO, (OperationType) entity);
                }
                case "ManagerVO": {
                    if (entity == null) entity = (OperationType) new Manager();
                    return (OperationType) copyToManager((ManagerVO) operationTypeVO, (Manager) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (OperationType) new OperationType();
                    return (OperationType) copyToOperationType((OperationTypeVO) operationTypeVO, (OperationType) entity);
                }
                case "SurgeonVO": {
                    if (entity == null) entity = (OperationType) new Surgeon();
                    return (OperationType) copyToSurgeon((SurgeonVO) operationTypeVO, (Surgeon) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (OperationType) new Visit();
                    return (OperationType) copyToVisit((VisitVO) operationTypeVO, (Visit) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (OperationType) new VisitDate();
                    return (OperationType) copyToVisitDate((VisitDateVO) operationTypeVO, (VisitDate) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertOperationTypeStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getPatient() != null && original.getPatient().getOperationTypeId() > 0) {
                    OperationType patient = new OperationType();
                    patient.setOperationTypeId(original.getPatient().getOperationTypeId());
                    result.setPatient(copyToOperationType(original.getPatient(), patient));
                } else result.setPatient(null);

                if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                    OperationType client = new OperationType();
                    client.setOperationTypeId(original.getOperationType().getOperationTypeId());
                    result.setOperationType(copyToOperationType(original.getOperationType(), client));
                } else result.setOperationType(null);


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

                if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                    OperationType operationType = new OperationType();
                    operationType.setOperationTypeId(original.getOperationType().getOperationTypeId());
                    result.setOperationType(copyToOperationType(original.getOperationType(), operationType));
                } else result.setOperationType(null);

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

        private OperationType copyToOperationType(OperationTypeVO original, OperationType result) {
            if (original != null) {
                result.setWard(Ward.valueOf("N" + original.getWard()));
                result.setWardPlace(original.getWardPlace());
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private OperationType copyToOperationType(OperationTypeVO original, OperationType result) {
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

        private OperationTypeStatus convertOperationTypeStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return OperationTypeStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return OperationTypeStatus.RELATIVE;
            }
        }

        private String convertOperationTypeStatus(OperationTypeStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(OperationType entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "OperationType": {
//                Map<String, Object> parametersOperationType = new HashMap<>();
//                parametersOperationType.put("client", (OperationType) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (OperationType) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.client = :client", parametersOperationType) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<OperationType>) Visit.class).size() != 0);
//            }
//            case "Manager": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("manager", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByManager", parameters, (Class<OperationType>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<OperationType>) Visit.class).size() != 0;
//            }
//            case "Surgeon": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("surgeon", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findBySurgeon", parameters, (Class<OperationType>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<OperationType>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<OperationType>) Visit.class).size() != 0;
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