package tech.gauthier.configuration.properties;

import lombok.Data;

import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Validated
public class OrkesServer {

    @NotBlank
    @NotNull
    private String url;
}
