package net.oneaccount.booking_api.service;

import net.oneaccount.booking_api.model.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    Booking getBooking(Long id);

    void deleteBooking(Long id);

    Booking createBooking(Booking booking);

    List<Booking> getBookingsByDate(LocalDate date);
}
