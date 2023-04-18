package pl.lotto.infrastructure.lottoclient;

import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;


public class LottoClientImpl implements LottoClient {
    private final WebClient webClient;

    public LottoClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public LocalDateTime getNextDrawingDate() {

        return null;
    }
}
