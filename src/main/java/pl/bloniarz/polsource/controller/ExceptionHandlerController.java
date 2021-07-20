package pl.bloniarz.polsource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.bloniarz.polsource.model.exceptions.AppException;
import pl.bloniarz.polsource.model.exceptions.AppExceptionResponse;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<AppExceptionResponse> handleNotValidArgumentException() {
        return ResponseEntity
                .status(400)
                .body(new AppExceptionResponse("Notes must contain content and title"));
    }

    @ExceptionHandler({AppException.class})
    public ResponseEntity<AppExceptionResponse> handleAppException(AppException appException){
        return ResponseEntity
                .status(appException.getResponseStatus())
                .body(new AppExceptionResponse(appException.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<AppExceptionResponse> handleUnsupportedException(){
        return ResponseEntity
                .status(500)
                .body(new AppExceptionResponse("An unsupported exception occurred, we are sorry."));
    }

}
