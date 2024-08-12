package sk.janobono.emergency.api.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.format.annotation.DateTimeFormat;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonRootName("Incident")
public record IncidentWebDto(
        Long id,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime created,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime modified,
        IncidentType type,
        IncidentLevel level,
        IncidentStatus status,
        BigDecimal latitude,
        BigDecimal longitude,
        String title,
        String description
) {
}
