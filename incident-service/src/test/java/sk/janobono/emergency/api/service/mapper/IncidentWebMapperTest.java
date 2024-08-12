package sk.janobono.emergency.api.service.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sk.janobono.emergency.api.model.IncidentChangeWebDto;
import sk.janobono.emergency.api.model.IncidentWebDto;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;
import sk.janobono.emergency.service.model.IncidentChangeData;
import sk.janobono.emergency.service.model.IncidentData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IncidentWebMapperImpl.class})
class IncidentWebMapperTest {

    @Autowired
    public IncidentWebMapper incidentWebMapper;

    @Test
    void mapToWebDto_whenValidData_thenValidResult() {
        // GIVEN
        final IncidentData incidentData = new IncidentData(
                1L,
                LocalDateTime.now(),
                null,
                IncidentType.FIRE,
                IncidentLevel.LOW.getLevel(),
                IncidentStatus.NEW,
                BigDecimal.valueOf(48.58253943979857),
                BigDecimal.valueOf(19.110418881370656),
                "Test incident",
                "Kitchen on fire"
        );

        // WHEN
        final IncidentWebDto incidentWebDto = incidentWebMapper.mapToWebDto(incidentData);

        // THEN
        assertThat(incidentData.id()).isEqualTo(incidentWebDto.id());
        assertThat(incidentData.created()).isEqualTo(incidentWebDto.created());
        assertThat(incidentData.modified()).isEqualTo(incidentWebDto.modified());
        assertThat(incidentData.type()).isEqualTo(incidentWebDto.type());
        assertThat(incidentData.level()).isEqualTo(incidentWebDto.level().getLevel());
        assertThat(incidentData.status()).isEqualTo(incidentWebDto.status());
        assertThat(incidentData.latitude()).isEqualTo(incidentWebDto.latitude());
        assertThat(incidentData.longitude()).isEqualTo(incidentWebDto.longitude());
        assertThat(incidentData.title()).isEqualTo(incidentWebDto.title());
        assertThat(incidentData.description()).isEqualTo(incidentWebDto.description());
    }

    @Test
    void mapToData_whenValidData_thenValidResult() {
        // GIVEN
        final IncidentChangeWebDto incidentChangeWebDto = new IncidentChangeWebDto(
                IncidentType.FIRE,
                IncidentLevel.LOW,
                BigDecimal.valueOf(48.58253943979857),
                BigDecimal.valueOf(19.110418881370656),
                "Test incident",
                "Kitchen on fire"
        );

        // WHEN
        final IncidentChangeData incidentChangeData = incidentWebMapper.mapToData(incidentChangeWebDto);

        // THEN
        assertThat(incidentChangeWebDto.type()).isEqualTo(incidentChangeData.type());
        assertThat(incidentChangeWebDto.level().getLevel()).isEqualTo(incidentChangeData.level());
        assertThat(incidentChangeWebDto.latitude()).isEqualTo(incidentChangeData.latitude());
        assertThat(incidentChangeWebDto.longitude()).isEqualTo(incidentChangeData.longitude());
        assertThat(incidentChangeWebDto.title()).isEqualTo(incidentChangeData.title());
        assertThat(incidentChangeWebDto.description()).isEqualTo(incidentChangeData.description());
    }
}
