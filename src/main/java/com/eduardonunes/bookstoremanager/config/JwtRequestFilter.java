package com.eduardonunes.bookstoremanager.config;

import com.eduardonunes.bookstoremanager.users.service.AuthenticationService;
import com.eduardonunes.bookstoremanager.users.service.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    JwtTokenManager jwtTokenManager;
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String username = "";
        String jwtToken = "";
        String reqTokenHeader = req.getHeader("Authorization");
        if(isTokenPresent(reqTokenHeader)){
            jwtToken = reqTokenHeader.substring(7);
            username = jwtTokenManager.getUsernameFromToken(jwtToken);
        } else{
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if(isUsernameInContext(username)){
            addUsernameToContext(req,username, jwtToken);
        }
        chain.doFilter(req,res);
    }

    private boolean isUsernameInContext(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void addUsernameToContext(HttpServletRequest req, String username, String jwtToken) {
        UserDetails userDetails = authenticationService.loadUserByUsername(username);
        if(jwtTokenManager.validateToken(jwtToken, userDetails)){
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    private boolean isTokenPresent(String reqTokenHeader) {
        return reqTokenHeader != null && reqTokenHeader.startsWith("Bearer ");
    }
}
