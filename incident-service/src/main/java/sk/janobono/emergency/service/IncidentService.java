package sk.janobono.emergency.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.util.ScDf;
import sk.janobono.emergency.dal.domain.IncidentDo;
import sk.janobono.emergency.dal.repository.IncidentRepository;
import sk.janobono.emergency.dal.specification.IncidentSpecification;
import sk.janobono.emergency.service.mapper.IncidentDataMapper;
import sk.janobono.emergency.service.model.IncidentChangeData;
import sk.janobono.emergency.service.model.IncidentData;
import sk.janobono.emergency.service.model.IncidentSearchCriteriaData;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class IncidentService {

    private final ScDf scDf;

    private final IncidentDataMapper incidentDataMapper;

    private final IncidentRepository incidentRepository;

    public Page<IncidentData> getIncidents(final IncidentSearchCriteriaData incidentSearchCriteriaData, final Pageable pageable) {
        log.debug("getIncidents({},{})", incidentSearchCriteriaData, pageable);

        final IncidentSearchCriteriaData criteria = Optional.ofNullable(incidentSearchCriteriaData)
                .orElseGet(() -> IncidentSearchCriteriaData.builder().build());

        return incidentRepository.findAll(
                IncidentSpecification.builder()
                        .scDf(scDf)
                        .searchField(criteria.searchField())
                        .type(criteria.type())
                        .level(criteria.level())
                        .status(criteria.status())
                        .createdFrom(criteria.createdFrom())
                        .createdTo(criteria.createdTo())
                        .latitudeFrom(criteria.latitudeFrom())
                        .latitudeTo(criteria.latitudeTo())
                        .longitudeFrom(criteria.longitudeFrom())
                        .longitudeTo(criteria.longitudeTo())
                        .build(),
                Optional.ofNullable(pageable).orElseGet(() -> Pageable.unpaged(Sort.by(Sort.Direction.ASC, "created")))
        ).map(incidentDataMapper::mapToData);
    }

    public IncidentData getIncident(final long id) {
        log.debug("getIncident({})", id);
        return incidentDataMapper.mapToData(getIncidentDo(id));
    }

    @Transactional
    public IncidentData addIncident(final IncidentChangeData incidentChangeData) {
        log.debug("addIncident({})", incidentChangeData);

        final IncidentDo incidentDo = new IncidentDo();
        incidentDo.setCreated(LocalDateTime.now());
        incidentDo.setType(incidentChangeData.type());
        incidentDo.setLevel(incidentChangeData.level());
        incidentDo.setStatus(IncidentStatus.NEW);
        incidentDo.setLatitude(incidentChangeData.latitude());
        incidentDo.setLongitude(incidentChangeData.longitude());
        incidentDo.setTitle(incidentChangeData.title());
        incidentDo.setDescription(incidentChangeData.description());

        return incidentDataMapper.mapToData(incidentRepository.save(incidentDo));
    }

    @Transactional
    public IncidentData setIncident(final long id, final IncidentChangeData incidentChangeData) {
        log.debug("setIncident({},{})", id, incidentChangeData);

        final IncidentDo incidentDo = getIncidentDo(id);
        incidentDo.setModified(LocalDateTime.now());
        incidentDo.setType(incidentChangeData.type());
        incidentDo.setLevel(incidentChangeData.level());
        incidentDo.setLatitude(incidentChangeData.latitude());
        incidentDo.setLongitude(incidentChangeData.longitude());
        incidentDo.setTitle(incidentChangeData.title());
        incidentDo.setDescription(incidentChangeData.description());

        return incidentDataMapper.mapToData(incidentRepository.save(incidentDo));
    }

    @Transactional
    public void setIncidentStatus(final long id, @NotNull final IncidentStatus value) {
        log.debug("setIncidentStatus({},{})", id, value);
        final IncidentDo incidentDo = getIncidentDo(id);
        incidentDo.setModified(LocalDateTime.now());
        incidentDo.setStatus(value);
        incidentRepository.save(incidentDo);
    }

    @Transactional
    public void deleteIncident(final long id) {
        log.debug("deleteIncident({})", id);
        if (!incidentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found [id=%d]".formatted(id));
        }
        incidentRepository.deleteById(id);
    }

    private IncidentDo getIncidentDo(final long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incident not found [id=%d]".formatted(id)));
    }
}
