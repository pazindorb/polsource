package pl.bloniarz.polsource.model.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException{

    private int responseStatus;

    public AppException(AppExceptionMessage appExceptionMessage, String... params){
        super(String.format(appExceptionMessage.getMessage(),params));
        this.responseStatus = appExceptionMessage.getStatus();
    }

}
