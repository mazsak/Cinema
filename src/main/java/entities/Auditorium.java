package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "AUDITORIUM")
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "auditorium")
    private Set<Seat> seats;

    @OneToMany(mappedBy = "auditorium")
    private Set<Screening> screenings;

    private String name;
}
