package pl.utp.scrumban.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.utp.scrumban.dto.request.SignUpRequest;
import pl.utp.scrumban.exception.InvalidCredentialsException;
import pl.utp.scrumban.dto.request.AuthRequest;
import pl.utp.scrumban.model.User;
import pl.utp.scrumban.service.JwtService;
import pl.utp.scrumban.service.OAuthService;
import pl.utp.scrumban.service.UserService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final OAuthService oauthService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager,
                          UserService userService, OAuthService oauthService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.oauthService = oauthService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Validated AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            return jwtService.generateToken(authRequest.getEmail());
        } catch (Exception ex) {
            throw new InvalidCredentialsException("Invalid credential", ex);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody @Validated SignUpRequest signUpRequest) {
        Boolean isSignedUp = userService.signUp(signUpRequest);

        if (isSignedUp) {
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/isEmailFree/{email}")
    public boolean isEmailFree(@PathVariable("email") String email) {
        return userService.getUserByEmail(email) == null;
    }

    @PostMapping("/loginGoogle")
    public String loginWithGoogle(@RequestBody String idToken) throws Exception {
        return oauthService.loginWithGoogle(idToken);
    }

    @PostMapping("/loginFacebook")
    public String loginWithFacebook(@RequestBody String authToken) throws Exception {
        return oauthService.loginWithFacebook(authToken);
    }

}
