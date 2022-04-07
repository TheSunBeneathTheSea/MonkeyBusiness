package monkey.domain.competition;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CompetitionVO {
    private Long id;
    private String name;
    private LocalDate start;
    private LocalDate end;
    private boolean active;

    public CompetitionVO(Competition competition) {
        this.id = competition.getId();
        this.name = competition.getName();
        this.start = competition.getStart();
        this.end = competition.getEnd();
        this.active = competition.isActive();
    }

    public static List<CompetitionVO> transformList(List<Competition> competitions) {
        return competitions.stream().map(c -> new CompetitionVO(c)).collect(Collectors.toList());
    }
}
