package spring.mvc.lecture.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTestController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello(HttpServletRequest request) {
        System.out.printf("Request in %s hello()%n", this.getClass().getSimpleName());
        final Map<String, String> data = new HashMap<>();
        data.put("hello", "world");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }

}
