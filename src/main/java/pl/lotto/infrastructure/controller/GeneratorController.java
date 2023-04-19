package pl.lotto.infrastructure.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.numbergenerator.DrawingResultDto;
import pl.lotto.numbergenerator.NumberGeneratorFacade;
import pl.lotto.numbergenerator.WinningNumbersNotFoundException;

import java.time.LocalDateTime;

@Log4j2
@RestController
public class GeneratorController {
    private final NumberGeneratorFacade numberGeneratorFacade;

    public GeneratorController(NumberGeneratorFacade numberGeneratorFacade) {
        this.numberGeneratorFacade = numberGeneratorFacade;
    }

    @GetMapping("/winnum/{date}")
    ResponseEntity<DrawingResultDto> result(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            DrawingResultDto drawingResultDto = numberGeneratorFacade.retrieveNumbersByDate(date);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(drawingResultDto);
        } catch (WinningNumbersNotFoundException exception) {
            log.info(exception);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

    }
}
