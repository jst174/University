package ua.com.foxminded.university.exceptions;

public class NoEntityException extends ServiceException {

    public NoEntityException() {
    }

    public NoEntityException(String message) {
        super(message);
    }

    public NoEntityException(String message, Exception cause) {
        super(message, cause);
    }
}
