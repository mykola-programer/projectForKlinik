package ua.nike.project.spring.service.transformation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.vo.ManagerVO;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceTransformationManagerImpl implements ServiceTransformation<ManagerVO, Manager> {

    @Override
    public ManagerVO convertToVisualObject(Manager manager) {
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

    @Override
    public Manager copyToEntityObject(ManagerVO original, Manager result) {
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

    @Override
    public boolean isRelated(Manager entity) {
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
