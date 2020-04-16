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
