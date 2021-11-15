package ua.com.foxminded.university.exceptions;

public class NotUniqueNameException extends Exception {

    public NotUniqueNameException() {
    }

    public NotUniqueNameException(String message) {
        super(message);
    }

    public NotUniqueNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
