package sk.janobono.emergency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(
        scanBasePackages = {"sk.janobono.emergency"}
)
@ConfigurationPropertiesScan(basePackages = {"sk.janobono.emergency"})
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
