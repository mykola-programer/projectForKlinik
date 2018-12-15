package ua.nike.project.spring.service.transformation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.vo.AccomodationVO;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceTransformationAccomodationImpl implements ServiceTransformation<AccomodationVO, Accomodation> {

    @Override
    public AccomodationVO convertToVisualObject(Accomodation accomodation) {
        if (accomodation == null) return null;
        AccomodationVO result = new AccomodationVO();
        result.setAccomodationId(accomodation.getAccomodationId());
        result.setWard(Integer.valueOf(accomodation.getWard().toString().substring(1)));
        result.setWardPlace(accomodation.getWardPlace());
        result.setInactive(accomodation.isInactive());
        return result;
    }

    @Override
    public Accomodation copyToEntityObject(AccomodationVO original, Accomodation result) {
        if (original != null) {
            result.setWard(Ward.valueOf("N" + original.getWard()));
            result.setWardPlace(original.getWardPlace());
            result.setInactive(original.isInactive());
        }
        return result;
    }

    @Override
    public boolean isRelated(Accomodation entity){
        return !(entity != null && entity.getVisits().size() == 0);
    }
}
