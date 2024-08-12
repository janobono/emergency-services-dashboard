package sk.janobono.emergency.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import sk.janobono.emergency.dal.domain.IncidentDo;
import sk.janobono.emergency.service.model.IncidentData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface IncidentDataMapper {

    IncidentData mapToData(IncidentDo incidentDo);
}
