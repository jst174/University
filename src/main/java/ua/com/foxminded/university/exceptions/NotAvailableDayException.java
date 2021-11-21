package ua.com.foxminded.university.exceptions;

public class NotAvailableDayException extends ServiceException {

    public NotAvailableDayException() {
    }

    public NotAvailableDayException(String message) {
        super(message);
    }

    public NotAvailableDayException(String message, Exception cause) {
        super(message, cause);
    }
}
