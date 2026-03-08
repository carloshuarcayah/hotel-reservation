package pe.com.hotel.reservation.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {
    Page<Room> findByActiveTrue(Pageable pageable);
    Optional<Room> findByRoomNumber(String roomNumber);
    Page<Room> findByRoomType(RoomType roomType, Pageable pageable);
    Page<Room> findByStatus(RoomStatus status, Pageable pageable);
    boolean existsByRoomNumber(String roomNumber);
    @Query("""
        SELECT r FROM Room r
        WHERE r.status = 'AVAILABLE'
        AND r.roomType = :roomType
        AND r.id NOT IN (
            SELECT res.room.id FROM Reservation res
            WHERE res.status IN ('PENDING', 'CONFIRMED')
            AND res.checkInDate < :checkOut
            AND res.checkOutDate > :checkIn
        )
    """)
    Page<Room> findAvailableRooms(
            @Param("roomType") RoomType roomType,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            Pageable pageable
    );
}
