package entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Blob;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "MOVIE")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Movie_Director",
            joinColumns = { @JoinColumn(name = "movie_id") },
            inverseJoinColumns = { @JoinColumn(name = "director_id") }
    )
    private Set<Director> directors= new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Movie_Actor",
            joinColumns = { @JoinColumn(name = "movie_id") },
            inverseJoinColumns = { @JoinColumn(name = "actor_id") }
    )
    private List<Actor> actors;

    @OneToMany(mappedBy = "movie")
    private Set<Screening> screenings;

    private String description;
    private int duration;
    private Blob image;
}
