package entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CINEMA_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String mail;

    @Column(name = "phone_number")
    private long phoneNumber;

    @OneToMany(mappedBy = "user")
    private Set<Reservation> reservations = new HashSet<>();
}
