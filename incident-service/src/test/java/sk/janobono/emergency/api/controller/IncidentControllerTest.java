package sk.janobono.emergency.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.LinkedMultiValueMap;
import sk.janobono.emergency.api.model.IncidentWebDto;
import sk.janobono.emergency.common.model.IncidentLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentControllerTest extends BaseControllerTest {

    @Test
    void getIncidents_whenNotQueryParameters_thenThisResult() {
        // GIVEN
        createTestIncidents();

        // WHEN
        final JsonNode jsonNode = getRestClient().get()
                .uri(getURI("/incidents"))
                .retrieve()
                .body(JsonNode.class);
        final Page<IncidentWebDto> page = getPage(jsonNode, Pageable.unpaged(), IncidentWebDto.class);

        // THEN
        assertThat(page).isNotEmpty();
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getPageable().isUnpaged()).isTrue();
    }

    @Test
    void getIncidents_whenCriteriaAndPageable_thenThisResult() {
        // GIVEN
        createTestIncidents();

        // WHEN
        final Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "level");
        final LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("createdFrom", LocalDateTime.of(YEAR, MONTH, DAY, 0, 0).minusDays(10).format(DateTimeFormatter.ISO_DATE_TIME));
        params.add("createdTo", LocalDateTime.of(YEAR, MONTH, DAY, 0, 0).plusDays(10).format(DateTimeFormatter.ISO_DATE_TIME));
        params.add("latitudeFrom", "-90");
        params.add("latitudeTo", "90");
        params.add("longitudeFrom", "-180");
        params.add("longitudeTo", "180");
        addPageableToParams(params, pageable);

        final JsonNode jsonNode = getRestClient().get()
                .uri(getURI("/incidents", params))
                .retrieve()
                .body(JsonNode.class);
        final Page<IncidentWebDto> page = getPage(jsonNode, pageable, IncidentWebDto.class);

        // THEN
        assertThat(page).isNotEmpty();
        assertThat(page.getTotalPages()).isEqualTo(6);
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getPageable().isUnpaged()).isFalse();
        assertThat(page.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(page.getContent().getFirst().level()).isEqualTo(IncidentLevel.HIGH);
    }
}
