package pl.utp.scrumban.exception;

import java.util.function.Supplier;

public class NotExistsException extends RuntimeException {

    public NotExistsException(String message) {
        super(message);
    }

    public NotExistsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
