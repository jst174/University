package ua.com.foxminded.university.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;
import ua.com.foxminded.university.exceptions.ServiceException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ServiceException.class})
    public String handleNotFound(ServiceException exception, Model model) {
        model.addAttribute("exception", exception.getClass().getSimpleName());
        model.addAttribute("message", exception.getMessage());
        return "exception/error";
    }
}
