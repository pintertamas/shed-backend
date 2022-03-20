package hu.bme.aut.shed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @OneToOne()
    private Game game;
}