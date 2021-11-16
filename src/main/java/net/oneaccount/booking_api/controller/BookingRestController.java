package net.oneaccount.booking_api.controller;

import lombok.RequiredArgsConstructor;
import net.oneaccount.booking_api.dto.BookingCreateDto;
import net.oneaccount.booking_api.dto.BookingDto;
import net.oneaccount.booking_api.mapper.BookingMapper;
import net.oneaccount.booking_api.model.Booking;
import net.oneaccount.booking_api.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

//Routing for endpoints was chosen based on REST principles i know,
//not like in text of the assignment(i think there routes written that way just to understand the idea).

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final BookingMapper bookingMapper;
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable Long id) {
        Booking booking = bookingService.getBooking(id);
        return ResponseEntity.ok(bookingMapper.bookingToBookingDto(booking));
    }

    //Assuming that cancel action = delete action from the storage. It can be other requirement that canceled records not deleted from the storage,
    //but marked as deleted, in that case logic will be slightly different. But in our case for simplify process i decided to assume actions are equal.
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingCreateDto bookingCreateDto) {
        Booking booking = bookingMapper.bookingCreateDtoToBooking(bookingCreateDto);
        booking = bookingService.createBooking(booking);
        //Here is better to return object instead of just a 200 status because id or some fields of a new created item may be needed at UI.
        return ResponseEntity.ok(bookingMapper.bookingToBookingDto(booking));
    }

    //I decided to receive date as epoch format. Because client shouldn't know in what format date has to be passed.
    // Whether required some custom format of a date we can override mvcConversionService bean and write logic
    // to convert date on format we needed, to provide global app format date config.
    @GetMapping("/list/{epoch}")
    public ResponseEntity<List<BookingDto>> getBookingsList(@PathVariable Long epoch) {
        LocalDate date = LocalDate.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.of("UTC"));
        List<BookingDto> bookings = bookingService.getBookingsByDate(date).stream()
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookings);
    }

}
