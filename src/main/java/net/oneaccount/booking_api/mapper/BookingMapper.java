package net.oneaccount.booking_api.mapper;

import net.oneaccount.booking_api.dto.BookingCreateDto;
import net.oneaccount.booking_api.dto.BookingDto;
import net.oneaccount.booking_api.entity.BookingEntity;
import net.oneaccount.booking_api.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking bookingEntityToBooking(BookingEntity source);
    Booking bookingCreateDtoToBooking(BookingCreateDto source);
    BookingEntity bookingToBookingEntity(Booking source);
    BookingDto bookingToBookingDto(Booking source);
}
