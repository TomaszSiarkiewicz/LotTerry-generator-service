package pl.lotto.infrastructure.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.lotto.numbergenerator.NumberGeneratorFacade;

@Component
@Log4j2
@AllArgsConstructor
public class NumberGeneratorScheduler {

    private final NumberGeneratorFacade numberGeneratorFacade;

    @Scheduled(cron = "${lotto.number-generator.lotteryRunOccurrence}")
    public void runNumberGenerating(){
        log.info("scheduler started");
        numberGeneratorFacade.generateNumbersAndSave();
    }
}
