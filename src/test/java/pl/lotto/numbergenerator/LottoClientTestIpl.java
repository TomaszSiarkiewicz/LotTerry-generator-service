package pl.lotto.numbergenerator;

import pl.lotto.AdjustableClock;
import pl.lotto.infrastructure.lottoclient.LottoClient;

import java.time.LocalDateTime;

public class LottoClientTestIpl implements LottoClient {
    private final AdjustableClock clock;

    public LottoClientTestIpl(AdjustableClock clock) {
        this.clock = clock;
    }

    @Override
    public LocalDateTime getNextDrawingDate() {
        return clock.getLocalDateTime();
    }
}
