package net.oneaccount.booking_api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Here should be a link to entity of a film with many to one relationship, but then we need to create this film entity and repo.
    //But we actually don't need it in this task so let's assume filmId stored just as long value without joining to other entity.
    @Column(name = "film_id", nullable = false)
    private Long filmId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "seat", nullable = false)
    private Integer seat;
}
