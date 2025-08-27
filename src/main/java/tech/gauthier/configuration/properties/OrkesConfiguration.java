package tech.gauthier.configuration.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.orkes.conductor.client.ApiClient;
import io.orkes.conductor.client.OrkesClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrkesConfiguration {

    private final OrkesProperties orkesProperties;

    @Bean
    @Primary
    public OrkesClients orkesClient() {
        return new OrkesClients(conductorApiClient());
    }

    @Bean
    @Primary
    public ApiClient conductorApiClient() {
        log.info("Connecting to conductor server {} ", orkesProperties.getServer().getUrl());
        ApiClient apiClient = new ApiClient(orkesProperties.getServer().getUrl());
        apiClient.setWriteTimeout(30_000);
        apiClient.setReadTimeout(30_000);
        apiClient.setConnectTimeout(30_000);
        return apiClient;
    }
}
