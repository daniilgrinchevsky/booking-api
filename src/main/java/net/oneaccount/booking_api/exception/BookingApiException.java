package net.oneaccount.booking_api.exception;

//General exception
public class BookingApiException extends RuntimeException {

    public BookingApiException(String message) {
        super(message);
    }
}
