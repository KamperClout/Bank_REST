package com.example.bankcards.exception;

import com.example.bankcards.exception.cardException.CardNotFoundException;
import com.example.bankcards.exception.cardException.CardTransferException;
import com.example.bankcards.exception.cardException.StatusConflictException;
import com.example.bankcards.exception.userException.InvalidPasswordException;
import com.example.bankcards.exception.userException.UserExistsException;
import com.example.bankcards.exception.userException.UserNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@ResponseBody
public class ExceptionHandlerController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleUserNotFoundException(UserNotFoundException e) {
        return new ApiError("USER_NOT_FOUND", "Please check your request", e.getMessage(),
                LocalDateTime.now());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserExistsException(UserExistsException e) {
        return new ApiError("USER_EXISTS_CONFLICT", "There is exists user", e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleCardNotFoundException(CardNotFoundException e) {
        return new ApiError("CARD_NOT_FOUND", "Please check your request", e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleStatusConflictException(StatusConflictException e) {
        return new ApiError("STATUS_CONFLICT", "Please check status!", e.getMessage(),
                LocalDateTime.now());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCardTransferException(CardTransferException e) {
        return new ApiError("TRANSFER_CONFLICT", "Please check transfer status!", e.getMessage(),
                LocalDateTime.now());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleInvalidPassworfException(InvalidPasswordException e) {
        return new ApiError("INVALID_PASSWORD", "Please try again your credentials!", e.getMessage(),
                LocalDateTime.now());
    }



    record ApiError(String status, String reason, String message,
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp) {
            ApiError(String status, String reason, String message, LocalDateTime timestamp) {
                this.status = status;
                this.reason = reason;
                this.message = message;
                this.timestamp = timestamp;
            }
        }
}
