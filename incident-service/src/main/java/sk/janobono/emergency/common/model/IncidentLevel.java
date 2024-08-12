package sk.janobono.emergency.common.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum IncidentLevel {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    final int level;

    IncidentLevel(final int level) {
        this.level = level;
    }

    public static IncidentLevel byLevel(final int level) {
        return Stream.of(IncidentLevel.values())
                .filter(item -> item.getLevel() == level)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported level: %d".formatted(level)));
    }
}
