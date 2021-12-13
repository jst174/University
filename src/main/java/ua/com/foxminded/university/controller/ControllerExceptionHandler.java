package ua.com.foxminded.university.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.foxminded.university.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public String handleNotFound(EntityNotFoundException exception, Model model) {
        model.addAttribute("exception", exception.getClass().getSimpleName());
        model.addAttribute("message", exception.getMessage());
        return "exception/error";
    }
}
