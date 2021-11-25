package ua.com.foxminded.university.exceptions;

public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
