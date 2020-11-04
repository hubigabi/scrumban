package pl.utp.scrumban.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.repositiory.UserRepository;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    // Email is username
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
