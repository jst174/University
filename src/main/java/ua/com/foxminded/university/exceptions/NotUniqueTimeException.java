package ua.com.foxminded.university.exceptions;

public class NotUniqueTimeException extends ServiceException {

    public NotUniqueTimeException() {
    }

    public NotUniqueTimeException(String message) {
        super(message);
    }

    public NotUniqueTimeException(String message, Exception cause) {
        super(message, cause);
    }
}
