package tech.gauthier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@SpringBootApplication
@ComponentScan(basePackages = { "io.orkes", "tech.gauthier" })
@ConfigurationPropertiesScan
@Slf4j
public class App {
    @PostConstruct
    public void initialize() {
        log.info("Workers started.");
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
