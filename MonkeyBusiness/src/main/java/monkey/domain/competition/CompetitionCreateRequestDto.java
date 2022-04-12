package monkey.domain.competition;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionCreateRequestDto {
    private String name;
    private LocalDate start;
    private LocalDate end;

    public CompetitionCreateRequestDto(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
}
