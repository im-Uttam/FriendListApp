package com.uttam.java_app.service;

import com.uttam.java_app.model.User;
import com.uttam.java_app.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Called after Google login — saves user if first time
    public User saveOrUpdateUser(OAuth2User oauth2User) {
        String email    = oauth2User.getAttribute("email");
        String name     = oauth2User.getAttribute("name");
        String googleId = oauth2User.getAttribute("sub");
        String picture  = oauth2User.getAttribute("picture");

        return userRepository.findByEmail(email)
            .map(existingUser -> {
                existingUser.setName(name);
                existingUser.setProfilePicture(picture);
                return userRepository.save(existingUser);
            })
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setGoogleId(googleId);
                newUser.setProfilePicture(picture);
                return userRepository.save(newUser);
            });
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}