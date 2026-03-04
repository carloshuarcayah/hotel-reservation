package pe.com.hotel.reservation.room;

import jakarta.persistence.EntityNotFoundException;
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
import pe.com.hotel.reservation.room.dto.RoomResponse;
import pe.com.hotel.reservation.room.dto.UpdateRoomRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)      // JUnit 5 + Mockito
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    // GET ROOM BY ID
    @Test
    @DisplayName("Should throw exception when room not found")
    void getRoomById_NotFound() {
        when(roomRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> roomService.getRoomById(99L));
    }
    @Test
    @DisplayName("Should return a room when found by id")
    void getRoomById_Ok(){
        Room room = Room.builder()
                .id(1L)
                .roomNumber("101")
                .roomType(RoomType.SINGLE)
                .pricePerNight(new BigDecimal("80.00"))
                .capacity(2)
                .floor(1)
                .status(RoomStatus.AVAILABLE)
                .active(true)
                .build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        RoomResponse response = roomService.getRoomById(1L);


        //verify not null response
        assertNotNull(response);

        //iquals ids
        assertEquals(1L, response.id());

        //equals room number
        assertEquals("101", response.roomNumber());

        //equals type room
        assertEquals(RoomType.SINGLE, response.roomType());
    }

    //  GET ROOMS
    @Test
    @DisplayName("Should return a List of 2 element with active true")
    void getAllRoomsByActiveTrue_ReturnsPage(){
        Pageable pageable = PageRequest.of(0,10);

        Room room1 = Room.builder()
                .id(1L).
                roomNumber("A101").
                roomType(RoomType.SINGLE).
                pricePerNight(new BigDecimal("80.00")).
                capacity(1).
                floor(1).
                description("Room for one person").
                status(RoomStatus.AVAILABLE)
                .active(true)
                .build();

        Room room2 = Room.builder()
                .id(2L).
                roomNumber("A102").
                roomType(RoomType.SINGLE).
                pricePerNight(new BigDecimal("80.00")).
                capacity(1).
                floor(1).
                description("Room for one person").
                status(RoomStatus.AVAILABLE)
                .active(true)
                .build();

        Page<Room> page = new PageImpl<>(List.of(room1,room2));

        when(roomRepository.findByActiveTrue(pageable)).thenReturn(page);

        Page<RoomResponse> result = roomService.getActiveRooms(pageable);

        assertNotNull(result,"result should not be null");
        assertEquals(2,result.getTotalElements());
        assertEquals("A101",result.getContent().getFirst().roomNumber());
        //todo: el RoomResponse no devuelve el active
        verify(roomRepository, times(1)).findByActiveTrue(pageable);
    }

    //  UPDATE ROOM
    @Test
    @DisplayName("Should update room when found")
    void updateRoom_Exists_ReturnsUpdated() {
        Room room = Room.builder()
                .id(1L)
                .roomNumber("101")
                .roomType(RoomType.SINGLE)
                .pricePerNight(new BigDecimal("80.00"))
                .capacity(2)
                .floor(1)
                .status(RoomStatus.AVAILABLE)
                .active(true)
                .build();

        UpdateRoomRequest request = new UpdateRoomRequest(
                RoomType.PREMIUM,
                new BigDecimal("120.00"),
                4,
                1,
                "Cuarto Premium"
        );

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.save(room)).thenReturn(room);

        RoomResponse response= roomService.updateRoom(1L,request);

        assertNotNull(response);
        assertEquals(RoomType.PREMIUM, response.roomType());
        assertEquals(new BigDecimal("120.00"), response.pricePerNight());
        assertEquals(4, response.capacity());
    }
    @Test
    @DisplayName("Should throw exception when updating non-existent room")
    void updateRoom_NotFound_ThrowsException() {
        UpdateRoomRequest request = new UpdateRoomRequest(
                RoomType.DOUBLE,
                new BigDecimal("120.00"),
                3,
                1,
                "Renovated room"
        );

        when(roomRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> roomService.updateRoom(999L, request));
    }

    //  DELETE ROOM
    @Test
    @DisplayName("Should delete room when found")
    void deleteRoom_NoActiveReservations_SetsInactive() {
        Room room = Room.builder()
                .id(1L).
                roomNumber("A101").
                roomType(RoomType.SINGLE).
                pricePerNight(new BigDecimal("80.00")).
                capacity(1).
                floor(1).
                description("Room for one person").
                status(RoomStatus.AVAILABLE)
                .active(true)
                .build();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        roomService.deleteRoom(1L);
        assertFalse(room.getActive());
        verify(roomRepository).findById(1L);
    }
    @Test
    @DisplayName("Should thrown exception when deleting non-existing room")
    void deleteRoom_NotFound_ThrowsException() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                ()->roomService.deleteRoom(999L));
    }


}
