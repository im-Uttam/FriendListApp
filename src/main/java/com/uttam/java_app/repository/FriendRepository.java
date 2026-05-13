package com.uttam.java_app.repository;

import com.uttam.java_app.model.Friend;
import com.uttam.java_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUser(User user);
    void deleteByIdAndUser(Long id, User user);
}