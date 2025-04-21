package ru.diplom.fpd.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.diplom.fpd.exception.AddressNotInDeliveryAreaException;
import ru.diplom.fpd.exception.ImageExtensionNotSupportedException;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
//@Api(value = "Обработка ошибок", tags = {"Ошибки"})
public class AdviceController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointer(NullPointerException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler({AuthorizationServiceException.class})
    protected ResponseEntity<Object> handleAuthorizationService(AuthorizationServiceException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({ImageExtensionNotSupportedException.class})
    protected ResponseEntity<Object> handleImageExtensionNotSupported(ImageExtensionNotSupportedException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({FileNotFoundException.class})
    protected ResponseEntity<Object> handleFileNotFound(FileNotFoundException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({AddressNotInDeliveryAreaException.class})
    public ResponseEntity<Object> AddressException(RuntimeException ex, WebRequest request) {
        return createResponse(ex, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }



    private ResponseEntity<Object> createResponse(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        logger.error(ex + ". " + request.toString() + ". " + Arrays.toString(ex.getStackTrace()));
        map.put("Timestamp", new Date().toString());
        map.put("Status",  String.valueOf(status.value()));
        map.put("Error", status.getReasonPhrase());
        map.put("Message", ex.getMessage());
        map.put("Path", request.getContextPath());
        return ResponseEntity.status(status).body(map);
    }

}
