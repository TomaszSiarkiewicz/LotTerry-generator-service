package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LotterryNumbergeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotterryNumbergeneratorApplication.class, args);
    }

}
