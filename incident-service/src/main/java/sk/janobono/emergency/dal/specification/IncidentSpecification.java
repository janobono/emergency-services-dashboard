package sk.janobono.emergency.dal.specification;

import jakarta.persistence.criteria.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;
import sk.janobono.emergency.common.util.ScDf;
import sk.janobono.emergency.dal.domain.IncidentDo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Builder
public record IncidentSpecification(
        ScDf scDf,
        String searchField,
        IncidentType type,
        Integer level,
        IncidentStatus status,
        LocalDateTime createdFrom,
        LocalDateTime createdTo,
        BigDecimal latitudeFrom,
        BigDecimal latitudeTo,
        BigDecimal longitudeFrom,
        BigDecimal longitudeTo
) implements Specification<IncidentDo> {

    @Override
    public Predicate toPredicate(final Root<IncidentDo> root, final CriteriaQuery<?> criteriaQuery, final CriteriaBuilder criteriaBuilder) {
        if (Optional.ofNullable(searchField).filter(s -> !s.isBlank()).isEmpty()
                && Optional.ofNullable(type).isEmpty()
                && Optional.ofNullable(level).isEmpty()
                && Optional.ofNullable(status).isEmpty()
                && Optional.ofNullable(createdFrom).isEmpty()
                && Optional.ofNullable(createdTo).isEmpty()
                && Optional.ofNullable(latitudeFrom).isEmpty()
                && Optional.ofNullable(latitudeTo).isEmpty()
                && Optional.ofNullable(longitudeFrom).isEmpty()
                && Optional.ofNullable(longitudeTo).isEmpty()
        ) {
            log.debug("Empty criteria.");
            return criteriaQuery.getRestriction();
        }

        final List<Predicate> predicates = new ArrayList<>();
        // search field
        if (Optional.ofNullable(searchField).filter(s -> !s.isBlank()).isPresent()) {
            predicates.add(searchFieldToPredicate(root, criteriaBuilder));
        }
        // type
        if (Optional.ofNullable(type).isPresent()) {
            predicates.add(criteriaBuilder.equal(root.get("type"), type));
        }
        // level
        if (Optional.ofNullable(level).isPresent()) {
            predicates.add(criteriaBuilder.equal(root.get("level"), level));
        }
        // status
        if (Optional.ofNullable(status).isPresent()) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }
        // createdFrom
        if (Optional.ofNullable(createdFrom).isPresent()) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), createdFrom));
        }
        // createdTo
        if (Optional.ofNullable(createdTo).isPresent()) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), createdTo));
        }
        // latitudeFrom
        if (Optional.ofNullable(latitudeFrom).isPresent()) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("latitude"), latitudeFrom));
        }
        // latitudeTo
        if (Optional.ofNullable(latitudeTo).isPresent()) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("latitude"), latitudeTo));
        }
        // longitudeFrom
        if (Optional.ofNullable(longitudeFrom).isPresent()) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("longitude"), longitudeFrom));
        }
        // longitudeTo
        if (Optional.ofNullable(longitudeTo).isPresent()) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("longitude"), longitudeTo));
        }
        return criteriaQuery.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new))).getRestriction();
    }

    private Predicate searchFieldToPredicate(final Root<IncidentDo> root, final CriteriaBuilder criteriaBuilder) {
        final List<Predicate> predicates = new ArrayList<>();
        final String[] fieldValues = searchField.split(" ");
        for (String fieldValue : fieldValues) {
            fieldValue = "%" + scDf.toScDf(fieldValue) + "%";
            final List<Predicate> subPredicates = new ArrayList<>();
            subPredicates.add(criteriaBuilder.like(toScDf(root.get("title"), criteriaBuilder), fieldValue));
            subPredicates.add(criteriaBuilder.like(toScDf(root.get("description"), criteriaBuilder), fieldValue));
            predicates.add(criteriaBuilder.or(subPredicates.toArray(Predicate[]::new)));
        }
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }

    private Expression<String> toScDf(final Path<String> path, final CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.lower(criteriaBuilder.function("unaccent", String.class, path));
    }
}
