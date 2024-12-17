package com.shopify.service;

import com.shopify.entity.User;
import com.shopify.exception.NotFoundException;
import com.shopify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private SecurityContextHolder securityContextHolder;
    private UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotFoundException("User not found");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(NotFoundException::new);
    }

}
