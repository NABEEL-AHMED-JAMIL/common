package com.barco.common.filter;

import com.barco.common.security.AnonAuthentication;
import com.barco.common.security.TokenHelper;
import com.barco.common.utility.BarcoUtil;
import com.barco.common.utility.ExceptionUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.lang.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nabeel Ahmed
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public Logger logger = LogManager.getLogger(TokenAuthenticationFilter.class);

    /*
     * The below paths will get ignored by the filter
     */
    public static final String ROOT_MATCHER = "/";
    public static final String FAVICON_MATCHER = "/favicon.ico";
    public static final String HTML_MATCHER = "/**/*.html";
    public static final String CSS_MATCHER = "/**/*.css";
    public static final String JS_MATCHER = "/**/*.js";
    public static final String IMG_MATCHER = "/images/*";

    private final List<String> pathsToSkip = Arrays.asList(ROOT_MATCHER, HTML_MATCHER, FAVICON_MATCHER,
            CSS_MATCHER, JS_MATCHER, IMG_MATCHER, "/v2/api-docs", "/swagger-resources",
            "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html",
            "/webjars/**","/public.json/**","/auth.json/**"
    );

    private String USERNAME = "userName";

    @Autowired
    public TokenHelper tokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        // get token from the request
        String authToken = this.tokenHelper.getToken(httpServletRequest);
        if (authToken != null) {
            try {
                String requestJson = this.tokenHelper.getUsernameFromToken(authToken);
                if (requestJson != null) {
                    logger.debug("Verify AppUser Detail With Token.");
                    JsonParser parser = new JsonParser();
                    JsonObject mainObject = parser.parse(requestJson).getAsJsonObject();
                    if (BarcoUtil.hasKeyValue(mainObject, USERNAME)) {
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(
                            this.userDetailsService.loadUserByUsername(mainObject.get(USERNAME).getAsString()));
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception ex) {
                logger.error("Error " + ExceptionUtil.getRootCauseMessage(ex));
                SecurityContextHolder.getContext().setAuthentication(new AnonAuthentication());
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private boolean skipPathRequest(HttpServletRequest request, List<String> pathsToSkip) {
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> {
            return new AntPathRequestMatcher(path);
        }).collect(Collectors.toList());
        OrRequestMatcher matchers = new OrRequestMatcher(m);
        return matchers.matches(request);
    }

}
