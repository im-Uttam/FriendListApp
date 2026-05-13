package com.uttam.java_app.controller;

import com.uttam.java_app.model.Friend;
import com.uttam.java_app.model.User;
import com.uttam.java_app.service.FriendService;
import com.uttam.java_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final FriendService friendService;
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User principal, Model model) {
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
            model.addAttribute("email", principal.getAttribute("email"));
            model.addAttribute("picture", principal.getAttribute("picture"));
        }
        return "dashboard";
    }

    @GetMapping("/friends-page")
    public String friendsPage(@AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = userService.getUserByEmail(principal.getAttribute("email"));
        model.addAttribute("friends", friendService.getFriends(user));
        return "friends";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = userService.getUserByEmail(principal.getAttribute("email"));
        model.addAttribute("user", user);
        return "profile";
    }

    // --- Friend Management UI ---

    @GetMapping("/friend-new")
    public String showAddFriendForm(Model model) {
        model.addAttribute("friend", new Friend());
        return "friend-form";
    }

    @GetMapping("/friend-edit/{id}")
    public String showEditFriendForm(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal, Model model) {
        User user = userService.getUserByEmail(principal.getAttribute("email"));
        Friend friend = friendService.getFriendByIdAndUser(id, user);
        model.addAttribute("friend", friend);
        return "friend-form";
    }

    @PostMapping("/friend-save")
    public String saveFriend(@ModelAttribute("friend") Friend friend, 
                             @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getUserByEmail(principal.getAttribute("email"));
        friendService.addFriend(friend, user); 
        return "redirect:/friends-page";
    }

    @GetMapping("/friend-delete/{id}")
    public String deleteFriend(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getUserByEmail(principal.getAttribute("email"));
        friendService.deleteFriend(id, user);
        return "redirect:/friends-page";
    }
}
