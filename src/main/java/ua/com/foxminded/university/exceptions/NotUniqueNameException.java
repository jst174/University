package ua.com.foxminded.university.exceptions;

public class NotUniqueNameException extends ServiceException {

    public NotUniqueNameException() {
    }

    public NotUniqueNameException(String message) {
        super(message);
    }

    public NotUniqueNameException(String message, ServiceException cause) {
        super(message, cause);
    }
}
