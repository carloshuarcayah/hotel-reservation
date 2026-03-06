package pe.com.hotel.reservation.guest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pe.com.hotel.reservation.guest.dto.CreateGuestRequest;
import pe.com.hotel.reservation.guest.dto.GuestResponse;
import pe.com.hotel.reservation.guest.dto.UpdateGuestRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {

    @Mock
    GuestRepository repository;

    @InjectMocks
    GuestService service;

    //GETS
    @Test
    @DisplayName("Should return a a pageable with 2 guests")
    void getAllGuests_ReturnGuestResponses_OnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();

        Guest guest1 = Guest.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber("32647586")
                .active(true)
                .createdAt(now)
                .build();
        Guest guest2 = Guest.builder()
                .id(2L)
                .firstName("Miguel")
                .lastName("Abuelo")
                .email("miguelabuelo@mail.com")
                .phone("756859405")
                .documentNumber("85064326")
                .createdAt(now)
                .active(true)
                .build();

        Page<Guest> page = new PageImpl<>(List.of(guest1, guest2));

        when(repository.findAll(pageable)).thenReturn(page);
        Page<GuestResponse> result = service.getAllGuests(pageable);

        assertAll("Guests validation",
                () -> assertEquals(2, result.getTotalElements(), "The page should contain exactly 2 elements"),

                () -> {
                    GuestResponse first = result.getContent().getFirst();
                    assertAll("First guest validation",
                            () -> assertEquals(1L, first.id()),
                            () -> assertEquals("Luis", first.firstName()),
                            () -> assertEquals("luismontalvo@mail.com", first.email()),
                            () -> assertEquals("123456789", first.phone()),
                            () -> assertEquals("32647586", first.documentNumber()),
                            () -> assertEquals(now, first.createdAt()),
                            () -> assertTrue(first.active())
                    );
                },

                // Validación del Segundo Huésped (Miguel)
                () -> {
                    GuestResponse second = result.getContent().getLast();
                    assertAll("Second guest validation",
                            () -> assertEquals(2L, second.id()),
                            () -> assertEquals("Miguel", second.firstName()),
                            () -> assertEquals("miguelabuelo@mail.com", second.email()),
                            () -> assertEquals("756859405", second.phone()),
                            () -> assertEquals("85064326", second.documentNumber()),
                            () -> assertEquals(now, second.createdAt()),
                            () -> assertTrue(second.active())
                    );
                }
        );
        verify(repository, times(1)).findAll(pageable);
    }

    //GET IF ID EXISTS
    @Test
    @DisplayName("Should return details of a guest if id exists")
    void shouldReturnGuestDetails_WhenIdExists() {
        LocalDateTime now = LocalDateTime.now();

        Guest guest1 = Guest.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber("32647586")
                .createdAt(now)
                .active(true)
                .build();

        //FIND GUEST
        when(repository.findById(1L)).thenReturn(Optional.of(guest1));

        //get result
        GuestResponse result = service.getGuestById(1L);

        assertAll("Guest validation",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.id()),
                () -> assertEquals("luismontalvo@mail.com", result.email()),
                () -> assertEquals("32647586", result.documentNumber()),
                () -> assertEquals("Luis", result.firstName()),
                () -> assertEquals("Montalvo", result.lastName()),
                () -> assertEquals("123456789", result.phone()),
                () -> assertTrue(result.active()),
                () -> assertEquals(now, result.createdAt())
        );
    }

    //GET ALL
    @Test
    @DisplayName("Should return 3 guests with active true")
    void shouldReturnAllGuestsDetails_WhenActiveIsTrue() {
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime now = LocalDateTime.now();

        Guest guest1 = Guest.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber("32647586")
                .createdAt(now)
                .active(true)
                .build();
        Guest guest2 = Guest.builder()
                .id(2L)
                .firstName("Mario")
                .lastName("Perez")
                .email("marioperez@mail.com")
                .phone("123456688")
                .documentNumber("32127586")
                .createdAt(now)
                .active(true)
                .build();
        Guest guest3 = Guest.builder()
                .id(3L)
                .firstName("Miguel")
                .lastName("Ruiz")
                .email("miguelruiz@mail.com")
                .phone("128456712")
                .documentNumber("98647586")
                .createdAt(now)
                .active(true)
                .build();

        Page<Guest> page = new PageImpl<>(List.of(guest1, guest2, guest3));

        when(repository.findByActiveTrue(pageable)).thenReturn(page);

        Page<GuestResponse> result = service.getActiveGuests(pageable);

        assertAll("Should return 3 guests",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.getTotalElements(), "Page must containt 3 guests"),

                () -> assertTrue(result.getContent().stream()
                        .allMatch(g -> g.active().equals(true)), "Active should be true for all guests"),

                () -> assertEquals("Luis", result.getContent().getFirst().firstName()),
                () -> assertEquals("Mario", result.getContent().get(1).firstName()),
                () -> assertEquals("Miguel", result.getContent().getLast().firstName())
        );
    }

    //GET BY  DOCUMENT
    @Test
    @DisplayName("Should return GuestDetails when Document number exists")
    void shouldReturnGuestDetails_WhenDocumentNumberExists() {
        LocalDateTime now = LocalDateTime.now();
        String document = "32647586";

        Guest guest1 = Guest.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber(document)
                .createdAt(now)
                .active(true)
                .build();

        when(repository.findByDocumentNumber(document)).thenReturn(Optional.of(guest1));

        GuestResponse result = service.getGuestByDocumentNumber(document);

        assertAll("Guest validation",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.id()),
                () -> assertEquals("luismontalvo@mail.com", result.email()),
                () -> assertEquals(document, result.documentNumber()),
                () -> assertEquals("Luis", result.firstName()),
                () -> assertEquals("Montalvo", result.lastName()),
                () -> assertEquals("123456789", result.phone()),
                () -> assertTrue(result.active()),
                () -> assertEquals(now, result.createdAt())
        );
        verify(repository, times(1)).findByDocumentNumber(document);

    }

    @Test
    @DisplayName("Should create a guest")
    void shouldCreateAGuest() {
        LocalDateTime now = LocalDateTime.now();

        CreateGuestRequest request = CreateGuestRequest.builder()
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber("85768437")
                .build();

        Guest savedGuest = Guest.builder()
                .id(1L)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .documentNumber(request.documentNumber())
                .active(true)
                .createdAt(now)
                .build();

        when(repository.save(any(Guest.class))).thenReturn(savedGuest);

        GuestResponse response = service.createGuest(request);

        assertAll("Validate Requested Guest",
                () -> assertNotNull(response),
                () -> assertEquals(1L, response.id()),
                () -> assertEquals(request.firstName(), response.firstName()),
                () -> assertEquals(request.lastName(), response.lastName()),
                () -> assertEquals(request.email(), response.email()),
                () -> assertEquals(request.documentNumber(), response.documentNumber()),
                () -> assertTrue(response.active(), "Guest should be active"),
                () -> assertNotNull(response.createdAt(), "Creation date shouldn't be null"),
                () -> assertEquals(now, response.createdAt())
        );
        verify(repository, times(1)).save(any(Guest.class));
    }

    @Test
    @DisplayName("Should update guest details when guest exists")
    void shouldUpdateGuestDetails(){
        LocalDateTime now = LocalDateTime.now();

        UpdateGuestRequest request = UpdateGuestRequest.builder()
                .firstName("Miguel")
                .lastName("Montalvos")
                .email("miguelmontalvos@gmail.com")
                .phone("960785647").build();

        Guest guest = Guest.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber("32647586")
                .createdAt(now)
                .active(true)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(guest));
        when(repository.existsByEmail("miguelmontalvos@gmail.com")).thenReturn(false);
        when(repository.save(any(Guest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GuestResponse response = service.updateGuest(1L, request);

        assertAll("Validate updated guest",
                () -> assertNotNull(response),
                () -> assertEquals(1L, response.id()),
                () -> assertEquals("Miguel", response.firstName()),
                () -> assertEquals("Montalvos", response.lastName()),
                () -> assertEquals("miguelmontalvos@gmail.com", response.email()),
                () -> assertEquals("960785647", response.phone()),
                () -> assertEquals("32647586", response.documentNumber(), "Document number should not change"),
                () -> assertTrue(response.active()),
                () -> assertEquals(now, response.createdAt())
        );

        verify(repository).findById(1L);
        verify(repository).existsByEmail("miguelmontalvos@gmail.com");
        verify(repository).save(any(Guest.class));
    }

    @Test
    @DisplayName("Should soft-delete guest setting active to false")
    void shouldSoftDeleteGuest_WhenIdExists(){
        LocalDateTime now = LocalDateTime.now();

        Guest guest = Guest.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber("32647586")
                .createdAt(now)
                .active(true)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(guest));

        GuestResponse response = service.deleteGuest(1L);

        assertAll("Validate deleted guest",
                () -> assertNotNull(response),
                () -> assertEquals(1L, response.id()),
                () -> assertFalse(response.active(), "Guest should be inactive after delete"),
                () -> assertFalse(guest.getActive(), "Entity should also be inactive"),
                () -> assertEquals("Luis", response.firstName())
        );
        verify(repository).findById(1L);
        verify(repository, never()).delete(any());
    }
    @Test
    @DisplayName("Should soft-delete guest setting active to true")
    void shouldEnableGuest_WhenIdExists(){
        LocalDateTime now = LocalDateTime.now();

        Guest guest = Guest.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Montalvo")
                .email("luismontalvo@mail.com")
                .phone("123456789")
                .documentNumber("32647586")
                .createdAt(now)
                .active(false)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(guest));
        when(repository.save(any(Guest.class))).thenAnswer(i -> i.getArgument(0));

        GuestResponse response = service.enableGuest(1L);

        assertAll("Validate deleted guest",
                () -> assertNotNull(response),
                () -> assertEquals(1L, response.id()),
                () -> assertTrue(response.active(), "Guest should be active after enable"),
                () -> assertTrue(guest.getActive(), "Entity should be active"),
                () -> assertEquals("Luis", response.firstName())
        );
        verify(repository).findById(1L);
        verify(repository).save(any(Guest.class));
    }
}
