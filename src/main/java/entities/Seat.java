package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auditorium_id", nullable = false)
    private Auditorium auditorium;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Seat_Reservation",
            joinColumns = { @JoinColumn(name = "seat_id") },
            inverseJoinColumns = { @JoinColumn(name = "reservation_id") }
    )
    private Set<Reservation> reservations= new HashSet<>();

    private int row;
    private int number;

}
