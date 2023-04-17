package pl.lotto.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.lotto.numbergenerator.DrawingResultDto;
import pl.lotto.numbergenerator.NumberGeneratorFacade;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
public class GeneratorController {
    private final NumberGeneratorFacade numberGeneratorFacade;

    public GeneratorController(NumberGeneratorFacade numberGeneratorFacade) {
        this.numberGeneratorFacade = numberGeneratorFacade;
    }

    @GetMapping("/winnum/{date}")
    ResponseEntity<DrawingResultDto> result(@PathVariable Long date) {

        LocalDateTime drawDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC);
        DrawingResultDto drawingResultDto = numberGeneratorFacade.retrieveNumbersByDate(drawDate);
        if (drawingResultDto == null){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(drawingResultDto);
    }
}
