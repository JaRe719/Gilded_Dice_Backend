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

    private static final String ENDPOINT_TEMPLATE = "/1/ai/%s/openai/chat/completions";
    private static final String MODEL = "mixtral";
    private static final int MAX_TOKENS = 250;

//    @Value("${llm.product.id}")
//    private String PRODUCT_ID;
    private final String PRODUCT_ID;
    private final RestClient restClient;


    public AiService(@Value("${llm.product.id}") String productId, RestClient restClient) {
        this.PRODUCT_ID = productId;
        this.restClient = restClient;
    }
    
    public KSuitAiResponseDTO callApi(String prompt) {
        try {
            return restClient.post()
                    .uri(buildEndpoint())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(buildRequest(prompt))
                    .retrieve()
                    .body(KSuitAiResponseDTO.class);
        } catch (HttpClientErrorException e) {
            System.err.println("API-Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw e;
        }
    }

    private String buildEndpoint() {
        return String.format(ENDPOINT_TEMPLATE, PRODUCT_ID);
    }

    private Map<String, Object> buildRequest(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", MODEL);
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", MAX_TOKENS);
        return requestBody;
    }
}
