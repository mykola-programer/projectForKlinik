package ua.nike.project.spring.service.transformation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.VisitDate;
import ua.nike.project.spring.vo.VisitDateVO;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceTransformationVisitDateImpl implements ServiceTransformation<VisitDateVO, VisitDate> {

    @Override
    public VisitDateVO convertToVisualObject(VisitDate visitDate) {
        if (visitDate == null) return null;
        VisitDateVO result = new VisitDateVO();
        result.setVisitDateId(visitDate.getVisitDateId());
        result.setDate(visitDate.getDate());
        result.setInactive(visitDate.isInactive());
        return result;
    }

    @Override
    public VisitDate copyToEntityObject(VisitDateVO original, VisitDate result) {
        if (original != null) {
            result.setDate(original.getDate());
            result.setInactive(original.isInactive());
        }
        return result;
    }

    @Override
    public boolean isRelated(VisitDate entity) {
        return !(entity != null && entity.getVisits().size() == 0);
    }
}
