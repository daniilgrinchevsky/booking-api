package net.oneaccount.booking_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.oneaccount.booking_api.dto.BookingCreateDto;
import net.oneaccount.booking_api.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//In real task i would have written addition tests for BookingServiceImpl with BookingRepository mock,
//also BookingMapperImpl should be tested for correct mappings.
//But i suppose this would be redundant in our task and all relevant cases was covered with class below.
//Also used the same db as for main context. In real case this should be separate dbs.

@SpringBootTest
@AutoConfigureMockMvc
//For recreating context and repopulate db after each test, i think it's a good solution for our purposes.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CONTROLLER_PATH = "/bookings";
    private static final String CONTROLLER_ID_PATH = CONTROLLER_PATH + "/{id}";
    private static final String CONTROLLER_LIST_PATH = CONTROLLER_PATH + "/list/{epoch}";
    private static final long BOOKING_ID_1 = 1L;
    private static final long BOOKING_ID_2 = 2L;
    private static final long BOOKING_FILM_ID_1 = 11L;
    private static final long BOOKING_FILM_ID_2 = 12L;
    private static final long BOOKING_CREATED_FILM_ID = 35L;
    private static final int BOOKING_SEAT_1 = 20;
    private static final int BOOKING_SEAT_2 = 25;
    private static final int BOOKING_CREATED_SEAT = 122;
    private static final Long INCORRECT_BOOKING_ID = 10000L;
    private static final LocalDate DATE = LocalDate.of(2021, 11, 14);

    @Test
    @SneakyThrows
    //Here we don't want to catch exceptions so SneakyThrows used
    public void getBooking_PresentId_StatusIsOkAndBookingDtoReturned() {
        mockMvc.perform(get(CONTROLLER_ID_PATH, BOOKING_ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //Of course we can create objects and then get json content with ObjectMapper and match response that way,
                // but i decided to match json path here and further to simplicity
                .andExpect(jsonPath("$.id").value(BOOKING_ID_1))
                .andExpect(jsonPath("$.filmId").value(BOOKING_FILM_ID_1))
                .andExpect(jsonPath("$.date").value("2021-11-14"))
                .andExpect(jsonPath("$.seat").value(20));
    }

    @Test
    @SneakyThrows
    public void getBooking_AbsentId_StatusIsNotFoundAndErrorDtoReturned() {
        mockMvc.perform(get(CONTROLLER_ID_PATH, INCORRECT_BOOKING_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(EntityNotFoundException.MESSAGE, INCORRECT_BOOKING_ID)));
    }

    @Test
    @SneakyThrows
    public void createBooking_NewBookingParameters_StatusIsOkAndCreatedBookingDtoReturned() {
        BookingCreateDto bookingCreateDto = buildNewBookingCreateDto();
        String requestBody = buildRequestBody(bookingCreateDto);

        mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.filmId").value(bookingCreateDto.getFilmId()))
                .andExpect(jsonPath("$.date").value(bookingCreateDto.getDate().toString()))
                .andExpect(jsonPath("$.seat").value(bookingCreateDto.getSeat()));
    }

    @Test
    @SneakyThrows
    public void createBooking_ExistingBookingParameters_StatusIsInternalServerErrorAndErrorDtoReturned() {
        BookingCreateDto bookingCreateDto = buildExistingBookingCreateDto();
        String requestBody = buildRequestBody(bookingCreateDto);

        mockMvc.perform(post(CONTROLLER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value(String.format(
                        //Again this message should be located in constant class
                        "Can't create a booking for given filmId = %d and date = %s, seat = %d is already booked.",
                        bookingCreateDto.getFilmId(), bookingCreateDto.getDate().toString(), bookingCreateDto.getSeat())));
    }

    @Test
    @SneakyThrows
    public void deleteBooking_PresentId_StatusIsOkAndEmptyResponseReturned() {
        mockMvc.perform(delete(CONTROLLER_ID_PATH, BOOKING_ID_1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void deleteBooking_AbsentId_StatusIsNotFoundAndErrorDtoReturned() {
        mockMvc.perform(delete(CONTROLLER_ID_PATH, INCORRECT_BOOKING_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(String.format(EntityNotFoundException.MESSAGE, INCORRECT_BOOKING_ID)));
    }

    @Test
    @SneakyThrows
    public void getBookingsList_ExistingDate_StatusIsOkAndListOfBookingsDtoReturned() {
        long epoch = DATE.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli();

        mockMvc.perform(get(CONTROLLER_LIST_PATH, epoch)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(BOOKING_ID_1))
                .andExpect(jsonPath("$.[0].filmId").value(BOOKING_FILM_ID_1))
                .andExpect(jsonPath("$.[0].seat").value(BOOKING_SEAT_1))
                .andExpect(jsonPath("$.[0].date").value(DATE.toString()))
                .andExpect(jsonPath("$.[1].id").value(BOOKING_ID_2))
                .andExpect(jsonPath("$.[1].filmId").value(BOOKING_FILM_ID_2))
                .andExpect(jsonPath("$.[1].seat").value(BOOKING_SEAT_2))
                .andExpect(jsonPath("$.[1].date").value(DATE.toString()));
    }

    @Test
    @SneakyThrows
    public void getBookingsList_AbsentDate_StatusIsOkAndEmptyResponseReturned() {
        long epoch = DATE.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli();

        mockMvc.perform(get(CONTROLLER_LIST_PATH, epoch)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }

    private BookingCreateDto buildNewBookingCreateDto() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setFilmId(BOOKING_CREATED_FILM_ID);
        bookingCreateDto.setDate(DATE);
        bookingCreateDto.setSeat(BOOKING_CREATED_SEAT);
        return bookingCreateDto;
    }

    private BookingCreateDto buildExistingBookingCreateDto() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setFilmId(BOOKING_FILM_ID_2);
        bookingCreateDto.setDate(DATE);
        bookingCreateDto.setSeat(BOOKING_SEAT_2);
        return bookingCreateDto;
    }

    @SneakyThrows
    private String buildRequestBody(BookingCreateDto bookingCreateDto) {
        return objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(bookingCreateDto);
    }
}
