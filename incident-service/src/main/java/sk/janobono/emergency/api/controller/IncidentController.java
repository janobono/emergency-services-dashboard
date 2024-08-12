package sk.janobono.emergency.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.janobono.emergency.api.model.IncidentChangeWebDto;
import sk.janobono.emergency.api.model.IncidentWebDto;
import sk.janobono.emergency.api.model.SingleValueBodyWebDto;
import sk.janobono.emergency.api.service.IncidentApiService;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/incidents")
public class IncidentController {

    private final IncidentApiService incidentApiService;

    @Operation(parameters = {
            @Parameter(in = ParameterIn.QUERY, name = "page", content = @Content(schema = @Schema(type = "integer"))),
            @Parameter(in = ParameterIn.QUERY, name = "size", content = @Content(schema = @Schema(type = "integer"))),
            @Parameter(in = ParameterIn.QUERY, name = "sort",
                    content = @Content(array = @ArraySchema(schema = @Schema(type = "string")))
            )
    })
    @GetMapping
    public Page<IncidentWebDto> getIncidents(
            @RequestParam(value = "searchField", required = false) final String searchField,
            @RequestParam(value = "type", required = false) final IncidentType type,
            @RequestParam(value = "level", required = false) final IncidentLevel level,
            @RequestParam(value = "status", required = false) final IncidentStatus status,
            @RequestParam(value = "createdFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime createdFrom,
            @RequestParam(value = "createdTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime createdTo,
            @RequestParam(value = "latitudeFrom", required = false) final BigDecimal latitudeFrom,
            @RequestParam(value = "latitudeTo", required = false) final BigDecimal latitudeTo,
            @RequestParam(value = "longitudeFrom", required = false) final BigDecimal longitudeFrom,
            @RequestParam(value = "longitudeTo", required = false) final BigDecimal longitudeTo,
            final Pageable pageable
    ) {
        log.debug("getIncidents({},{},{},{},{},{},{},{},{},{},{})",
                searchField,
                type,
                level,
                status,
                createdFrom,
                createdTo,
                latitudeFrom,
                latitudeTo,
                longitudeFrom,
                longitudeTo,
                pageable);

        return incidentApiService.getIncidents(
                searchField,
                type,
                level,
                status,
                createdFrom,
                createdTo,
                latitudeFrom,
                latitudeTo,
                longitudeFrom,
                longitudeTo,
                pageable
        );
    }

    @GetMapping("/{id}")
    public IncidentWebDto getIncident(@PathVariable("id") final long id) {
        log.debug("getIncident({})", id);
        return incidentApiService.getIncident(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentWebDto addIncident(@Valid @RequestBody final IncidentChangeWebDto incidentData) {
        log.debug("addIncident({})", incidentData);
        return incidentApiService.addIncident(incidentData);
    }

    @PutMapping("/{id}")
    public IncidentWebDto setIncident(@PathVariable("id") final long id, @Valid @RequestBody final IncidentChangeWebDto incidentData) {
        log.debug("setIncident({},{})", id, incidentData);
        return incidentApiService.setIncident(id, incidentData);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setIncidentStatus(@PathVariable("id") final long id, @Valid @RequestBody final SingleValueBodyWebDto<IncidentStatus> status) {
        log.debug("setIncidentStatus({},{})", id, status);
        incidentApiService.setIncidentStatus(id, status.value());
    }

    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable("id") final long id) {
        log.debug("deleteIncident({})", id);
        incidentApiService.deleteIncident(id);
    }
}
