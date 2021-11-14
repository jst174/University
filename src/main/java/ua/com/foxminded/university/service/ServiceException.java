package ua.com.foxminded.university.service;

public class ServiceException extends RuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, RuntimeException cause) {
        super(message, cause);
    }
}
