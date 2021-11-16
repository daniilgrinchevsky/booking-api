package net.oneaccount.booking_api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Booking {

    //In this case we have the same attributes in dto, model and entity. But in real project it can be changed at any time,                                                                                                                                                                                                  so i decided to make it canonical way
    //so i created different objects for each layer, where layers working only with related objects.
    private Long id;
    private Long filmId;
    private LocalDate date;
    private Integer seat;
}
