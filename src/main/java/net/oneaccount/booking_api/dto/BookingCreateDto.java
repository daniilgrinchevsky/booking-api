package net.oneaccount.booking_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


//In DTOs, model and entity used boxed types because we can easily have null values in those fields.
@Getter
@Setter
public class BookingCreateDto {

    private Long filmId;
    private LocalDate date;
    private Integer seat;
}
