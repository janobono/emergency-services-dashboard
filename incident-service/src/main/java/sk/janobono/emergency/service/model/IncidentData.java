package sk.janobono.emergency.service.model;

import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IncidentData(
        Long id,
        LocalDateTime created,
        LocalDateTime modified,
        IncidentType type,
        Integer level,
        IncidentStatus status,
        BigDecimal latitude,
        BigDecimal longitude,
        String title,
        String description
) {
}
