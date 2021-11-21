package ua.com.foxminded.university.exceptions;

public class NotAvailableClassroomException extends ServiceException {

    public NotAvailableClassroomException() {
    }

    public NotAvailableClassroomException(String message) {
        super(message);
    }

    public NotAvailableClassroomException(String message, Exception cause) {
        super(message, cause);
    }
}
