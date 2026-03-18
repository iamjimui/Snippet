package com.quest.etna.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.gson.Gson;
import com.quest.etna.model.ErrorResponse;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.UserDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtService;
    private final JwtUserDetailsService userDetailsService;
    private Gson gson = new Gson();

    public JwtRequestFilter(JwtTokenUtil jwtService, JwtUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        try{

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                if(request.getMethod().equals("GET") ||
                		request.getMethod().equals("PUT") ||
                		request.getMethod().equals("DELETE") ||
                		request.getMethod().equals("POST") &&
                		(!request.getRequestURI().toString().equals("/authenticate") &&
                		!request.getRequestURI().toString().equals("/register"))) {
                    response.setStatus(401);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    ErrorResponse error = new ErrorResponse("Bad request");
                    var json = this.gson.toJson(error);
                    response.getWriter().write(json);
                    return;
                }

            } else {
            	jwt = authHeader.substring(7);
                var x = JWT.decode(jwt);
                userName = jwtService.getUsernameFromToken(jwt);
                System.out.println(userName);

                if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    JwtUserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

                    if(jwtService.validateToken(jwt, userDetails)){
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (IOException e){
            response.setStatus(400);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ErrorResponse error = new ErrorResponse("Bad request");
            var json = this.gson.toJson(error);
            response.getWriter().write(json);
        } catch (JWTDecodeException e) {
            response.setStatus(401);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ErrorResponse error = new ErrorResponse("Bad token");
            var json = this.gson.toJson(error);
            response.getWriter().write(json);
        } catch (Exception e){
            if(!request.getMethod().equals("GET")) {
                response.setStatus(401);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                ErrorResponse error = new ErrorResponse("Bad or missing token");
                var json = this.gson.toJson(error);
                response.getWriter().write(json);
            }

        }

    }
}
