package sk.janobono.emergency;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import sk.janobono.emergency.common.model.IncidentLevel;
import sk.janobono.emergency.common.model.IncidentStatus;
import sk.janobono.emergency.common.model.IncidentType;
import sk.janobono.emergency.dal.domain.IncidentDo;
import sk.janobono.emergency.dal.repository.IncidentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.flyway.clean-disabled=false"
        }
)
public class BaseTest {

    public static final PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>
            ("postgres:alpine")
            .withDatabaseName("app")
            .withUsername("app")
            .withPassword("app");

    static {
        postgresDB.start();
    }

    @DynamicPropertySource
    public static void properties(final DynamicPropertyRegistry registry) throws Exception {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
    }

    protected static final int YEAR = 2024;
    protected static final int MONTH = 8;
    protected static final int DAY = 11;

    @Value("${local.server.port}")
    public int serverPort;

    @Autowired
    public Flyway flyway;

    @Autowired
    public IncidentRepository incidentRepository;

    @BeforeEach
    public void setUp() throws Exception {
        flyway.clean();
        flyway.migrate();
    }

    protected void createTestIncidents() {
        for (int index = 0; index < 30; index++) {
            LocalDateTime created = LocalDateTime.of(YEAR, MONTH, DAY, 0, 0);
            final IncidentType type;
            final int level;
            final BigDecimal latitude;
            final BigDecimal longitude;
            if (index <= 9) {
                created = created.minusDays(10);
                type = IncidentType.CRIME;
                level = IncidentLevel.LOW.getLevel();
                latitude = BigDecimal.valueOf(-90);
                longitude = BigDecimal.valueOf(-180);
            } else if (index <= 19) {
                type = IncidentType.FIRE;
                level = IncidentLevel.MEDIUM.getLevel();
                latitude = BigDecimal.valueOf(0);
                longitude = BigDecimal.valueOf(0);
            } else {
                created = created.plusDays(10);
                type = IncidentType.MEDICAL;
                level = IncidentLevel.HIGH.getLevel();
                latitude = BigDecimal.valueOf(90);
                longitude = BigDecimal.valueOf(180);
            }

            final IncidentDo incidentDo = new IncidentDo();
            incidentDo.setCreated(created);
            incidentDo.setType(type);
            incidentDo.setLevel(level);
            incidentDo.setStatus(IncidentStatus.NEW);
            incidentDo.setLatitude(latitude);
            incidentDo.setLongitude(longitude);
            incidentDo.setTitle("Incident title %02d".formatted(index));
            incidentDo.setDescription("Incident description %02d".formatted(index));
            incidentRepository.save(incidentDo);
        }
    }
}
