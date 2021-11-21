package ua.com.foxminded.university.exceptions;

public class NotAvailableTeacherException extends ServiceException {
    public NotAvailableTeacherException() {
    }

    public NotAvailableTeacherException(String message) {
        super(message);
    }

    public NotAvailableTeacherException(String message, Exception cause) {
        super(message, cause);
    }
}
