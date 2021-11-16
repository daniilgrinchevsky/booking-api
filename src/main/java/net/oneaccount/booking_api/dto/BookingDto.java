package net.oneaccount.booking_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingDto {

    //Here validation can be added to meet requirements.
    private Long id;
    private Long filmId;
    private LocalDate date;
    private Integer seat;
}
