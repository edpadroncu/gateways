package com.edpadron.gateways.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.edpadron.gateways.common.Helper;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private Helper helper;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getCause() instanceof InvalidFormatException ? "Field with invalid format." : "Incorrect payload.";
        ApiError apiError = new ApiError(message, "JSON request incorrect.", ex.getCause().getMessage());
        return helper.httpResponse(false, apiError, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError("Arguments validation", errors);
        return helper.httpResponse(false, apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

        ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
        return helper.httpResponse(false, apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        String error = ex.getRequestPartName() + " part is missing";
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
        return helper.httpResponse(false, apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        String error = ex.getParameterName() + " missing parameter";
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
        return helper.httpResponse(false, apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
        return helper.httpResponse(false, apiError, HttpStatus.UNPROCESSABLE_ENTITY );
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        ApiError apiError = new ApiError("Validation error", errors);
        return helper.httpResponse(false, apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({ ValidationException.class })
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        ApiError apiError = new ApiError("Validation error", errors);
        return helper.httpResponse(false, apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), error);
        return helper.httpResponse(false, apiError, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), builder.toString());
        return helper.httpResponse(false, apiError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return helper.httpResponse(false, apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<?> handleAll(Exception ex, WebRequest request) {
        logger.error(ex.getClass().getName() + " " + ex.getLocalizedMessage());
        ex.printStackTrace();
        ApiError apiError = new ApiError(ex.getLocalizedMessage(), "Sorry, something went wrong...");
        return helper.httpResponse(false, apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
