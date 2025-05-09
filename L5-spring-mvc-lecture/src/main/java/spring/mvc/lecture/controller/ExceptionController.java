package spring.mvc.lecture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping
    public String getException() {
        System.out.printf("Request in %s getException()%n", this.getClass().getSimpleName());
        throw new RuntimeException("Some exception");
    }
}
