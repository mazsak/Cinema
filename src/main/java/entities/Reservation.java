package entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RESERVATION")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Singular
    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private Set<Screening> screenings;

    @Singular
    @ManyToMany(mappedBy = "reservations", fetch = FetchType.LAZY)
    private Set<Seat> seats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private boolean reserved;
}
