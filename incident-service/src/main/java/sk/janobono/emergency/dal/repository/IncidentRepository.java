package sk.janobono.emergency.dal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.janobono.emergency.dal.domain.IncidentDo;

public interface IncidentRepository extends JpaRepository<IncidentDo, Long> {

    void deleteById(long id);

    boolean existsById(long incidentId);

    Page<IncidentDo> findAll(Specification<IncidentDo> specification, Pageable pageable);
}
