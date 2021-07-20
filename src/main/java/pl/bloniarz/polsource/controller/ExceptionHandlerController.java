package pl.bloniarz.polsource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bloniarz.polsource.model.exceptions.AppExceptionResponse;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<AppExceptionResponse> handleNotValidArgumentException()
    {
        return ResponseEntity
                .status(400)
                .body(new AppExceptionResponse("Notes must contain content and title"));
    }
}
