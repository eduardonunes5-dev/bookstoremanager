package com.eduardonunes.bookstoremanager.users.service;

import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import com.eduardonunes.bookstoremanager.users.dto.JwtRequest;
import com.eduardonunes.bookstoremanager.users.dto.JwtResponse;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private JwtTokenManager jwtTokenManager;

    public AuthenticationService(UserRepository userRepository, @Lazy AuthenticationManager authenticationManager, JwtTokenManager jwtTokenManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    public JwtResponse createAuthenticationToken(JwtRequest jwtRequest){
        String username = jwtRequest.getUsername();

        authenticate(username, jwtRequest.getPassword());

        UserDetails userDetails = this.loadUserByUsername(username);
        String token = jwtTokenManager.generateToken(userDetails);
        return JwtResponse.builder()
                .jwtToken(token).build();
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User with username %s not found", username)));
        return new AuthenticatedUser(
                user.getUsername(),
                user.getPassword(),
                user.getRole().getDescription()
        );
    }
}
