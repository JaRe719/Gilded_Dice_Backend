package de.jare.gildeddice.dtos.ai.response;

import java.util.List;

public record KSuitAiResponseDTO(
        String model,
        String id,
        String object,
        String system_fingerprint,
        long created,
        List<KSuitAiChoicesDTO> choices
) {
}
