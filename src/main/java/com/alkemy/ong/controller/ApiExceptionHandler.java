package com.alkemy.ong.controller;

import com.alkemy.ong.dto.ApiErrorDTO;
import com.alkemy.ong.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseBody
    public void unauthorizedRequest() {
        //Empty. Nothing to do
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    @ResponseBody
    public ResponseEntity<ApiErrorDTO> notFoundRequest(NotFoundException exception) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({//BadRequestException.class,
            org.springframework.dao.DuplicateKeyException.class,
            org.springframework.web.bind.support.WebExchangeBindException.class,
            org.springframework.http.converter.HttpMessageNotReadableException.class,
            org.springframework.web.server.ServerWebInputException.class})
    @ResponseBody
    public ResponseEntity<ApiErrorDTO> badRequest(Exception exception) {

        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<ApiErrorDTO> handleException(BadRequestException exc) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        List<String> errors = exc.getResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return buildResponseEntity(httpStatus, new RuntimeException("Data enviada es invalida"), errors);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ConflictException.class})
    @ResponseBody
    public ResponseEntity<ApiErrorDTO> conflict(ConflictException exception) {
        return buildResponseEntity(HttpStatus.CONFLICT, exception);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class})
    @ResponseBody
    public ResponseEntity<ApiErrorDTO> forbidden(ForbiddenException exception) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, exception);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler({BadGatewayException.class})
    @ResponseBody
    public ResponseEntity<ApiErrorDTO> badGateway(BadGatewayException exception) {
        return buildResponseEntity(HttpStatus.BAD_GATEWAY, exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public ResponseEntity<ApiErrorDTO> Throwable(Throwable exception) { // The error must be corrected
        exception.printStackTrace();
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private ResponseEntity<ApiErrorDTO> buildResponseEntity(HttpStatus httpStatus, Throwable exc) {
        return buildResponseEntity(httpStatus, exc, null);
    }

    private ResponseEntity<ApiErrorDTO> buildResponseEntity(HttpStatus httpStatus, Throwable exc, List<String> errors) {
        ApiErrorDTO error = new ApiErrorDTO();
        error.setMessage("USRMSG-" + exc.getMessage());
        error.setStatus(httpStatus);
        error.setErrors(errors);
        return new ResponseEntity<>(error, httpStatus);
    }

}
