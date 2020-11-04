package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.scrumban.model.AuthRequest;
import pl.utp.scrumban.util.JwtUtil;

@RestController
public class WelcomeController {

    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;

    @Autowired
    public WelcomeController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome to my site!!!";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getEmail());
    }
}
