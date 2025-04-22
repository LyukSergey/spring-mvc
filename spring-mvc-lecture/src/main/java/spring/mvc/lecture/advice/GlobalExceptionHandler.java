package spring.mvc.lecture.advice;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleAllException(Exception ex) {
        System.out.printf("Exception in %s%n", this.getClass().getSimpleName());
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body("Something went wrong: " + ex.getMessage());
    }
}
