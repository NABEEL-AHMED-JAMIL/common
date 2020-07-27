package com.barco.common.filter;


import com.barco.common.security.TokenHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public Logger logger = LogManager.getLogger(TokenAuthenticationFilter.class);

    @Autowired
    public TokenHelper tokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        String authToken = this.tokenHelper.getToken(httpServletRequest);
        if (authToken != null) {
            String username = this.tokenHelper.getUsernameFromToken(authToken);
            if (username != null) {
                logger.debug("Verify AppUser Detail With Token.");
                TokenBasedAuthentication authentication = new TokenBasedAuthentication(
                        this.userDetailsService.loadUserByUsername(username));
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
