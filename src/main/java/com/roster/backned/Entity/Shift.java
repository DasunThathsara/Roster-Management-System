package com.roster.backned.Entity;

import com.roster.backned.Enumeration.Duty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    private Duty duty;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    private Long CreatedBy;

    @NotNull
    private Long ModifiedBy;

    @NotNull
    private LocalDateTime CreatedDate;

    @NotNull
    private LocalDateTime ModifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "roster_id")
    private Roster roster;
}
