package pl.lotto.numbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.infrastructure.lottoclient.LottoClient;

@Configuration
public class NumberGeneratorFacadeConfiguration {
    @Bean
    public NumberGeneratorFacade numberGeneratorFacade(WinningNumbersRepository repository, LottoClient lottoClient) {
        return new NumberGeneratorFacade(repository, lottoClient);
    }
}