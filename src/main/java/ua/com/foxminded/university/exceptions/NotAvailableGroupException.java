package ua.com.foxminded.university.exceptions;

public class NotAvailableGroupException extends ServiceException {

    public NotAvailableGroupException() {
    }

    public NotAvailableGroupException(String message) {
        super(message);
    }

    public NotAvailableGroupException(String message, Exception cause) {
        super(message, cause);
    }
}
