package com.barco.common.exception.handler;

import com.barco.common.exception.bean.ApiValidationError;
import com.barco.common.payload.APIResponse;
import com.barco.common.utility.BarcoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.validation.BindException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Global REST exception handler.
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Handles validation errors for method arguments annotated with @Valid.
     * @param ex the MethodArgumentNotValidException containing details about the validation failure
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status code to be returned
     * @param request the WebRequest that resulted in the exception
     * @return a ResponseEntity containing an APIResponse with details about the validation errors
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        LOGGER.debug("Validation error: MethodArgumentNotValidException", ex);
        // Extract field errors and global errors from the exception and convert them into a list of ApiValidationError objects
        List<ApiValidationError> subErrors = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (!BarcoUtil.isNullOrEmpty(fieldErrors)) {
            for (FieldError fe : fieldErrors) {
                subErrors.add(new ApiValidationError(fe.getObjectName(), fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()));
            }
        }
        // Extract global errors and convert them into ApiValidationError objects
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        if (!BarcoUtil.isNullOrEmpty(globalErrors)) {
            for (ObjectError ge : globalErrors) {
                subErrors.add(new ApiValidationError(ge.getObjectName(), null, null, ge.getDefaultMessage()));
            }
        }
        // Create an APIResponse with a bad request status and include the list of validation errors as the response body
        APIResponse<List<ApiValidationError>> apiResponse = APIResponse.badRequest("Validation failed", subErrors);
        apiResponse.setPath(getPath(request));
        return ResponseEntity.status(apiResponse.getReturnCode()).body(apiResponse);
    }

    /**
     * Handles validation errors for binding failures, such as when a request parameter cannot be bound to a method argument.
     * @param ex the BindException containing details about the binding failure
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status code to be returned
     * @param request the WebRequest that resulted in the exception
     * @return a ResponseEntity containing an APIResponse with details about the validation errors
     */
    @Override
    protected ResponseEntity<Object> handleBindException(
        BindException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        LOGGER.debug("Validation error: BindException", ex);
        // Extract field errors and global errors from the exception and convert them into a list of ApiValidationError objects
        List<ApiValidationError> subErrors = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (!BarcoUtil.isNullOrEmpty(fieldErrors)) {
            for (FieldError fe : fieldErrors) {
                subErrors.add(new ApiValidationError(fe.getObjectName(), fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()));
            }
        }
        // Extract global errors and convert them into ApiValidationError objects
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        if (!BarcoUtil.isNullOrEmpty(globalErrors)) {
            for (ObjectError ge : globalErrors) {
                subErrors.add(new ApiValidationError(ge.getObjectName(), null, null, ge.getDefaultMessage()));
            }
        }
        // Create an APIResponse with a bad request status and include the list of validation errors as the response body
        APIResponse<List<ApiValidationError>> apiResponse = APIResponse.badRequest("Validation failed", subErrors);
        apiResponse.setPath(getPath(request));
        return ResponseEntity.status(apiResponse.getReturnCode()).body(apiResponse);
    }

    /**
     * Handles exceptions that occur when the request body contains malformed JSON.
     * @param ex the HttpMessageNotReadableException containing details about the parsing failure
     * @param headers the HTTP headers to be included in the response
     * @param status the HTTP status code to be returned
     * @param request the WebRequest that resulted in the exception
     * @return a ResponseEntity containing an APIResponse with details about the error
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request) {
        LOGGER.debug("Malformed JSON request", ex);
        String msg = "Malformed JSON request" + (!BarcoUtil.isNull(ex.getMostSpecificCause()) ? ": " + ex.getMostSpecificCause().getMessage() : "");
        APIResponse<?> apiResponse = APIResponse.badRequest(msg);
        apiResponse.setPath(getPath(request));
        return ResponseEntity.status(apiResponse.getReturnCode()).body(apiResponse);
    }

    /**
     * Handles validation errors for constraints defined on method parameters, such as those annotated with @NotNull or @Size.
     * @param ex the ConstraintViolationException containing details about the constraint violations
     * @param request the WebRequest that resulted in the exception
     * @return a ResponseEntity containing an APIResponse with details about the validation errors
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
        ConstraintViolationException ex,
        WebRequest request) {
        LOGGER.debug("Validation error: ConstraintViolationException", ex);
        // Extract constraint violations from the exception and convert them into a list of ApiValidationError objects
        List<ApiValidationError> subErrors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String object = !BarcoUtil.isNull(violation.getRootBeanClass()) ? violation.getRootBeanClass().getSimpleName() : null;
            String field = !BarcoUtil.isNull(violation.getPropertyPath()) ? violation.getPropertyPath().toString() : null;
            Object invalidValue = violation.getInvalidValue();
            String message = violation.getMessage();
            subErrors.add(new ApiValidationError(object, field, invalidValue, message));
        }
        // Create an APIResponse with a bad request status and include the list of validation errors as the response body
        APIResponse<List<ApiValidationError>> apiResponse = APIResponse.badRequest("Validation failed", subErrors);
        apiResponse.setPath(getPath(request));
        return ResponseEntity.status(apiResponse.getReturnCode()).body(apiResponse);
    }

    /**
     * Handles authentication exceptions that occur when a user fails to authenticate successfully.
     * @param ex the AuthenticationException containing details about the authentication failure
     * @param request the WebRequest that resulted in the exception
     * @return a ResponseEntity containing an APIResponse with details about the authentication error
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        LOGGER.debug("Authentication exception: {}", ex.getMessage());
        APIResponse<?> apiResponse = APIResponse.unauthorized("Authentication failed: " + ex.getMessage());
        apiResponse.setPath(getPath(request));
        return ResponseEntity.status(apiResponse.getReturnCode()).body(apiResponse);
    }

    /**
     * Utility method to extract the request path from a WebRequest.
     * @param request the WebRequest from which to extract the path
     * @return the request path, or null if it cannot be extracted
     */
    private String getPath(WebRequest request) {
        try {
            if (request instanceof ServletWebRequest) {
                return ((ServletWebRequest) request).getRequest().getRequestURI();
            }
        } catch (Exception e) {
            LOGGER.debug("Could not extract request path", e);
        }
        return null;
    }

}
