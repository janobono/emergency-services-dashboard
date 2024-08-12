package sk.janobono.emergency.api.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotNull;

@JsonRootName("SingleValueBody")
public record SingleValueBodyWebDto<T>(@NotNull T value) {
}
