package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.exception.InvalidCredentialsException;
import pl.utp.scrumban.model.AuthRequest;
import pl.utp.scrumban.service.JwtService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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
}
