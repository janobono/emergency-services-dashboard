package sk.janobono.emergency.api.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.common.model.IncidentType;

import java.math.BigDecimal;

@JsonRootName("IncidentData")
public record IncidentChangeWebDto(
        @NotNull IncidentType type,
        @NotNull IncidentLevel level,
        @NotNull @Min(-90) @Max(90) BigDecimal latitude,
        @NotNull @Min(-180) @Max(180) BigDecimal longitude,
        @NotBlank String title,
        @NotBlank String description
) {
}
