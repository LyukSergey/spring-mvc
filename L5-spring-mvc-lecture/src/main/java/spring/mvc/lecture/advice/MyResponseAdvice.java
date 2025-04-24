package spring.mvc.lecture.advice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class MyResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        System.out.printf("ResponseBodyAdvice: check body in %s supports()%n", this.getClass().getSimpleName());
        return true; // перехоплюй все
    }

    @Override
    public ResponseEntity<Object> beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        System.out.printf("ResponseBodyAdvice: check body in %s beforeBodyWrite()%n", this.getClass().getSimpleName());
        int status = HttpStatus.OK.value(); // Default

        // Витягуємо справжній HttpServletResponse
        if (response instanceof ServletServerHttpResponse) {
            status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        }
        final Map<String, Object> data = new HashMap<>();
        data.put("data", body);
        data.put("timestamp", Instant.now().toString());
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(data);
    }
}
