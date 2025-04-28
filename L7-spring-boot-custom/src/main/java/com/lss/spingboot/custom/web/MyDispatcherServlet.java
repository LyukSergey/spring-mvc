package com.lss.spingboot.custom.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyDispatcherServlet extends HttpServlet {

    private final RouteRegistry routeRegistry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MyDispatcherServlet(RouteRegistry registry) {
        this.routeRegistry = registry;
    }

    @Override
    public void init() throws ServletException {
        System.out.println("[MyDispatcherServlet] 🔥 DispatcherServlet initialized!");
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("[MyDispatcherServlet] 🚀 service() method called, HTTP method: " + req.getMethod());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("[MyDispatcherServlet] 📥 Received GET request: " + req.getRequestURI());
        String path = req.getRequestURI();
        RouteRegistry.HandlerMethod handler = routeRegistry.getHandler(path);
        if (handler != null) {
            try {
                Object result = handler.method.invoke(handler.controller);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getWriter(), result);
            } catch (Exception e) {
                resp.setStatus(500);
                objectMapper.writeValue(resp.getWriter(), "Internal Error");
            }
        } else {
            resp.setStatus(404);
            objectMapper.writeValue(resp.getWriter(), "Not Found");
        }
    }
}