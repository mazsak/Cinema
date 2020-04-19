package entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Blob;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MOVIE")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Singular
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Movie_Director",
            joinColumns = { @JoinColumn(name = "movie_id") },
            inverseJoinColumns = { @JoinColumn(name = "director_id") }
    )
    private Set<Director> directors= new HashSet<>();

    @Singular
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Movie_Actor",
            joinColumns = { @JoinColumn(name = "movie_id") },
            inverseJoinColumns = { @JoinColumn(name = "actor_id") }
    )
    private Set<Actor> actors;

    private String description;
    private int duration;
    private Blob image;
}
