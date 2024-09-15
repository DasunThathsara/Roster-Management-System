package com.roster.backned.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "rosters",
        uniqueConstraints = @UniqueConstraint(columnNames = {"date", "restaurant_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Roster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private long id;
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

}
