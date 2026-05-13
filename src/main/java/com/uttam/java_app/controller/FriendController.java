package com.uttam.java_app.controller;

import com.uttam.java_app.model.Friend;
import com.uttam.java_app.model.User;
import com.uttam.java_app.service.FriendService;
import com.uttam.java_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    // GET all friends of logged-in user
    @GetMapping
    public ResponseEntity<List<Friend>> getFriends(@AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getUserByEmail(principal.getAttribute("email"));
        return ResponseEntity.ok(friendService.getFriends(user));
    }

    // POST add a new friend
    @PostMapping
    public ResponseEntity<Friend> addFriend(
            @Valid @RequestBody Friend friend,
            @AuthenticationPrincipal OAuth2User principal) {

        User user = userService.getUserByEmail(principal.getAttribute("email"));
        Friend saved = friendService.addFriend(friend, user);
        return ResponseEntity.ok(saved);
    }

    // DELETE a friend
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFriend(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) {

        User user = userService.getUserByEmail(principal.getAttribute("email"));
        friendService.deleteFriend(id, user);
        return ResponseEntity.ok("Friend deleted!");
    }
}