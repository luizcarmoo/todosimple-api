package com.luizcarmo.todosimple.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.luizcarmo.todosimple.models.User;
import com.luizcarmo.todosimple.repositories.UserRepository;
import com.luizcarmo.todosimple.services.UserService;
import com.luizcarmo.todosimple.services.exceptions.ObjectNotFoundException;
import com.luizcarmo.todosimple.services.exceptions.AuthorizationException;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;
    
    User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    public void testCreateUser() {
        when(repository.save(user)).thenReturn(user);
        User createdUser = service.create(user);
        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
        verify(repository, times(1)).save(user);
    }

    @Test
    public void testFindUserById() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = service.findById(1L);
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    public void testFindUserByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ObjectNotFoundException.class, () -> service.findById(1L));
        assertEquals("User not found! Id: 1, Type: class com.luizcarmo.todosimple.models.User", exception.getMessage());
    }

    @Test
    public void testUpdateUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);
        user.setPassword("newpassword");
        User updatedUser = service.update(user);
        assertEquals("newpassword", updatedUser.getPassword());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testUnauthorizedAccess() {
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(null);
        Exception exception = assertThrows(AuthorizationException.class, () -> service.findById(1L));
        assertEquals("Access denied!", exception.getMessage());
    }
}
