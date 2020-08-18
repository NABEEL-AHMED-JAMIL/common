package com.barco.common.filter;

import com.barco.common.security.TokenHelper;
import com.barco.common.utility.BarcoUtil;
import com.barco.common.utility.ExceptionUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    private String USERNAME = "userName";

    @Autowired
    public TokenHelper tokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        String authToken = this.tokenHelper.getToken(httpServletRequest);
        if (authToken != null) {
            try {
                String requestJson = this.tokenHelper.getUsernameFromToken(authToken);
                if (requestJson != null) {
                    logger.debug("Verify AppUser Detail With Token.");
                    JsonParser parser = new JsonParser();
                    JsonObject mainObject = parser.parse(requestJson).getAsJsonObject();
                    if(BarcoUtil.hasKeyValue(mainObject, USERNAME)) {
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(
                                this.userDetailsService.loadUserByUsername(mainObject.get(USERNAME).getAsString()));
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception ex) {
                logger.error("Error " + ExceptionUtil.getRootCauseMessage(ex));
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
