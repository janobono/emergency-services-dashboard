package sk.janobono.emergency.service.model;

import lombok.Builder;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record IncidentSearchCriteriaData(
        String searchField,
        IncidentType type,
        Integer level,
        IncidentStatus status,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        BigDecimal latitudeFrom,
        BigDecimal latitudeTo,
        BigDecimal longitudeFrom,
        BigDecimal longitudeTo
) {
}
