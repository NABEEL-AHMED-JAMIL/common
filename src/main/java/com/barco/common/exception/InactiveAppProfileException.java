package com.barco.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown when an EtlAccount's AppProfile exists but is not in ACTIVE status.
 * @author Nabeel Ahmed
 */
public class InactiveAppProfileException extends AuthenticationException {

    public InactiveAppProfileException(String msg) {
        super(msg);
    }

    public InactiveAppProfileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

