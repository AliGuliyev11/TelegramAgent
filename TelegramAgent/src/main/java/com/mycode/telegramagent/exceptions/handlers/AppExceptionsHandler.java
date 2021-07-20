package com.mycode.telegramagent.exceptions.handlers;

import com.mycode.telegramagent.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error occurred.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Please fill out all of the fields.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

    @ExceptionHandler(value = {MethodNotAllowedException.class})
    public ResponseEntity<Object> handleMethodNotAllowedException(MethodNotAllowedException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage(), "This method not allowed for this request.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementException(MethodNotAllowedException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage(), "No value present.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {EmailNotVerified.class})
    public ResponseEntity<Object> handleEmailVerificationException(EmailNotVerified ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.FORBIDDEN,
                ex.getLocalizedMessage(), "You must verify your email.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {NotCreated.class})
    public ResponseEntity<Object> handleNotCreatedAgentException(NotCreated ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), "You must sign up.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {EmailAlreadyExist.class})
    public ResponseEntity<Object> handleEmailExistException(EmailAlreadyExist ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "This email already exist.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {CompanyExist.class})
    public ResponseEntity<Object> handleCompanyExistException(CompanyExist ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "This company already exist.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }
    @ExceptionHandler(value = {AgencyExist.class})
    public ResponseEntity<Object> handleAgencyExistException(AgencyExist ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "This agency already exist.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {PasswordNotMatched.class})
    public ResponseEntity<Object> handlePasswordNotMatchedException(PasswordNotMatched ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,enter correct password.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {EmailNotFound.class})
    public ResponseEntity<Object> handleEmailNotFoundException(EmailNotFound ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,enter correct email.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }



}
