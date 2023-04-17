package pl.lotto.numbergenerator;

import org.junit.jupiter.api.Test;
import pl.lotto.AdjustableClock;
import pl.lotto.infrastructure.lottoclient.LottoClient;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberGeneratorFacadeTest {
    WinningNumbersRepository winningNumbersRepository = new InMemoryWinningNumbersDatabaseImplementation();
    AdjustableClock clock =new AdjustableClock(LocalDateTime.of(2023, 3, 5, 10, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());

    @Test
    public void should_return_six_distinct_number_in_range_of_1_99() {
        //given
        LocalDateTime drawingDate = LocalDateTime.of(2023, 3, 22, 12, 0);
        clock.setClockToLocalDateTime(drawingDate);
        LottoClient lottoClient = new LottoClientTestIpl(clock);
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorFacadeConfiguration().numberGeneratorFacade(winningNumbersRepository, lottoClient);

        //when
        DrawingResultDto drawingResult = numberGeneratorFacade.generateNumbersAndSave();

        //then
        assertThat(new HashSet<>(drawingResult.numbers())).hasSize(6);
        assertThat(drawingResult.numbers()).noneMatch(integer -> integer > 99 || integer < 0);
    }

    @Test
    public void should_save_numbers_for_current_draw_date() {
        //given
        LocalDateTime drawingDate = LocalDateTime.of(2023, 3, 22, 12, 0);
        clock.setClockToLocalDateTime(drawingDate);
        LottoClient lottoClient = new LottoClientTestIpl(clock);
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorFacadeConfiguration().numberGeneratorFacade(winningNumbersRepository, lottoClient);

        //when
        DrawingResultDto drawingResult = numberGeneratorFacade.generateNumbersAndSave();

        //then
        DrawingResultDto resultDto = numberGeneratorFacade.retrieveNumbersByDate(drawingDate);

        assertThat(drawingResult).isEqualTo(resultDto);
    }

    @Test
    public void should_return_null_saving_drawing_result_when_drawn_number_in_database_for_given_day() {
        //given
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        clock.setClockToLocalDateTime(dateTime);
        LottoClient lottoClient = new LottoClientTestIpl(clock);
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorFacadeConfiguration().numberGeneratorFacade(winningNumbersRepository, lottoClient);

        //when
        DrawingResultDto firstDrawingResult = numberGeneratorFacade.generateNumbersAndSave();
        DrawingResultDto secondDrawingSameDay = numberGeneratorFacade.generateNumbersAndSave();

        //then
        assertThat(numberGeneratorFacade.retrieveNumbersByDate(lottoClient.getNextDrawingDate()).numbers()).isEqualTo(firstDrawingResult.numbers());
    }

    @Test
    public void should_return_one_drawing_result_for_given_saturday() {
        //given
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 12, 0);
        clock.setClockToLocalDateTime(dateTime);
        LottoClient lottoClient = new LottoClientTestIpl(clock);
        NumberGeneratorFacade numberGeneratorFacade = new NumberGeneratorFacadeConfiguration().numberGeneratorFacade(winningNumbersRepository, lottoClient);

        //when
        LocalDateTime firstDrawingDate = lottoClient.getNextDrawingDate();
        DrawingResultDto twoWeeksBackDrawingResult = numberGeneratorFacade.generateNumbersAndSave();
        clock.plusDays(7);
        LocalDateTime secondDrawingDate = lottoClient.getNextDrawingDate();
        DrawingResultDto oneWeeksBackDrawingResult = numberGeneratorFacade.generateNumbersAndSave();

        //then
        assertThat(oneWeeksBackDrawingResult).isEqualTo(numberGeneratorFacade.retrieveNumbersByDate(secondDrawingDate));
        assertThat(twoWeeksBackDrawingResult).isEqualTo(numberGeneratorFacade.retrieveNumbersByDate(firstDrawingDate));
    }
}