package sk.janobono.emergency.api.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import sk.janobono.emergency.api.model.IncidentChangeWebDto;
import sk.janobono.emergency.api.model.IncidentWebDto;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.service.model.IncidentChangeData;
import sk.janobono.emergency.service.model.IncidentData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
imports = {IncidentLevel.class})
public interface IncidentWebMapper {

    @Mapping(expression = "java(IncidentLevel.byLevel(incidentData.level()))", target = "level")
    IncidentWebDto mapToWebDto(IncidentData incidentData);

    @Mapping(expression = "java(incidentChange.level().getLevel())", target = "level")
    IncidentChangeData mapToData(IncidentChangeWebDto incidentChange);
}
