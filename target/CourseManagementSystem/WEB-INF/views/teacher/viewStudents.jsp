<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.coursemanagement.model.User, java.util.List, com.coursemanagement.model.Course" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !user.getRole().equals("teacher")) {
        response.sendRedirect("../login");
        return;
    }
    Course course = (Course) request.getAttribute("course");
    List<User> students = (List<User>) request.getAttribute("students");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Students</title>
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
                    <a href="dashboard" class="list-group-item list-group-item-action">My Courses</a>
                    <a href="#" class="list-group-item list-group-item-action active">View Students</a>
                </div>
            </div>
            <div class="col-md-9">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h4>Students enrolled in <%= course.getTitle() %></h4>
                        <a href="dashboard" class="btn btn-secondary btn-sm">Back to Courses</a>
                    </div>
                    <div class="card-body">
                        <% if (students != null && !students.isEmpty()) { %>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Student ID</th>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Username</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (User student : students) { %>
                                            <tr>
                                                <td><%= student.getId() %></td>
                                                <td><%= student.getName() %></td>
                                                <td><%= student.getEmail() %></td>
                                                <td><%= student.getUsername() %></td>
                                            </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        <% } else { %>
                            <div class="alert alert-info" role="alert">
                                No students are enrolled in this course yet.
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
