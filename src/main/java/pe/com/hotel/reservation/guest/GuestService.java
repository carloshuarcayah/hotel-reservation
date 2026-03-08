package pe.com.hotel.reservation.guest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.hotel.reservation.guest.dto.CreateGuestRequest;
import pe.com.hotel.reservation.guest.dto.GuestResponse;
import pe.com.hotel.reservation.guest.dto.UpdateGuestRequest;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestService {
    private final GuestRepository guestRepository;

    public Page<GuestResponse> getAllGuests(Pageable pageable) {
        return guestRepository.findAll(pageable).map(GuestResponse::fromEntity);
    }

    public Page<GuestResponse> getActiveGuests(Pageable pageable) {
        return guestRepository.findByActiveTrue(pageable).map(GuestResponse::fromEntity);
    }

    public GuestResponse getGuestById(Long id) {
        Guest guest = findGuestOrThrow(id);
        return GuestResponse.fromEntity(guest);
    }

    public GuestResponse getGuestByDocumentNumber(String documentNumber) {
        Guest guest = guestRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Guest not found with document number: " + documentNumber));
        return GuestResponse.fromEntity(guest);
    }

    @Transactional
    public GuestResponse createGuest(CreateGuestRequest request) {
        if (guestRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException(
                    "Already exists a guest with email: " + request.email());
        }
        if (guestRepository.existsByDocumentNumber(request.documentNumber())) {
            throw new IllegalArgumentException(
                    "Already exists a guest with document number: " + request.documentNumber());
        }

        Guest guest = Guest.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .documentNumber(request.documentNumber())
                .active(true)
                .build();

        return GuestResponse.fromEntity(guestRepository.save(guest));
    }

    @Transactional
    public GuestResponse updateGuest(Long id, UpdateGuestRequest request) {
        Guest guest = findGuestOrThrow(id);

        if (request.firstName() != null) guest.setFirstName(request.firstName());
        if (request.lastName() != null) guest.setLastName(request.lastName());
        if (request.email() != null) {
            if (!request.email().equals(guest.getEmail())
                    && guestRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException(
                        "Already exists a guest with email: " + request.email());
            }
            guest.setEmail(request.email());
        }
        if (request.phone() != null) guest.setPhone(request.phone());

        return GuestResponse.fromEntity(guestRepository.save(guest));
    }

    @Transactional
    public GuestResponse deleteGuest(Long id) {
        Guest guest = findGuestOrThrow(id);
        // todo: verificar que no eliminemos un guest que tiene reservas activas a menos que requiera
        guest.setActive(false);
        return GuestResponse.fromEntity(guest);
    }

    @Transactional
    public GuestResponse enableGuest(Long id) {
        Guest guest = findGuestOrThrow(id);
        guest.setActive(true);
        guestRepository.save(guest);
        return GuestResponse.fromEntity(guest);
    }

    // HELPERS
    private Guest findGuestOrThrow(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found with id: " + id));
    }
}