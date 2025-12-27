package fr.fgroup.chatai.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExpectationFailedException extends RuntimeException {

    public ExpectationFailedException() {
    }

    public ExpectationFailedException(String message) {
        super(message);
    }

    public ExpectationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
