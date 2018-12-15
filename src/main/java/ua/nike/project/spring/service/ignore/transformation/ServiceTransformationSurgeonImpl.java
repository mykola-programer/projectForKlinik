package ua.nike.project.spring.service.transformation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.vo.SurgeonVO;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceTransformationSurgeonImpl implements ServiceTransformation<SurgeonVO, Surgeon> {

    @Override
    public SurgeonVO convertToVisualObject(Surgeon surgeon) {
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

    @Override
    public Surgeon copyToEntityObject(SurgeonVO original, Surgeon result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            result.setSex(convertSex(original.getSex()));
            result.setInactive(original.isInactive());
        }
        return result;
    }

    @Override
    public boolean isRelated(Surgeon entity) {
        return !(entity != null && entity.getVisits().size() == 0);
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

}
