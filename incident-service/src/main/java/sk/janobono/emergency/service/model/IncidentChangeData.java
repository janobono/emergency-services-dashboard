package sk.janobono.emergency.service.model;

import sk.janobono.emergency.common.model.IncidentType;

import java.math.BigDecimal;

public record IncidentChangeData(
        IncidentType type,
        Integer level,
        BigDecimal latitude,
        BigDecimal longitude,
        String title,
        String description
) {
}
