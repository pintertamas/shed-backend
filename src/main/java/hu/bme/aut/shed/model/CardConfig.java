package hu.bme.aut.shed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "CardConfig")
@Table(name = "cardsconfig")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column()
    private int number;

    @Column()
    private Shape shape;

    @Column()
    private Rule rule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id", nullable=false)
    @JsonIgnore
    private Game game;
}
