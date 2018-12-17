package ua.nike.project.spring.service.implementatoins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.VisitDate;
import ua.nike.project.spring.dao.implementatoins.DAO;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.VisitDateVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ServiceVisitDate {

    @Autowired
    private DAO<VisitDate> dao;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VisitDateVO findByID(int visitDateID) throws ApplicationException {
        return convertToVisitDateVO(dao.findByID(visitDateID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VisitDate findEntytiByID(int entityID) throws ApplicationException {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitDateVO> findAll() {
        List<VisitDate> entities = dao.findAll();
        if (entities == null) return null;
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDate entity : entities) {
            result.add(convertToVisitDateVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitDateVO> findAllActive() {
        List<VisitDate> entities = dao.findAllActive();
        if (entities == null) return null;
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDate entity : entities) {
            result.add(convertToVisitDateVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VisitDateVO create(VisitDateVO visitDateVO) {
        VisitDate entity = copyToVisitDate(visitDateVO, null);
        return convertToVisitDateVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VisitDateVO update(int visitDateID, VisitDateVO visitDateVO) throws ApplicationException {
        VisitDate originalEntity = dao.findByID(visitDateID);
        VisitDate updatedEntity = copyToVisitDate(visitDateVO, originalEntity);
        return convertToVisitDateVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int visitDateID) {
        return dao.remove(visitDateID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VisitDateVO deactivateByID(int visitDateID) throws ApplicationException {
        VisitDate visitDate = dao.findByID(visitDateID);
        visitDate.setInactive(true);
        return convertToVisitDateVO(dao.update(visitDate));
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public VisitDateVO activateByID(int visitDateID) throws ApplicationException {
        VisitDate visitDate = dao.findByID(visitDateID);
        visitDate.setInactive(false);
        return convertToVisitDateVO(dao.update(visitDate));
    }

    private VisitDateVO convertToVisitDateVO(VisitDate visitDate) {
        if (visitDate == null) return null;
        VisitDateVO result = new VisitDateVO();
        result.setVisitDateId(visitDate.getVisitDateId());
        result.setDate(visitDate.getDate());
        result.setInactive(visitDate.isInactive());
        return result;
    }

    private VisitDate copyToVisitDate(VisitDateVO original, VisitDate result) {
        if (original != null) {
            result.setDate(original.getDate());
            result.setInactive(original.isInactive());
        }
        return result;
    }

}














    /*
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitDateVO> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws
            ApplicationException {
        List<VisitDate> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDate entity : entities) {
            result.add(convertToVisitDateVO(entity));
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
//    public VisitDate getEntityByID(int visitDateID) throws ApplicationException {
//        return dao.findByID(visitDateID);
//    }

        private VisitDateVO convertToVisitDateVO(VisitDate entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "VisitDate":
                    return (VisitDateVO) transformToVisitDateVO((VisitDate) entity);
                case "Visit":
                    return (VisitDateVO) transformToVisitVO((Visit) entity);
                case "Accomodation":
                    return (VisitDateVO) transformToAccomodationVO((Accomodation) entity);
                case "VisitDate":
                    return (VisitDateVO) transformToVisitDateVO((VisitDate) entity);
                case "OperationType":
                    return (VisitDateVO) transformToOperationTypeVO((OperationType) entity);
                case "VisitDate":
                    return (VisitDateVO) transformToVisitDateVO((VisitDate) entity);
                case "VisitDate":
                    return (VisitDateVO) transformToVisitDateVO((VisitDate) entity);

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
            result.setVisitDate(transformToVisitDateVO(visit.getVisitDate()));
            result.setPatient(transformToVisitDateVO(visit.getPatient()));
            result.setStatus(convertVisitDateStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setVisitDate(transformToVisitDateVO(visit.getVisitDate()));
            result.setVisitDate(transformToVisitDateVO(visit.getVisitDate()));
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setNote(visit.getNote());
            result.setInactive(visit.getInactive());
            return result;
        }

        private VisitDateVO transformToVisitDateVO(VisitDate visitDate) {
            if (visitDate == null) return null;
            VisitDateVO result = new VisitDateVO();
            result.setVisitDateId(visitDate.getVisitDateId());
            result.setSurname(visitDate.getSurname());
            result.setFirstName(visitDate.getFirstName());
            result.setSecondName(visitDate.getSecondName());
            result.setSex(convertSex(visitDate.getSex()));
            result.setBirthday(visitDate.getBirthday());
            result.setTelephone(visitDate.getTelephone());
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

        private VisitDateVO transformToVisitDateVO(VisitDate visitDate) {
            if (visitDate == null) return null;
            VisitDateVO result = new VisitDateVO();
            result.setVisitDateId(visitDate.getVisitDateId());
            result.setSurname(visitDate.getSurname());
            result.setFirstName(visitDate.getFirstName());
            result.setSecondName(visitDate.getSecondName());
            result.setSex(convertSex(visitDate.getSex()));
            result.setCityFrom(visitDate.getCityFrom());
            result.setInactive(visitDate.isInactive());
            return result;
        }

        private VisitDateVO transformToVisitDateVO(VisitDate visitDate) {
            if (visitDate == null) return null;
            VisitDateVO result = new VisitDateVO();
            result.setVisitDateId(visitDate.getVisitDateId());
            result.setSurname(visitDate.getSurname());
            result.setFirstName(visitDate.getFirstName());
            result.setSecondName(visitDate.getSecondName());
            result.setSex(convertSex(visitDate.getSex()));
            result.setInactive(visitDate.isInactive());
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

        private VisitDate copyToEntityObject(VisitDateVO visitDateVO, VisitDate entity) throws ApplicationException {
            if (visitDateVO == null) return null;
            switch (visitDateVO.getClass().getSimpleName()) {
                case "VisitDateVO": {
                    if (entity == null) entity = (VisitDate) new VisitDate();
                    return (VisitDate) copyToVisitDate((VisitDateVO) visitDateVO, (VisitDate) entity);
                }
                case "AccomodationVO": {
                    if (entity == null) entity = (VisitDate) new Accomodation();
                    return (VisitDate) copyToAccomodation((AccomodationVO) visitDateVO, (Accomodation) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (VisitDate) new VisitDate();
                    return (VisitDate) copyToVisitDate((VisitDateVO) visitDateVO, (VisitDate) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (VisitDate) new OperationType();
                    return (VisitDate) copyToOperationType((OperationTypeVO) visitDateVO, (OperationType) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (VisitDate) new VisitDate();
                    return (VisitDate) copyToVisitDate((VisitDateVO) visitDateVO, (VisitDate) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (VisitDate) new Visit();
                    return (VisitDate) copyToVisit((VisitVO) visitDateVO, (Visit) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (VisitDate) new VisitDate();
                    return (VisitDate) copyToVisitDate((VisitDateVO) visitDateVO, (VisitDate) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertVisitDateStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getPatient() != null && original.getPatient().getVisitDateId() > 0) {
                    VisitDate patient = new VisitDate();
                    patient.setVisitDateId(original.getPatient().getVisitDateId());
                    result.setPatient(copyToVisitDate(original.getPatient(), patient));
                } else result.setPatient(null);

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);


                if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                    OperationType operationType = new OperationType();
                    operationType.setOperationTypeId(original.getOperationType().getOperationTypeId());
                    result.setOperationType(copyToOperationType(original.getOperationType(), operationType));
                } else result.setOperationType(null);

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

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

        private VisitDate copyToVisitDate(VisitDateVO original, VisitDate result) {
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

        private VisitDate copyToVisitDate(VisitDateVO original, VisitDate result) {
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

        private VisitDate copyToVisitDate(VisitDateVO original, VisitDate result) {
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

        private VisitDateStatus convertVisitDateStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return VisitDateStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return VisitDateStatus.RELATIVE;
            }
        }

        private String convertVisitDateStatus(VisitDateStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(VisitDate entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "VisitDate": {
//                Map<String, Object> parametersVisitDate = new HashMap<>();
//                parametersVisitDate.put("visitDate", (VisitDate) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (VisitDate) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.visitDate = :visitDate", parametersVisitDate) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<VisitDate>) Visit.class).size() != 0);
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<VisitDate>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<VisitDate>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<VisitDate>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<VisitDate>) Visit.class).size() != 0;
//            }
//            case "Accomodation": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("accomodation", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByAccomodation", parameters, (Class<VisitDate>) Visit.class).size() != 0;
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