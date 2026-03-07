package com.barco.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import com.barco.common.security.session.EtlAccountSessionDetail;

/**
 * TokenBasedAuthentication is a custom implementation of AbstractAuthenticationToken that represents an authentication token based on a user details principle and a token string.
 * It is used to encapsulate the authentication information for a user, including their authorities and credentials
 * @author Nabeel Ahmed
 */
public class TokenBasedAuthentication extends AbstractAuthenticationToken {

    private String token;
    private String userId;
    private String tenantId;
    private final EtlAccountSessionDetail principle;

    public TokenBasedAuthentication(String token, EtlAccountSessionDetail principle) {
        super(principle.getAuthorities());
        this.token = token;
        this.principle = principle;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principle;
    }

    @Override
    public String toString() {
        return "TokenBasedAuthentication{" +
            "tenantId='" + tenantId + '\'' +
            ", userId='" + userId + '\'' +
        '}';
    }
}