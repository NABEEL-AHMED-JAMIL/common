package com.barco.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when an AppRole exists but is not in ACTIVE status.
 * @author Nabeel Ahmed
 */
public class InactiveRoleException extends AuthenticationException {

    public InactiveRoleException(String msg) {
        super(msg);
    }

    public InactiveRoleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}