// ...existing code...
package com.mts.spotmerest.services;

import com.mts.spotmerest.mappers.GymDAO;
import com.mts.spotmerest.models.Gym;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GymServicesTest {

    @Mock
    private GymDAO gymDAO;

    private GymService gymService;

    @BeforeEach
    void setUp() {
        gymService = new GymService(gymDAO);
    }

    private Gym makeGym(Long id, String gymId) {
        Gym g = new Gym();
        g.setId(id);
        g.setGymId(gymId);
        return g;
    }

    @Test
    void testGetGymByIdFound() {
        Gym g = makeGym(1L, "ext-1");
        when(gymDAO.findGymById(1L)).thenReturn(Optional.of(g));

        Optional<Gym> res = gymService.getGymById(1L);

        assertTrue(res.isPresent());
        assertEquals(1L, res.get().getId());
        assertEquals("ext-1", res.get().getGymId());
        verify(gymDAO).findGymById(1L);
    }

    @Test
    void testGetGymByIdNotFound() {
        when(gymDAO.findGymById(99L)).thenReturn(Optional.empty());

        Optional<Gym> res = gymService.getGymById(99L);

        assertFalse(res.isPresent());
        verify(gymDAO).findGymById(99L);
    }

    @Test
    void testAddNewGymSuccess() {
        Gym newGym = makeGym(2L, "ext-2");
        when(gymDAO.findGymById(2L)).thenReturn(Optional.empty());

        gymService.addNewGym(newGym);

        verify(gymDAO).findGymById(2L);
        verify(gymDAO).save(newGym);
    }

    @Test
    void testAddNewGymDuplicateThrows() {
        Gym existing = makeGym(3L, "ext-3");
        when(gymDAO.findGymById(3L)).thenReturn(Optional.of(existing));

        Gym toAdd = makeGym(3L, "ext-3");
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> gymService.addNewGym(toAdd));
        assertNotNull(ex.getMessage());
        verify(gymDAO).findGymById(3L);
        verify(gymDAO, never()).save(ArgumentMatchers.any());
    }

    @Test
    void testDeleteGymSuccess() {
        when(gymDAO.existsById(4L)).thenReturn(true);

        gymService.deleteGym(4L);

        verify(gymDAO).existsById(4L);
        verify(gymDAO).deleteById(4L);
    }

    @Test
    void testDeleteGymNotFoundThrows() {
        when(gymDAO.existsById(5L)).thenReturn(false);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> gymService.deleteGym(5L));
        assertTrue(ex.getMessage().contains("does not exist"));
        verify(gymDAO).existsById(5L);
        verify(gymDAO, never()).deleteById(5L);
    }

    @Test
    void testGetGymsReturnsAll() {
        Gym a = makeGym(6L, "ext-6");
        Gym b = makeGym(7L, "ext-7");
        List<Gym> list = Arrays.asList(a, b);
        when(gymDAO.findAll()).thenReturn(list);

        List<Gym> res = gymService.getGyms();

        assertEquals(2, res.size());
        assertTrue(res.contains(a));
        assertTrue(res.contains(b));
        verify(gymDAO).findAll();
    }
}