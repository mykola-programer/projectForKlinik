package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.ManagerVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ServiceManager {

    private DAO<Manager> dao;

    @Autowired
    public void setDao(DAO<Manager> dao) {
        this.dao = dao;
        this.dao.setClassEO(Manager.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ManagerVO findByID(int managerID) throws ApplicationException {
        return convertToManagerVO(dao.findByID(managerID));
    }
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Manager findEntytiByID(int entityID) throws ApplicationException {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ManagerVO> findAll() {
        List<Manager> entities = dao.findAll("Manager.findAll",null);
        if (entities == null) return null;
        List<ManagerVO> result = new ArrayList<>();
        for (Manager entity : entities) {
            result.add(convertToManagerVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ManagerVO> findAllActive() {
        List<Manager> entities = dao.findAll("Manager.findAllActive", null);
        if (entities == null) return null;
        List<ManagerVO> result = new ArrayList<>();
        for (Manager entity : entities) {
            result.add(convertToManagerVO(entity));
        }
        return result;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ManagerVO create(ManagerVO managerVO) {
        Manager entity = copyToManager(managerVO, null);
        return convertToManagerVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerVO update(int managerID, ManagerVO managerVO) throws ApplicationException {
        Manager originalEntity = dao.findByID(managerID);
        Manager updatedEntity = copyToManager(managerVO, originalEntity);
        return convertToManagerVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int managerID) {
        return dao.remove(managerID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerVO deactivateByID(int managerID) throws ApplicationException {
        Manager manager = dao.findByID(managerID);
        manager.setInactive(true);
        return convertToManagerVO(dao.update(manager));
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerVO activateByID(int managerID) throws ApplicationException {
        Manager manager = dao.findByID(managerID);
        manager.setInactive(false);
        return convertToManagerVO(dao.update(manager));
    }

    private ManagerVO convertToManagerVO(Manager manager) {
        if (manager == null) return null;
        ManagerVO result = new ManagerVO();
        result.setManagerId(manager.getManagerId());
        result.setSurname(manager.getSurname());
        result.setFirstName(manager.getFirstName());
        result.setSecondName(manager.getSecondName());
        result.setSex(manager.getSex().toCharacter());
        result.setCityFrom(manager.getCityFrom());
        result.setInactive(manager.isInactive());
        return result;
    }

    private Manager copyToManager(ManagerVO original, Manager result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            result.setCityFrom(original.getCityFrom());
            result.setSex(Sex.getInstance(original.getSex()));
            result.setInactive(original.isInactive());
        }
        return result;
    }

}














    /*
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ManagerVO> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws
            ApplicationException {
        List<Manager> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<ManagerVO> result = new ArrayList<>();
        for (Manager entity : entities) {
            result.add(convertToManagerVO(entity));
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
//    public Manager getEntityByID(int managerID) throws ApplicationException {
//        return dao.findByID(managerID);
//    }

        private ManagerVO convertToManagerVO(Manager entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "Manager":
                    return (ManagerVO) transformToManagerVO((Manager) entity);
                case "Visit":
                    return (ManagerVO) transformToVisitVO((Visit) entity);
                case "Accomodation":
                    return (ManagerVO) transformToAccomodationVO((Accomodation) entity);
                case "Manager":
                    return (ManagerVO) transformToManagerVO((Manager) entity);
                case "OperationType":
                    return (ManagerVO) transformToOperationTypeVO((OperationType) entity);
                case "Surgeon":
                    return (ManagerVO) transformToSurgeonVO((Surgeon) entity);
                case "VisitDate":
                    return (ManagerVO) transformToVisitDateVO((VisitDate) entity);

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
            result.setManager(transformToManagerVO(visit.getManager()));
            result.setPatient(transformToManagerVO(visit.getPatient()));
            result.setStatus(convertManagerStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setManager(transformToManagerVO(visit.getManager()));
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setNote(visit.getNote());
            result.setInactive(visit.getInactive());
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
            result.setBirthday(manager.getBirthday());
            result.setTelephone(manager.getTelephone());
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

        private Manager copyToEntityObject(ManagerVO managerVO, Manager entity) throws ApplicationException {
            if (managerVO == null) return null;
            switch (managerVO.getClass().getSimpleName()) {
                case "ManagerVO": {
                    if (entity == null) entity = (Manager) new Manager();
                    return (Manager) copyToManager((ManagerVO) managerVO, (Manager) entity);
                }
                case "AccomodationVO": {
                    if (entity == null) entity = (Manager) new Accomodation();
                    return (Manager) copyToAccomodation((AccomodationVO) managerVO, (Accomodation) entity);
                }
                case "ManagerVO": {
                    if (entity == null) entity = (Manager) new Manager();
                    return (Manager) copyToManager((ManagerVO) managerVO, (Manager) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (Manager) new OperationType();
                    return (Manager) copyToOperationType((OperationTypeVO) managerVO, (OperationType) entity);
                }
                case "SurgeonVO": {
                    if (entity == null) entity = (Manager) new Surgeon();
                    return (Manager) copyToSurgeon((SurgeonVO) managerVO, (Surgeon) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (Manager) new Visit();
                    return (Manager) copyToVisit((VisitVO) managerVO, (Visit) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (Manager) new VisitDate();
                    return (Manager) copyToVisitDate((VisitDateVO) managerVO, (VisitDate) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertManagerStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getPatient() != null && original.getPatient().getManagerId() > 0) {
                    Manager patient = new Manager();
                    patient.setManagerId(original.getPatient().getManagerId());
                    result.setPatient(copyToManager(original.getPatient(), patient));
                } else result.setPatient(null);

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

        private Manager copyToManager(ManagerVO original, Manager result) {
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

        private ManagerStatus convertManagerStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return ManagerStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return ManagerStatus.RELATIVE;
            }
        }

        private String convertManagerStatus(ManagerStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(Manager entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "Manager": {
//                Map<String, Object> parametersManager = new HashMap<>();
//                parametersManager.put("manager", (Manager) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (Manager) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.manager = :manager", parametersManager) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<Manager>) Visit.class).size() != 0);
//            }
//            case "Manager": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("manager", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByManager", parameters, (Class<Manager>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<Manager>) Visit.class).size() != 0;
//            }
//            case "Surgeon": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("surgeon", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findBySurgeon", parameters, (Class<Manager>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<Manager>) Visit.class).size() != 0;
//            }
//            case "Accomodation": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("accomodation", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByAccomodation", parameters, (Class<Manager>) Visit.class).size() != 0;
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