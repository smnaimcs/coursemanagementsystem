<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.coursemanagement.model.User, java.util.List" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !user.getRole().equals("admin")) {
        response.sendRedirect("../login");
        return;
    }
    List<User> teachers = (List<User>) request.getAttribute("teachers");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Course</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="#">Course Management System</a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">Welcome, <%= user.getName() %></span>
                <a class="btn btn-outline-light btn-sm" href="../logout">Logout</a>
            </div>
        </div>
    </nav>
    
    <div class="container mt-4">
        <div class="row">
            <div class="col-md-3">
                <div class="list-group">
                    <a href="dashboard" class="list-group-item list-group-item-action">Dashboard</a>
                    <a href="addCourse" class="list-group-item list-group-item-action active">Add New Course</a>
                </div>
            </div>
            <div class="col-md-9">
                <div class="card">
                    <div class="card-header">
                        <h4>Add New Course</h4>
                    </div>
                    <div class="card-body">
                        <form action="addCourse" method="post">
                            <div class="mb-3">
                                <label for="title" class="form-label">Course Title</label>
                                <input type="text" class="form-control" id="title" name="title" required>
                            </div>
                            <div class="mb-3">
                                <label for="description" class="form-label">Description</label>
                                <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="teacherId" class="form-label">Teacher</label>
                                <select class="form-select" id="teacherId" name="teacherId" required>
                                    <option value="">Select a teacher</option>
                                    <% for (User teacher : teachers) { %>
                                        <option value="<%= teacher.getId() %>"><%= teacher.getName() %></option>
                                    <% } %>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary">Add Course</button>
                        </form>
                        
                        <%
                            String message = (String) request.getAttribute("message");
                            String error = (String) request.getAttribute("error");
                            
                            if (message != null) {
                        %>
                            <div class="alert alert-success mt-3" role="alert">
                                <%= message %>
                            </div>
                        <%
                            } else if (error != null) {
                        %>
                            <div class="alert alert-danger mt-3" role="alert">
                                <%= error %>
                            </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
