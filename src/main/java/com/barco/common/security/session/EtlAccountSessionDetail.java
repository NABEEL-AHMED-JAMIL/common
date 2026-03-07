package com.barco.common.security.session;

import com.barco.common.utility.BarcoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EtlAccountSessionDetail implements UserDetails, Serializable  {

    private Long id;
    private String uuid;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String ipAddress;
    private Boolean orgAccount = false;
    private Boolean isSystem = false;
    private String accountProfile;
    private OrganizationDetail organization;
    // Role privileges/permissions as simple strings (e.g. "ROLE_ADMIN", "PRIV_CREATE").
    // This can be used to easily set authorities without needing to create GrantedAuthority objects upfront.
    private Collection<String> privileges = Collections.emptyList();
    // Ensure a safe default to avoid NPEs when security frameworks call getAuthorities()
    private Collection<? extends GrantedAuthority> authorities = Collections.emptyList();

    public EtlAccountSessionDetail() {}

    public Long getId() {
        return id;
    }

    public EtlAccountSessionDetail setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public EtlAccountSessionDetail setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public EtlAccountSessionDetail setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public EtlAccountSessionDetail setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Mark account as valid by default. Returning false previously caused the account to be treated
    // as expired/locked/disabled and prevented method-based security checks from working as expected.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public EtlAccountSessionDetail setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public EtlAccountSessionDetail setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public EtlAccountSessionDetail setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public EtlAccountSessionDetail setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public Boolean getOrgAccount() {
        return orgAccount;
    }

    public EtlAccountSessionDetail setOrgAccount(Boolean orgAccount) {
        this.orgAccount = orgAccount;
        return this;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public EtlAccountSessionDetail setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
        return this;
    }

    public String getAccountProfile() {
        return accountProfile;
    }

    public EtlAccountSessionDetail setAccountProfile(String accountProfile) {
        this.accountProfile = accountProfile;
        return this;
    }

    public OrganizationDetail getOrganization() {
        return organization;
    }

    public EtlAccountSessionDetail setOrganization(OrganizationDetail organization) {
        this.organization = organization;
        return this;
    }

    /* Spring Security method - returns the authorities (roles/permissions) granted to the user. This is used by Spring Security
     * for authorization checks. The authorities collection should contain GrantedAuthority objects representing the user's roles and permissions.
     * If authorities are not set, it returns an empty collection to avoid NPEs in security checks.
     * */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return BarcoUtil.isNull(this.authorities) ? Collections.emptyList() : this.authorities;
    }

    /**
     * Set authorities for this session detail. This method allows setting authorities directly as GrantedAuthority objects,
     * which is the standard way Spring Security expects authorities to be provided.
     * @param authorities Collection of GrantedAuthority objects to set for this session detail.
     * */
    public EtlAccountSessionDetail setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = BarcoUtil.isNull(authorities) ? Collections.emptyList() : authorities;
        return this;
    }

    /**
     * Privileges accessor methods - similar helpers as provided for authorities.
     */
    public Collection<String> getPrivileges() {
        return BarcoUtil.isNull(this.privileges) ? Collections.emptyList() : this.privileges;
    }

    /**
     * Set privileges as a collection of simple strings (e.g. "PRIV_CREATE"). This is a convenience method
     * to allow setting privileges without needing to create GrantedAuthority objects upfront.
     * @param privileges Collection of privilege strings to set for this session detail.
     */
    public void setPrivileges(Collection<String> privileges) {
        this.privileges = BarcoUtil.isNull(privileges) ? Collections.emptyList() : privileges;
    }

}