package sk.janobono.emergency.api.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.janobono.emergency.api.model.IncidentChangeWebDto;
import sk.janobono.emergency.api.model.IncidentWebDto;
import sk.janobono.emergency.api.service.mapper.IncidentWebMapper;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;
import sk.janobono.emergency.service.IncidentService;
import sk.janobono.emergency.service.model.IncidentSearchCriteriaData;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IncidentApiService {

    private final IncidentWebMapper incidentWebMapper;

    private final IncidentService incidentService;

    public Page<IncidentWebDto> getIncidents(final String searchField,
                                             final IncidentType type,
                                             final IncidentLevel level,
                                             final IncidentStatus status,
                                             final LocalDateTime createdFrom,
                                             final LocalDateTime createdTo,
                                             final BigDecimal latitudeFrom,
                                             final BigDecimal latitudeTo,
                                             final BigDecimal longitudeFrom,
                                             final BigDecimal longitudeTo,
                                             final Pageable pageable) {
        final IncidentSearchCriteriaData criteria = IncidentSearchCriteriaData.builder()
                .searchField(searchField)
                .type(type)
                .level(Optional.ofNullable(level).map(IncidentLevel::getLevel).orElse(null))
                .status(status)
                .createdFrom(createdFrom)
                .createdTo(createdTo)
                .latitudeFrom(latitudeFrom)
                .latitudeTo(latitudeTo)
                .longitudeFrom(longitudeFrom)
                .longitudeTo(longitudeTo)
                .build();

        return incidentService.getIncidents(criteria, pageable)
                .map(incidentWebMapper::mapToWebDto);
    }

    public IncidentWebDto getIncident(final long id) {
        return incidentWebMapper.mapToWebDto(incidentService.getIncident(id));
    }

    @Transactional
    public IncidentWebDto addIncident(@Valid final IncidentChangeWebDto incidentChange) {
        return incidentWebMapper.mapToWebDto(incidentService.addIncident(
                incidentWebMapper.mapToData(incidentChange)
        ));
    }

    @Transactional
    public IncidentWebDto setIncident(final long id, @Valid final IncidentChangeWebDto incidentChange) {
        return incidentWebMapper.mapToWebDto(incidentService.setIncident(
                id,
                incidentWebMapper.mapToData(incidentChange)
        ));
    }

    @Transactional
    public void setIncidentStatus(final long id, @NotNull final IncidentStatus value) {
        incidentService.setIncidentStatus(id, value);
    }

    @Transactional
    public void deleteIncident(final long id) {
        incidentService.deleteIncident(id);
    }
}
