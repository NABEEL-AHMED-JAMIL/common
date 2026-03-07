package com.barco.common.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import java.io.Serializable;

/**
 * Generic API Response wrapper for all API endpoints.
 * Provides consistent response format across the application.
 *
 * @author Nabeel Ahmed
 * @param <T> The type of entity/data in the response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static String DEFAULT_SUCCESS_STATUS = "SUCCESS";
    private final static String DEFAULT_ERROR_STATUS = "ERROR";

    private String message;
    private String status;
    private HttpStatus returnCode;
    private Integer statusCode;
    private T data;
    private long timestamp;
    private String path;

    public APIResponse() {}

    public APIResponse(String message, String status, HttpStatus returnCode,
        Integer statusCode, T data, long timestamp, String path) {
        this.message = message;
        this.status = status;
        this.returnCode = returnCode;
        this.statusCode = statusCode;
        this.data = data;
        this.timestamp = timestamp;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public APIResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public APIResponse<T> setStatus(String status) {
        this.status = status;
        return this;
    }

    public HttpStatus getReturnCode() {
        return returnCode;
    }

    public APIResponse<T> setReturnCode(HttpStatus returnCode) {
        this.returnCode = returnCode;
        return this;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public APIResponse<T> setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public T getData() {
        return data;
    }

    public APIResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public APIResponse<T> setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getPath() {
        return path;
    }

    public APIResponse<T> setPath(String path) {
        this.path = path;
        return this;
    }

    public static <T> APIResponseBuilder<T> builder() {
        return new APIResponseBuilder<>();
    }

    /**
     * Builder class for APIResponse
     */
    public static class APIResponseBuilder<T> {

        private String message;
        private String status;
        private HttpStatus returnCode;
        private Integer statusCode;
        private T entity;
        private long timestamp;
        private String path;

        public APIResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public APIResponseBuilder<T> status(String status) {
            this.status = status;
            return this;
        }

        public APIResponseBuilder<T> returnCode(HttpStatus returnCode) {
            this.returnCode = returnCode;
            return this;
        }

        public APIResponseBuilder<T> statusCode(Integer statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public APIResponseBuilder<T> entity(T entity) {
            this.entity = entity;
            return this;
        }

        public APIResponseBuilder<T> timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public APIResponseBuilder<T> path(String path) {
            this.path = path;
            return this;
        }

        public APIResponse<T> build() {
            return new APIResponse<>(message, status, returnCode, statusCode, entity, timestamp, path);
        }
    }

    /**
     * Build a success response with only data
     */
    public static <T> APIResponse<T> success(T data) {
        return APIResponse.<T>builder()
            .status(DEFAULT_SUCCESS_STATUS)
            .entity(data)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.OK)
            .statusCode(200)
            .build();
    }

    /**
     * Build a success response with message and data
     */
    public static <T> APIResponse<T> success(String message, T data) {
        return APIResponse.<T>builder()
            .status(DEFAULT_SUCCESS_STATUS)
            .message(message)
            .entity(data)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.OK)
            .statusCode(200)
            .build();
    }

    /**
     * Build a success response with message, data, and status code
     */
    public static <T> APIResponse<T> success(String message, T data, HttpStatus returnCode) {
        return APIResponse.<T>builder()
            .status(DEFAULT_SUCCESS_STATUS)
            .message(message)
            .entity(data)
            .timestamp(System.currentTimeMillis())
            .returnCode(returnCode)
            .statusCode(returnCode.value())
            .build();
    }

    /**
     * Build a success response with path information
     */
    public static <T> APIResponse<T> success(String message, T data, String path) {
        return APIResponse.<T>builder()
            .status(DEFAULT_SUCCESS_STATUS)
            .message(message)
            .entity(data)
            .path(path)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.OK)
            .statusCode(200)
            .build();
    }

    /**
     * Build an error response with message only
     */
    public static <T> APIResponse<T> error(String message) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.INTERNAL_SERVER_ERROR)
            .statusCode(500)
            .build();
    }

    /**
     * Build an error response with message and status code
     */
    public static <T> APIResponse<T> error(String message, Integer statusCode) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.valueOf(statusCode))
            .statusCode(statusCode)
            .build();
    }

    /**
     * Build an error response with message and HttpStatus
     */
    public static <T> APIResponse<T> error(String message, HttpStatus returnCode) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(returnCode)
            .statusCode(returnCode.value())
            .build();
    }

    /**
     * Build an error response with message, data and HttpStatus
     */
    public static <T> APIResponse<T> error(String message, T data, HttpStatus returnCode) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .entity(data)
            .timestamp(System.currentTimeMillis())
            .returnCode(returnCode)
            .statusCode(returnCode.value())
            .build();
    }

    /**
     * Build an unauthorized response (401)
     */
    public static <T> APIResponse<T> unauthorized(String message) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.UNAUTHORIZED)
            .statusCode(401)
            .build();
    }

    /**
     * Build a forbidden response (403)
     */
    public static <T> APIResponse<T> forbidden(String message) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.FORBIDDEN)
            .statusCode(403)
            .build();
    }

    /**
     * Build a not found response (404)
     */
    public static <T> APIResponse<T> notFound(String message) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.NOT_FOUND)
            .statusCode(404)
            .build();
    }

    /**
     * Build a not found response (404) with data
     */
    public static <T> APIResponse<T> notFound(String message, T data) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .entity(data)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.NOT_FOUND)
            .statusCode(404)
            .build();
    }

    /**
     * Build a service unavailable response (503)
     */
    public static <T> APIResponse<T> serviceUnavailable(String message) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.SERVICE_UNAVAILABLE)
            .statusCode(503)
            .build();
    }

    /**
     * Build a bad request response (400)
     */
    public static <T> APIResponse<T> badRequest(String message) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.BAD_REQUEST)
            .statusCode(400)
            .build();
    }

    /**
     * Build a bad request response (400) with data
     */
    public static <T> APIResponse<T> badRequest(String message, T data) {
        return APIResponse.<T>builder()
            .status(DEFAULT_ERROR_STATUS)
            .message(message)
            .entity(data)
            .timestamp(System.currentTimeMillis())
            .returnCode(HttpStatus.BAD_REQUEST)
            .statusCode(400)
            .build();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
