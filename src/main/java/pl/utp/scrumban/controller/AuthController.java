package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.exception.InvalidCredentialsException;
import pl.utp.scrumban.request.AuthRequest;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.service.JwtService;
import pl.utp.scrumban.service.UserService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private JwtService jwtService;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager,
                          UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new InvalidCredentialsException("Invalid credential", ex);
        }
        return jwtService.generateToken(authRequest.getEmail());
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setRegistrationDate(LocalDate.now());

        user = userService.createUser(user);

        if (user != null) {
            user.setPassword(password);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/isEmailUsed/{email}")
    public boolean isEmailUsed(@PathVariable("email") String email) {
        return userService.getUserByEmail(email) == null;
    }

}
