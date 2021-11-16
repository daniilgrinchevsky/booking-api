package net.oneaccount.booking_api.service.impl;

import lombok.RequiredArgsConstructor;
import net.oneaccount.booking_api.entity.BookingEntity;
import net.oneaccount.booking_api.exception.BookingApiException;
import net.oneaccount.booking_api.exception.EntityNotFoundException;
import net.oneaccount.booking_api.mapper.BookingMapper;
import net.oneaccount.booking_api.model.Booking;
import net.oneaccount.booking_api.repository.BookingRepository;
import net.oneaccount.booking_api.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    public Booking getBooking(Long id) {
        return bookingRepository.findById(id)
                .map(bookingMapper::bookingEntityToBooking)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking createBooking(Booking booking) {
        //As far as i understood this is the kind of how we can be sure no double booking can be performed, as simple as it is.
        Optional<BookingEntity> maybeBooking = bookingRepository.findByFilmIdAndDateAndSeat(booking.getFilmId(), booking.getDate(), booking.getSeat());
        maybeBooking.ifPresent(bookingEntity -> {
            throw new BookingApiException(String.format(
                    "Can't create a booking for given filmId = %d and date = %s, seat = %d is already booked.", //Here message also should be located in constants class
                    booking.getFilmId(), booking.getDate().toString(), booking.getSeat()));
        });
        BookingEntity bookingEntity = bookingMapper.bookingToBookingEntity(booking);
        bookingEntity = bookingRepository.save(bookingEntity);
        return bookingMapper.bookingEntityToBooking(bookingEntity);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date) {
        return bookingRepository.findAllByDate(date).stream()
                .map(bookingMapper::bookingEntityToBooking)
                .collect(Collectors.toList());
    }
}
