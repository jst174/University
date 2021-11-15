package ua.com.foxminded.university.exceptions;

public class ServiceException extends RuntimeException {

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, RuntimeException cause) {
        super(message, cause);
    }
}
