package net.oneaccount.booking_api.exception;

import lombok.extern.slf4j.Slf4j;
import net.oneaccount.booking_api.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class BookingApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorDto handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return buildErrorDto(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BookingApiException.class)
    public ErrorDto handleBookingApiException(BookingApiException ex) {
        log.error(ex.getMessage());
        return buildErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ErrorDto buildErrorDto(HttpStatus status, String message) {
        return ErrorDto.of(status.value(), message);
    }
}
