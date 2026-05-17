package com.uttam.java_app.service;

import com.uttam.java_app.model.Friend;
import com.uttam.java_app.model.User;
import com.uttam.java_app.repository.FriendRepository;
import com.uttam.java_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public Friend addFriend(Friend friend, User user) {
        User managedUser = userRepository.getReferenceById(user.getId());
        friend.setUser(managedUser);
        return friendRepository.save(friend);
    }

    @Transactional(readOnly = true)
    public List<Friend> getFriends(User user) {
        return friendRepository.findByUser(user);
    }

    @Transactional
    public void deleteFriend(Long friendId, User user) {
        friendRepository.deleteByIdAndUser(friendId, user);
    }

    @Transactional(readOnly = true)
    public Friend getFriendByIdAndUser(Long id, User user) {
    return friendRepository.findByIdAndUser(id, user)
            .orElseThrow(() -> new RuntimeException("Friend not found"));
    }
}
