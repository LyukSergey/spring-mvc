package spring.mvc.lecture;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String hello(Model model) {
        System.out.println("Hello 1111");
        model.addAttribute("message", "Hello from Spring MVC!");
        return "welcome";
    }
}
