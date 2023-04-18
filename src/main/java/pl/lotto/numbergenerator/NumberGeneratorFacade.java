package pl.lotto.numbergenerator;

import lombok.extern.log4j.Log4j2;
import pl.lotto.infrastructure.lottoclient.LottoClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
public class NumberGeneratorFacade {
    private final NumberGenerator numberGenerator;
    private final WinningNumbersRepository repository;
    private final LottoClient lottoClient;



    public NumberGeneratorFacade(WinningNumbersRepository repository, LottoClient lottoClient) {
        this.lottoClient = lottoClient;
        this.repository = repository;
        numberGenerator = new NumberGenerator();
    }

    public DrawingResultDto generateNumbersAndSave() {
        LocalDateTime nextDrawingDate = lottoClient.getNextDrawingDate();
        Optional<WinningNumbers> byDate = repository.findByDate(nextDrawingDate);
        if (byDate.isPresent()) {
            WinningNumbers winningNumbers1 = byDate.get();
            log.info("numbers was already generated for: " + winningNumbers1.date());
            return new DrawingResultDto(winningNumbers1.date(), winningNumbers1.numbers());
        }

        List<Integer> numbers = numberGenerator.generate()
                .stream()
                .toList();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .numbers(numbers)
                .date(nextDrawingDate)
                .build();
        WinningNumbers saved = repository.save(winningNumbers);
        log.info(saved);
        return new DrawingResultDto(saved.date(), saved.numbers());
    }

    public DrawingResultDto retrieveNumbersByDate(LocalDateTime date) {
        WinningNumbers winningNumbers = repository.findByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("winning number not found"));
        return new DrawingResultDto(winningNumbers.date(), winningNumbers.numbers());
    }
}
