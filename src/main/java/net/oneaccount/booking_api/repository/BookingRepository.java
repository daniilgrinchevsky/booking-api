package net.oneaccount.booking_api.repository;

import net.oneaccount.booking_api.entity.BookingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<BookingEntity, Long> {

    Optional<BookingEntity> findByFilmIdAndDateAndSeat(Long filmId, LocalDate date, Integer seat);

    List<BookingEntity> findAllByDate(LocalDate date);
}
