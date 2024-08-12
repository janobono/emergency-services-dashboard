package sk.janobono.emergency.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import sk.janobono.emergency.BaseTest;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;
import sk.janobono.emergency.service.model.IncidentChangeData;
import sk.janobono.emergency.service.model.IncidentData;
import sk.janobono.emergency.service.model.IncidentSearchCriteriaData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentServiceTest extends BaseTest {

    @Autowired
    public IncidentService incidentService;

    @Test
    void getIncidents_whenNullParameters_thenThisResult() {
        // GIVEN
        createTestIncidents();

        // WHEN
        final Page<IncidentData> page = incidentService.getIncidents(null, null);

        // THEN
        assertThat(page).isNotEmpty();
        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getPageable().isUnpaged()).isTrue();
    }

    @Test
    void getIncidents_whenCriteriaAndPageable_thenThisResult() {
        // GIVEN
        createTestIncidents();

        // WHEN
        final Page<IncidentData> page = incidentService.getIncidents(
                IncidentSearchCriteriaData.builder()
                        .createdFrom(LocalDateTime.of(YEAR, MONTH, DAY, 0, 0).minusDays(10))
                        .createdTo(LocalDateTime.of(YEAR, MONTH, DAY, 0, 0).plusDays(10))
                        .latitudeFrom(BigDecimal.valueOf(-90))
                        .latitudeTo(BigDecimal.valueOf(90))
                        .longitudeFrom(BigDecimal.valueOf(-180))
                        .longitudeTo(BigDecimal.valueOf(180))
                        .build(),
                PageRequest.of(0, 5, Sort.Direction.DESC, "level")
        );

        // THEN
        assertThat(page).isNotEmpty();
        assertThat(page.getTotalPages()).isEqualTo(6);
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getPageable().isUnpaged()).isFalse();
        assertThat(page.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(page.getContent().getFirst().level()).isEqualTo(IncidentLevel.HIGH.getLevel());
    }

    @Test
    void getIncident_whenNotFound_thenRaisedException() {
        // WHEN
        final ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> incidentService.getIncident(1L));

        // THEN
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void crudTest_whenAllCrudOps_thenThisResults() {
        // ADD
        final IncidentData newIncident = incidentService.addIncident(new IncidentChangeData(IncidentType.CRIME, IncidentLevel.LOW.getLevel(), BigDecimal.ZERO, BigDecimal.ZERO, "title", "description"));
        assertThat(newIncident).isNotNull();
        assertThat(newIncident.id()).isNotNull();
        assertThat(newIncident.created()).isNotNull();
        assertThat(newIncident.modified()).isNull();
        assertThat(newIncident.type()).isEqualTo(IncidentType.CRIME);
        assertThat(newIncident.level()).isEqualTo(IncidentLevel.LOW.getLevel());
        assertThat(newIncident.status()).isEqualTo(IncidentStatus.NEW);
        assertThat(newIncident.latitude()).isEqualTo(BigDecimal.ZERO);
        assertThat(newIncident.longitude()).isEqualTo(BigDecimal.ZERO);
        assertThat(newIncident.title()).isEqualTo("title");
        assertThat(newIncident.description()).isEqualTo("description");

        // SET
        final IncidentData modifiedIncident = incidentService.setIncident(newIncident.id(), new IncidentChangeData(IncidentType.FIRE, IncidentLevel.MEDIUM.getLevel(), BigDecimal.ONE, BigDecimal.ONE, "title2", "description2"));
        assertThat(modifiedIncident).isNotNull();
        assertThat(modifiedIncident.id()).isNotNull();
        assertThat(modifiedIncident.created()).isNotNull();
        assertThat(modifiedIncident.modified()).isNotNull();
        assertThat(modifiedIncident.type()).isEqualTo(IncidentType.FIRE);
        assertThat(modifiedIncident.level()).isEqualTo(IncidentLevel.MEDIUM.getLevel());
        assertThat(modifiedIncident.status()).isEqualTo(IncidentStatus.NEW);
        assertThat(modifiedIncident.latitude()).isEqualTo(BigDecimal.ONE);
        assertThat(modifiedIncident.longitude()).isEqualTo(BigDecimal.ONE);
        assertThat(modifiedIncident.title()).isEqualTo("title2");
        assertThat(modifiedIncident.description()).isEqualTo("description2");

        // SET STATUS and GET INCIDENT
        incidentService.setIncidentStatus(modifiedIncident.id(), IncidentStatus.FINISHED);
        final IncidentData savedIncident = incidentService.getIncident(modifiedIncident.id());
        assertThat(savedIncident.status()).isEqualTo(IncidentStatus.FINISHED);

        // DELETE
        incidentService.deleteIncident(savedIncident.id());
        final ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> incidentService.getIncident(savedIncident.id()));
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
