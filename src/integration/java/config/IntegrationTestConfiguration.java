package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.lotto.AdjustableClock;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Configuration
public class IntegrationTestConfiguration {
    @Bean
    @Primary
    AdjustableClock clock(){
        return new AdjustableClock(LocalDateTime.of(2023, 4, 6, 12, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }
}
