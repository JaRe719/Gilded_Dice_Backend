package de.jare.gildeddice.services;

import de.jare.gildeddice.dtos.ai.response.KSuitAiResponseDTO;
import de.jare.gildeddice.entities.games.storys.Story;
import de.jare.gildeddice.entities.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${llm.product.id}")
    private String PRODUCT_ID;

    private final RestClient restClient;

    public AiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public KSuitAiResponseDTO callApi(Story story, User user) {
        String endpoint = "/1/ai/" + PRODUCT_ID + "/openai/chat/completions";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mixtral");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", story.getPrompt())
        ));
        requestBody.put("max_tokens", 100);

        try {
            return restClient.post()
                    .uri(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(KSuitAiResponseDTO.class);
        } catch (HttpClientErrorException e) {
            System.err.println("API-Error: " + e.getMessage());
            return null;
        }
    }

}
