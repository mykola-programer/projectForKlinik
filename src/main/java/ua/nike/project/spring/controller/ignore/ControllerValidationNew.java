package ua.nike.project.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceValidationMassage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Controller
final class ControllerValidation {

    private final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    ServiceValidationMassage serviceValidationMassage;

    void validate(Object object) throws BusinessException {

        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object);

        if (constraintViolations.size() != 0) {
            final StringBuilder sb = new StringBuilder();

            switch (object.getClass().getSimpleName()) {
                case "ClientVO":
                    sb.append(serviceValidationMassage.value("client.massage") + " \n");
                    break;
                case "AccomodationVO":
                    sb.append(serviceValidationMassage.value("accomodation.massage") + " \n");
                    break;
                case "ManagerVO":
                    sb.append(serviceValidationMassage.value("manager.massage") + " \n");
                    break;
                case "OperationTypeVO":
                    sb.append(serviceValidationMassage.value("operation_type.massage") + " \n");
                    break;
                case "SurgeonVO":
                    sb.append(serviceValidationMassage.value("surgeon.massage") + " \n");
                    break;
                case "VisitDateVO":
                    sb.append(serviceValidationMassage.value("visit_date.massage") + " \n");
                    break;
                case "VisitVO":
                    sb.append(serviceValidationMassage.value("visit.massage") + " \n");
                    break;
                default:
                    sb.append(object.getClass().getSimpleName()).append(serviceValidationMassage.value("object.mistake") + " \n");
            }
            sb.append(serviceValidationMassage.value("count.mistakes")).append(constraintViolations.size()).append(" \n");
            for (ConstraintViolation<Object> cv : constraintViolations) {
                sb.append(serviceValidationMassage.value("mistake"))
                        .append(serviceValidationMassage.value(cv.getMessage()))
                        .append(" \n");
            }
            sb.append(object).append(" \n");
            throw new BusinessException(sb.toString());
        }
    }
}
