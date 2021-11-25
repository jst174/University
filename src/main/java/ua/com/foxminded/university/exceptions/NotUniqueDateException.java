package ua.com.foxminded.university.exceptions;

public class NotUniqueDateException extends ServiceException {

    public NotUniqueDateException() {
    }

    public NotUniqueDateException(String message) {
        super(message);
    }

    public NotUniqueDateException(String message, Exception cause) {
        super(message, cause);
    }
}
