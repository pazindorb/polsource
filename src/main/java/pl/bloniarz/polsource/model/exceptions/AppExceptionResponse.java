package pl.bloniarz.polsource.model.exceptions;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class AppExceptionResponse {

    private final String message;
    private final String errorTime;

    public AppExceptionResponse(String message){
        this.message = message;
        this.errorTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
