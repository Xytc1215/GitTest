package jm.task.core.jdbc.exception;

public class SessionFactoryCreationException extends RuntimeException {
    public SessionFactoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}