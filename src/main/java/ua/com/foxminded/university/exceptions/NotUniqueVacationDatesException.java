package ua.com.foxminded.university.exceptions;

public class NotUniqueVacationDatesException extends ServiceException {

    public NotUniqueVacationDatesException() {
    }

    public NotUniqueVacationDatesException(String message) {
        super(message);
    }

    public NotUniqueVacationDatesException(String message, Exception cause) {
        super(message, cause);
    }
}
