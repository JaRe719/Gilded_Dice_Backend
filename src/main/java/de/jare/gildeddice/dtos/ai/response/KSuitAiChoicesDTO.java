package de.jare.gildeddice.dtos.ai.response;

public record KSuitAiChoicesDTO(
        long index,
        KSuitAiMessageDTO message,
        Object logprobs,
        String finish_reason
) {
}
