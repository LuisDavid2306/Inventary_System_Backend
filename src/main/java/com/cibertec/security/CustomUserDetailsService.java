package com.cibertec.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cibertec.entity.User;
import com.cibertec.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 👈 BUSCAR POR EMAIL
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return org.springframework.security.core.userdetails.User
            .builder()
            .username(user.getEmail())  // 👈 USAR EMAIL COMO USERNAME
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}
