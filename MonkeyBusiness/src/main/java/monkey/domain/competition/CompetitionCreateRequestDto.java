package monkey.domain.competition;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionCreateRequestDto {
    private String name;
    private LocalDate start;
    private LocalDate end;
}
