package sk.janobono.emergency.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestControllerAdvice
public class RestControllerAdvisor {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException handlerFoundException) {
        log.warn(handlerFoundException.toString(), handlerFoundException);
        return new ResponseEntity<>(new ExceptionBody(
                HttpStatus.NOT_FOUND.name(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(final ResponseStatusException responseStatusException) {
        log.warn(responseStatusException.toString(), responseStatusException);
        return new ResponseEntity<>(
                new ExceptionBody(
                        "UNKNOWN",
                        responseStatusException.getReason(),
                        LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
                ), responseStatusException.getStatusCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(final RuntimeException runtimeException) {
        log.warn(runtimeException.toString(), runtimeException);
        return new ResponseEntity<>(new ExceptionBody(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
