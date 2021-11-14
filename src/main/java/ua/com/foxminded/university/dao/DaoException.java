package ua.com.foxminded.university.dao;

public class DaoException extends RuntimeException {

    public DaoException() {
        super();
    }

    public DaoException(String message, RuntimeException cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }
}
