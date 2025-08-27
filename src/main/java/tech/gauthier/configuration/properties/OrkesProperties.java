package tech.gauthier.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Validated
@ConfigurationProperties(prefix = "conductor")
public class OrkesProperties {

    @NotNull
    private OrkesServer server = new OrkesServer();
}
