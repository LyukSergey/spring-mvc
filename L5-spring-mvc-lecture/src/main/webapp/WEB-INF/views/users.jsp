<%@ page import="spring.mvc.lecture.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://www.springframework.org/tags/form" %>
<html>
<head><title>Users</title></head>
<body>Hello JSP!<br/>
<h2>User List</h2>
<ul>
    <%
        List<User> users = (List<User>) request.getAttribute("users");
        for (User user : users) {
    %>
    <li><%= user.getName() %></li>
    <% } %>
</ul>
</body>
</html>
