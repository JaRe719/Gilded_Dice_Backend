package de.jare.gildeddice.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${llm.url}")
    private String LLM_URL;

    @Value("${llm.token}")
    private String LLM_TOKEN;


    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(LLM_URL)
                .defaultHeader("Authorization", "Bearer " + LLM_TOKEN)
                .build();
    }
}
