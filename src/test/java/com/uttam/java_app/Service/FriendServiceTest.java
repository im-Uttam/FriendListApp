package com.uttam.java_app.service;

import com.uttam.java_app.model.Friend;
import com.uttam.java_app.model.User;
import com.uttam.java_app.repository.FriendRepository;
import com.uttam.java_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendService friendService;

    private User user;
    private Friend friend;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        friend = new Friend();
        friend.setId(1L);
        friend.setFriendName("John Doe");
        friend.setUser(user);
    }

    @Test
    void addFriend_ShouldReturnSavedFriend() {
        // Arrange
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(friendRepository.save(any(Friend.class))).thenReturn(friend);

        // Act
        Friend result = friendService.addFriend(friend, user);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getFriendName());
        assertEquals(user, result.getUser());
        verify(userRepository).getReferenceById(user.getId());
        verify(friendRepository).save(friend);
    }

    @Test
    void getFriends_ShouldReturnList() {
        // Arrange
        List<Friend> friends = Collections.singletonList(friend);
        when(friendRepository.findByUser(user)).thenReturn(friends);

        // Act
        List<Friend> result = friendService.getFriends(user);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getFriendName());
        verify(friendRepository).findByUser(user);
    }

    @Test
    void deleteFriend_ShouldCallRepository() {
        // Act
        friendService.deleteFriend(1L, user);

        // Assert
        verify(friendRepository).deleteByIdAndUser(1L, user);
    }

    @Test
    void getFriendByIdAndUser_ShouldReturnFriend_WhenFound() {
        // Arrange
        when(friendRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(friend));

        // Act
        Friend result = friendService.getFriendByIdAndUser(1L, user);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getFriendName());
        verify(friendRepository).findByIdAndUser(1L, user);
    }

    @Test
    void getFriendByIdAndUser_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(friendRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendService.getFriendByIdAndUser(1L, user);
        });

        assertEquals("Friend not found", exception.getMessage());
        verify(friendRepository).findByIdAndUser(1L, user);
    }
}