package se.jensen.saman.socialnetworkmaven.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.service.UserService;
import se.jensen.saman.socialnetworkmaven.util.JwtUtil;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String token = authHeader.substring(7);
           try{ String username = jwtUtil.extractUsername(token);


            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getFindByUsernameMethod(username);

                if (jwtUtil.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
                            null,
                            user.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }

            }catch (ExpiredJwtException exception){
               System.out.println("Token has expired");
           }
           catch (Exception exception){
               System.out.println("Invalid token, unable to Extract JWT Data.");
           }
        }
        chain.doFilter(request, response);
    }
}