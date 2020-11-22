package pl.utp.scrumban.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.utp.scrumban.service.UserDetailsServiceImpl;
import pl.utp.scrumban.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final String TOKEN_NAME_COOKIE = "jwt-token=";

    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public JwtFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String userName = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                userName = jwtService.extractUsername(token);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Could not extract username from token");
            }
        } else {
            String cookieHeader = httpServletRequest.getHeader("cookie");
            if (cookieHeader != null && cookieHeader.contains(TOKEN_NAME_COOKIE)) {
                String[] pairs = cookieHeader.split("; ");
                String t = Stream.of(pairs)
                        .filter(s -> s.startsWith(TOKEN_NAME_COOKIE))
                        .map(s -> s.substring(TOKEN_NAME_COOKIE.length()))
                        .findFirst().orElse(null);

                if (t != null && t.length() > 0) {
                    token = t;
                    try {
                        userName = jwtService.extractUsername(token);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Could not extract username from token");
                    }
                }
            }
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);

                if (jwtService.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Could not authenticate a user: " + userName);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}