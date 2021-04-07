package pl.utp.scrumban.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.utp.scrumban.model.User;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class OAuthService {

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";
    private final String FACEBOOK_TOKEN_URL = "https://graph.facebook.com/me?fields=email,name&access_token=";

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OAuthService(UserService userService, JwtService jwtService,
                        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String loginWithGoogle(String idToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = GOOGLE_TOKEN_URL + idToken;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(Objects.requireNonNull(response.getBody()));
        String email = jsonNode.get("email").asText();

        if (userService.getUserByEmail(email) == null) {
            String name = jsonNode.get("name").asText();
            User user = new User(email, name, passwordEncoder.encode(""), LocalDate.now());
            userService.createUser(user);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtService.generateToken(email);
    }

    public String loginWithFacebook(String authToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = FACEBOOK_TOKEN_URL + authToken;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(Objects.requireNonNull(response.getBody()));
        String email = jsonNode.get("email").asText();

        if (userService.getUserByEmail(email) == null) {
            String name = jsonNode.get("name").asText();
            User user = new User(email, name, passwordEncoder.encode(""), LocalDate.now());
            userService.createUser(user);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtService.generateToken(email);
    }
}
