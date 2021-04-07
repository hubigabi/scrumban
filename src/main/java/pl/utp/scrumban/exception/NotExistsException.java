package pl.utp.scrumban.exception;

public class NotExistsException extends RuntimeException {

    public NotExistsException(String message) {
        super(message);
    }

    public NotExistsException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
