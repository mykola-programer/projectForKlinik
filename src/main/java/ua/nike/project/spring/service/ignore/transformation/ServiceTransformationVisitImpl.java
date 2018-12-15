package ua.nike.project.spring.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.*;
import ua.nike.project.hibernate.type.ClientStatus;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.*;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceTransformationVisitImpl implements ServiceTransformation<VisitVO, Visit> {

    @Autowired
    private ServiceTransformation<VisitDateVO, VisitDate> visitDateServiceTransformation;

    @Autowired
    private ServiceTransformation<ClientVO, Client> clientServiceTransformation;

    @Autowired
    private ServiceTransformation<OperationTypeVO, OperationType> operationTypeServiceTransformation;

    @Autowired
    private ServiceTransformation<SurgeonVO, Surgeon> surgeonServiceTransformation;

    @Autowired
    private ServiceTransformation<ManagerVO, Manager> managerServiceTransformation;

    @Autowired
    private ServiceTransformation<AccomodationVO, Accomodation> accomodationServiceTransformation;

    @Override
    public VisitVO convertToVisualObject(Visit visit) throws ApplicationException {
        if (visit == null) return null;

        VisitVO result = new VisitVO();
        result.setVisitId(visit.getVisitId());
        result.setVisitDate(visitDateServiceTransformation.convertToVisualObject(visit.getVisitDate()));
        result.setTimeForCome(visit.getTimeForCome());
        result.setOrderForCome(visit.getOrderForCome());
        result.setClient(clientServiceTransformation.convertToVisualObject(visit.getClient()));
        result.setPatient(clientServiceTransformation.convertToVisualObject(visit.getPatient()));
        result.setStatus(convertClientStatus(visit.getStatus()));
        result.setOperationType(operationTypeServiceTransformation.convertToVisualObject(visit.getOperationType()));
        result.setEye(visit.getEye());
        result.setSurgeon(surgeonServiceTransformation.convertToVisualObject(visit.getSurgeon()));
        result.setManager(managerServiceTransformation.convertToVisualObject(visit.getManager()));
        result.setAccomodation(accomodationServiceTransformation.convertToVisualObject(visit.getAccomodation()));
        result.setNote(visit.getNote());
        result.setInactive(visit.getInactive());
        return result;
    }

    @Override
    public Visit copyToEntityObject(VisitVO original, Visit result) throws ApplicationException {
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
                result.setVisitDate(visitDateServiceTransformation.copyToEntityObject(original.getVisitDate(), visitDate));
            } else result.setVisitDate(null);

            if (original.getPatient() != null && original.getPatient().getClientId() > 0) {
                Client patient = new Client();
                patient.setClientId(original.getPatient().getClientId());
                result.setPatient(clientServiceTransformation.copyToEntityObject(original.getPatient(), patient));
            } else result.setPatient(null);

            if (original.getClient() != null && original.getClient().getClientId() > 0) {
                Client client = new Client();
                client.setClientId(original.getClient().getClientId());
                result.setClient(clientServiceTransformation.copyToEntityObject(original.getClient(), client));
            } else result.setClient(null);


            if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                OperationType operationType = new OperationType();
                operationType.setOperationTypeId(original.getOperationType().getOperationTypeId());
                result.setOperationType(operationTypeServiceTransformation.copyToEntityObject(original.getOperationType(), operationType));
            } else result.setOperationType(null);

            if (original.getSurgeon() != null && original.getSurgeon().getSurgeonId() > 0) {
                Surgeon surgeon = new Surgeon();
                surgeon.setSurgeonId(original.getSurgeon().getSurgeonId());
                result.setSurgeon(surgeonServiceTransformation.copyToEntityObject(original.getSurgeon(), surgeon));
            } else result.setSurgeon(null);

            if (original.getManager() != null && original.getManager().getManagerId() > 0) {
                Manager manager = new Manager();
                manager.setManagerId(original.getManager().getManagerId());
                result.setManager(managerServiceTransformation.copyToEntityObject(original.getManager(), manager));
            } else result.setManager(null);

            if (original.getAccomodation() != null && original.getAccomodation().getAccomodationId() > 0) {
                Accomodation accomodation = new Accomodation();
                accomodation.setAccomodationId(original.getAccomodation().getAccomodationId());
                result.setAccomodation(accomodationServiceTransformation.copyToEntityObject(original.getAccomodation(), accomodation));
            } else result.setAccomodation(null);

        }
        return result;
    }

    @Override
    public boolean isRelated(Visit entity) {
        return (entity == null);
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

}
