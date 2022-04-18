package monkey.domain.competition;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor(staticName = "of")
public class CompetitionCreateRequestDto {
    private final String name;
    private final LocalDate start;
    private final LocalDate end;
}
