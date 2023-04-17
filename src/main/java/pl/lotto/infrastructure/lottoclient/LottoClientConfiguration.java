package pl.lotto.infrastructure.lottoclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LottoClientConfiguration {
    @Value("${lotto.lotto-client.baseURL}")
    private String BASE_URL;


    @Bean
    public LottoClientImpl lottoClientImpl(){
        WebClient webClient = WebClient
                .builder()
                .baseUrl(BASE_URL)
                .build();
        return new LottoClientImpl(webClient);
    }
}
