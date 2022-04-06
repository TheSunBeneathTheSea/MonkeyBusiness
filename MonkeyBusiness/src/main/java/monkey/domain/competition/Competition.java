package monkey.domain.competition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
public class Competition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate start;

    @Column(nullable = false)
    private LocalDate end;

    private boolean active;

    @OneToMany(mappedBy = "competition", fetch = FetchType.LAZY)
    private Set<Participant> participants;

    @Builder
    public Competition(String name, LocalDate start, LocalDate end) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.active = false;
    }

    public void startCompetition(){
        this.active = true;
    }

    public void endCompetition(){
        this.active = false;
    }
}
