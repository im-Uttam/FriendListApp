package com.uttam.java_app.service;
import com.uttam.java_app.model.Friend;
import com.uttam.java_app.model.User;
import com.uttam.java_app.repository.FriendRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class FriendService {

    private final FriendRepository friendRepository;

    public Friend addFriend(Friend friend, User user) {
        friend.setUser(user);
        return friendRepository.save(friend);
    }

    public List<Friend> getFriends(User user) {
        return friendRepository.findByUser(user);
    }

    public void deleteFriend(Long friendId, User user) {
        friendRepository.deleteByIdAndUser(friendId, user);
    }

    public Friend getFriendByIdAndUser(Long id, User user) {
        return friendRepository.findByUser(user).stream()
            .filter(f -> f.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Friend not found"));
    }
}