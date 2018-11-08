package ua.nike.project.spring.controller;

import ua.nike.project.spring.exceptions.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

final class ControllerValidation {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    static void validate(Object object) throws BusinessException {

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object);

        if (constraintViolations.size() != 0) {
            final StringBuilder sb = new StringBuilder();

            switch (object.getClass().getSimpleName()) {
                case "ClientVO":
                    sb.append("Кліент має недопустимі значення : \n");
                    break;
                case "AccomodationVO":
                    sb.append("Поселення має недопустимі значення : \n");
                    break;
                case "ManagerVO":
                    sb.append("Менеджер має недопустимі значення : \n");
                    break;
                case "OperationTypeVO":
                    sb.append("Вид операції має недопустимі значення : \n");
                    break;
                case "SurgeonVO":
                    sb.append("Хірург має недопустимі значення : \n");
                    break;
                case "VisitDateVO":
                    sb.append("Дата приходу має недопустимі значення : \n");
                    break;
                case "VisitVO":
                    sb.append("Візит має недопустимі значення : \n");
                    break;
                default:
                    sb.append(object.getClass().getSimpleName()).append(" have invalid value : \n");
            }
            sb.append("Кіл-ть помилок: ").append(constraintViolations.size()).append(" \n");
            for (ConstraintViolation<Object> cv : constraintViolations) {
                sb.append("Помилка: ").append(cv.getMessage()).append(" \n");
            }
            sb.append(object).append(" \n");
            throw new BusinessException(sb.toString());
        }
    }
}
