package pl.lotto.infrastructure.lottoclient;

import java.time.LocalDateTime;

public interface LottoClient {
    LocalDateTime getNextDrawingDate();
}
