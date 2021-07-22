package pl.bloniarz.polsource.model.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppExceptionMessage {
    NOTE_NOT_FOUND("Note with id: %s not found", 404),
    NO_CHANGES("There is nothing to edit", 400),
    ;
    final String message;
    final int status;
}
