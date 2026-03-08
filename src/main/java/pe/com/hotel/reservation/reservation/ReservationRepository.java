package pe.com.hotel.reservation.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByActiveTrue(Pageable pageable);
    Page<Reservation> findByGuestId(Long guestId, Pageable pageable);
    Page<Reservation> findByRoomId(Long roomId, Pageable pageable);
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reservation r
        WHERE r.room.id = :roomId
        AND r.status IN ('PENDING', 'CONFIRMED')
        AND r.active = true
        AND r.checkInDate < :checkOut
        AND r.checkOutDate > :checkIn
    """)
    boolean existsOverlappingReservation(
            @Param("roomId") Long roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}

