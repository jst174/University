package ua.com.foxminded.university.exceptions;

public class NotAvailableTimeException extends ServiceException {
    public NotAvailableTimeException() {
    }

    public NotAvailableTimeException(String message) {
        super(message);
    }

    public NotAvailableTimeException(String message, Exception cause) {
        super(message, cause);
    }
}
