package com.monolith.boilerplate.config;

import com.monolith.boilerplate.dto.ErrorDTO;
import com.monolith.boilerplate.exception.BadRequestException;
import com.monolith.boilerplate.exception.ResourceNotFoundException;
import com.monolith.boilerplate.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class GlobalExceptionHandler {

    private static String INTERNAL_SERVER_ERROR_MESSAGE = "An internal server error occured.";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> runtimeExceptionHandler(@AuthenticationPrincipal UserPrincipal principal, RuntimeException e, HttpServletRequest req) {
        ErrorDTO.ErrorDTOBuilder errorDTOBuilder = getErrorDTOBuilder(principal, req);
        if(e instanceof BadRequestException){
            return new ResponseEntity<>(errorDTOBuilder.message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        } else if (e instanceof ResourceNotFoundException){
            return new ResponseEntity<>(errorDTOBuilder.message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        } else if (e instanceof BadCredentialsException){
            return new ResponseEntity<>(errorDTOBuilder.message(e.getMessage()).build(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(errorDTOBuilder.message(INTERNAL_SERVER_ERROR_MESSAGE).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> exceptionHandler(@AuthenticationPrincipal UserPrincipal principal, Exception e, HttpServletRequest req) {
        ErrorDTO.ErrorDTOBuilder errorDTOBuilder = getErrorDTOBuilder(principal, req);
        return new ResponseEntity<>(errorDTOBuilder.message(INTERNAL_SERVER_ERROR_MESSAGE).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorDTO.ErrorDTOBuilder getErrorDTOBuilder(@AuthenticationPrincipal UserPrincipal principal, HttpServletRequest req) {
        return ErrorDTO.builder()
                .userId(principal != null ? principal.getId() : null)
                .referenceCode(UUID.randomUUID().toString())
                .requestMethod(req.getMethod())
                .requestUrl(String.valueOf(req.getRequestURL()));
    }

}
