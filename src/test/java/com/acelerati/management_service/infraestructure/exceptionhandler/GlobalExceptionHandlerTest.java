package com.acelerati.management_service.infraestructure.exceptionhandler;

import com.acelerati.management_service.domain.exception.ProductNotFoundException;
import com.acelerati.management_service.infraestructure.exceptionhandler.response.ErrorDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    GlobalExceptionHandler globalExceptionHandler;
    ResponseEntity<ErrorDetails> response;
    ErrorDetails body ;
    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        body = new ErrorDetails(LocalDateTime.of(2023,02,22,10,22),"Error","/path");
    }

    @Test
    void whenThrowExceptionNotControlledThenHandleAllExceptions() {
        WebRequest webrequest = mock(WebRequest.class);
        response = new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        when(webrequest.getDescription(false)).thenReturn("/path");
        ResponseEntity<ErrorDetails> responseReal = this.globalExceptionHandler.handleAllExceptions(new Exception("Error"),webrequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,responseReal.getStatusCode());
    }

    @Test
    void whenThrowConstraintViolationExceptionHandleAll() {
        WebRequest webrequest = mock(WebRequest.class);
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        response = new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        when(webrequest.getDescription(false)).thenReturn("/path");
        ResponseEntity<ErrorDetails> responseReal = this.globalExceptionHandler.handleConstraintViolationException(exception,webrequest);
        assertEquals(HttpStatus.BAD_REQUEST,responseReal.getStatusCode());
    }

    @Test
    void whenThrowAccessDeniedExceptionThenReturnHttpStatus403(){
        WebRequest webRequest = mock(WebRequest.class);
        AccessDeniedException exception = mock(AccessDeniedException.class);
        response = new ResponseEntity<>(body,HttpStatus.FORBIDDEN);
        when(webRequest.getDescription(false)).thenReturn(("/path"));
        ResponseEntity<ErrorDetails>responseReal = this.globalExceptionHandler.handleAccessDeniedException(exception,webRequest);
        assertEquals(HttpStatus.FORBIDDEN,responseReal.getStatusCode());
    }
    @Test
    void whenThrowProductNotFoundExceptionThenReturnHttStatus404(){
        WebRequest webRequest = mock(WebRequest.class);
        ProductNotFoundException exception = mock(ProductNotFoundException.class);
        response = new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
        when(webRequest.getDescription(false)).thenReturn(("/path"));
        ResponseEntity<ErrorDetails>responseReal = this.globalExceptionHandler.handleProductNotFoundException(exception,webRequest);
        assertEquals(HttpStatus.NOT_FOUND,responseReal.getStatusCode());
    }

}