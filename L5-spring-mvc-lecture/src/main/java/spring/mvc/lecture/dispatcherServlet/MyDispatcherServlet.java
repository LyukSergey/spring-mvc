package spring.mvc.lecture.dispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.DispatcherServlet;

public class MyDispatcherServlet extends DispatcherServlet {

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.printf("🔍 Request received in: %s%n", this.getClass().getSimpleName());
        System.out.printf("🔍 Request in: " + request.getMethod() + " " + request.getRequestURI() + " " + this.getClass().getSimpleName() +"%n");

        long start = System.currentTimeMillis();
        super.doDispatch(request, response);
        long time = System.currentTimeMillis() - start;
        System.out.printf("🔍 Request out from: %s%n", this.getClass().getSimpleName());
        System.out.printf("Request out. Request takes = : " + time + " ms%n");
    }

}
