package net.oneaccount.booking_api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ErrorDto {

    private final Integer statusCode;
    private final String message;
}
