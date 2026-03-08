package pe.com.hotel.reservation.reservation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.hotel.reservation.guest.Guest;
import pe.com.hotel.reservation.guest.GuestRepository;
import pe.com.hotel.reservation.reservation.dto.CreateReservationRequest;
import pe.com.hotel.reservation.reservation.dto.ReservationResponse;
import pe.com.hotel.reservation.reservation.dto.UpdateReservationRequest;
import pe.com.hotel.reservation.room.Room;
import pe.com.hotel.reservation.room.RoomRepository;
import pe.com.hotel.reservation.room.RoomStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService{
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable)
                .map(ReservationResponse::fromEntity);
    }

    public Page<ReservationResponse> getActiveReservations(Pageable pageable) {
        return reservationRepository.findByActiveTrue(pageable)
                .map(ReservationResponse::fromEntity);
    }

    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = findReservationOrThrow(id);
        return ReservationResponse.fromEntity(reservation);
    }

    public Page<ReservationResponse> getReservationsByGuest(Long guestId, Pageable pageable) {
        return reservationRepository.findByGuestId(guestId, pageable)
                .map(ReservationResponse::fromEntity);
    }

    public Page<ReservationResponse> getReservationsByRoom(Long roomId, Pageable pageable) {
        return reservationRepository.findByRoomId(roomId, pageable)
                .map(ReservationResponse::fromEntity);
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        // Validar fechas
        validateDates(request.checkInDate(), request.checkOutDate());


        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Room not found with id: " + request.roomId()));

        Guest guest = guestRepository.findById(request.guestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Guest not found with id: " + request.guestId()));


        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Room " + room.getRoomNumber() + " is not available. Current status: " + room.getStatus());
        }

        // Validar que no haya reservas solapadas
        if (reservationRepository.existsOverlappingReservation(
                request.roomId(), request.checkInDate(), request.checkOutDate())) {
            throw new IllegalStateException(
                    "Room " + room.getRoomNumber() + " already has a reservation for the selected dates");
        }


        long nights = ChronoUnit.DAYS.between(request.checkInDate(), request.checkOutDate());
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Reservation reservation = Reservation.builder()
                .room(room)
                .guest(guest)
                .checkInDate(request.checkInDate())
                .checkOutDate(request.checkOutDate())
                .totalPrice(totalPrice)
                .status(ReservationStatus.PENDING)
                .notes(request.notes())
                .build();

        return ReservationResponse.fromEntity(reservationRepository.save(reservation));
    }

    @Transactional
    public ReservationResponse updateReservation(Long id, UpdateReservationRequest request) {
        Reservation reservation = findReservationOrThrow(id);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException(
                    "Can only update reservations in PENDING status. Current: " + reservation.getStatus());
        }

        LocalDate newCheckIn = request.checkInDate() != null ? request.checkInDate() : reservation.getCheckInDate();
        LocalDate newCheckOut = request.checkOutDate() != null ? request.checkOutDate() : reservation.getCheckOutDate();


        if (request.checkInDate() != null || request.checkOutDate() != null) {
            validateDates(newCheckIn, newCheckOut);

            if (reservationRepository.existsOverlappingReservationExcluding(
                    reservation.getRoom().getId(), reservation.getId(), newCheckIn, newCheckOut)) {
                throw new IllegalStateException(
                        "Room already has a reservation for the selected dates");
            }

            reservation.setCheckInDate(newCheckIn);
            reservation.setCheckOutDate(newCheckOut);


            long nights = ChronoUnit.DAYS.between(newCheckIn, newCheckOut);
            BigDecimal totalPrice = reservation.getRoom().getPricePerNight()
                    .multiply(BigDecimal.valueOf(nights));
            reservation.setTotalPrice(totalPrice);
        }

        if (request.notes() != null) {
            reservation.setNotes(request.notes());
        }

        return ReservationResponse.fromEntity(reservationRepository.save(reservation));
    }

    //status changes

    @Transactional
    public ReservationResponse confirmReservation(Long id) {
        Reservation reservation = findReservationOrThrow(id);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException(
                    "Can only confirm PENDING reservations. Current: " + reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        return ReservationResponse.fromEntity(reservation);
    }

    @Transactional
    public ReservationResponse cancelReservation(Long id) {
        Reservation reservation = findReservationOrThrow(id);

        if (reservation.getStatus() != ReservationStatus.PENDING
                && reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Can only cancel PENDING or CONFIRMED reservations. Current: " + reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return ReservationResponse.fromEntity(reservation);
    }

    @Transactional
    public ReservationResponse completeReservation(Long id) {
        Reservation reservation = findReservationOrThrow(id);

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Can only complete CONFIRMED reservations. Current: " + reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        return ReservationResponse.fromEntity(reservation);
    }

    //delete
    @Transactional
    public ReservationResponse deleteReservation(Long id) {
        Reservation reservation = findReservationOrThrow(id);
        reservation.setActive(false);
        return ReservationResponse.fromEntity(reservation);
    }

    @Transactional
    public ReservationResponse enableReservation(Long id) {
        Reservation reservation = findReservationOrThrow(id);
        reservation.setActive(true);
        return ReservationResponse.fromEntity(reservation);
    }


    //Helpers
    private Reservation findReservationOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Reservation not found with id: " + id));
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date");
        }
    }
}
