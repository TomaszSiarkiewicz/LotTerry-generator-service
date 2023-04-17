package pl.lotto.numbergenerator;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Builder
public record WinningNumbers(
        @Id
        String id,
        @Indexed(unique = true)
        LocalDateTime date,
        List<Integer> numbers
) {
}
