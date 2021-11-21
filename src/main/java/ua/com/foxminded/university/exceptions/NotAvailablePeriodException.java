package ua.com.foxminded.university.exceptions;

public class NotAvailablePeriodException extends ServiceException {

    public NotAvailablePeriodException() {
    }

    public NotAvailablePeriodException(String message) {
        super(message);
    }

    public NotAvailablePeriodException(String message, Exception cause) {
        super(message, cause);
    }
}
