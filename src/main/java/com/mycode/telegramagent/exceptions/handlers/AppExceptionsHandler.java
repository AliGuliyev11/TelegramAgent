package com.mycode.telegramagent.exceptions.handlers;

import com.mycode.telegramagent.exceptions.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

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

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), "Please,enter correct email.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {YouAlreadyMakeOffer.class})
    public ResponseEntity<Object> handleMakeOfferException(YouAlreadyMakeOffer ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Already make offer.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {RequestExpired.class})
    public ResponseEntity<Object> handleRequestExpiredException(RequestExpired ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "This request expired.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {RequestNotFound.class})
    public ResponseEntity<Object> handleRequestNotFoundException(RequestNotFound ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), "This request not found.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {OfferValidation.class})
    public ResponseEntity<Object> handleOfferValidationException(OfferValidation ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Except note,other fields is required.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {OfferPriceZero.class})
    public ResponseEntity<Object> handleOfferPriceZeroException(OfferPriceZero ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Price mustn't be zero or negative value.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {DateFormat.class})
    public ResponseEntity<Object> handleDateFormatException(DateFormat ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add date in year.month.day format(Ex. 2021-07-18).");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {OfferDateBeforeNow.class})
    public ResponseEntity<Object> handleDateBeforeException(OfferDateBeforeNow ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add date present or future tense.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {EndDateBeforeStart.class})
    public ResponseEntity<Object> handleEndDateBeforeException(EndDateBeforeStart ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add correct end date.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {CheckStartDate.class})
    public ResponseEntity<Object> handleCheckStartDateException(CheckStartDate ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add start date present or future tense.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {NotAnyOffer.class})
    public ResponseEntity<Object> handleNotAnyOfferException(NotAnyOffer ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), "You haven't any offer.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {NotAnyRequest.class})
    public ResponseEntity<Object> handleNotAnyRequestException(NotAnyRequest ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), "You haven't any request.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {OfferNotWorkingHour.class})
    public ResponseEntity<Object> handleOfferNotWHoursException(OfferNotWorkingHour ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,come back later.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {RequestInArchive.class})
    public ResponseEntity<Object> handleRequestInArchiveException(RequestInArchive ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "This request is in archive.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {OfferBudgetHigher.class})
    public ResponseEntity<Object> handleOfferBudgetException(OfferBudgetHigher ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Offer budget too higher than requested budget.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {EmailValidation.class})
    public ResponseEntity<Object> handleEmailValidationException(EmailValidation ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add correct email.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {PasswordValidation.class})
    public ResponseEntity<Object> handlePasswordException(PasswordValidation ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add correct password.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {PhoneValidation.class})
    public ResponseEntity<Object> handlePhoneException(PhoneValidation ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add your correctly.E.g:+994501234567.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {VoenValidation.class})
    public ResponseEntity<Object> handleVOENException(VoenValidation ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Please,add your VOEN correctly.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {RequestAccepted.class})
    public ResponseEntity<Object> handleRequestAcceptedException(RequestAccepted ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Accepted request cannot be moved from archive to incoming requests.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {AgentValidation.class})
    public ResponseEntity<Object> handleAgentValidationException(AgentValidation ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.CONFLICT,
                ex.getLocalizedMessage(), "Except VOEN,other fields is required.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }

    @ExceptionHandler(value = {AgentNotFound.class})
    public ResponseEntity<Object> handleAgentNotFoundnException(AgentNotFound ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.NOT_FOUND,
                ex.getLocalizedMessage(), "Thia agent not found.");
        return new ResponseEntity<>(
                errorMessage, new HttpHeaders(), errorMessage.getStatus());

    }


}
