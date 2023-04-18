package pl.lotto.infrastructure.lottoclient;

import java.time.LocalDateTime;

public record NextDrawResponseDto(
        LocalDateTime nextDrawDate
) {
}
