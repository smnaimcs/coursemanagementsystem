<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.coursemanagement.model.User, java.util.List, com.coursemanagement.model.Course" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !user.getRole().equals("teacher")) {
        response.sendRedirect("../login");
        return;
    }
    List<Course> assignedCourses = (List<Course>) request.getAttribute("assignedCourses");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard</title>
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
                    <a href="dashboard" class="list-group-item list-group-item-action active">My Courses</a>
                </div>
            </div>
            <div class="col-md-9">
                <div class="card">
                    <div class="card-header">
                        <h4>My Assigned Courses</h4>
                    </div>
                    <div class="card-body">
                        <% if (assignedCourses != null && !assignedCourses.isEmpty()) { %>
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Course ID</th>
                                            <th>Course Title</th>
                                            <th>Description</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (Course course : assignedCourses) { %>
                                            <tr>
                                                <td><%= course.getId() %></td>
                                                <td><%= course.getTitle() %></td>
                                                <td><%= course.getDescription() %></td>
                                                <td>
                                                    <a href="viewStudents?courseId=<%= course.getId() %>" 
                                                       class="btn btn-info btn-sm">View Students</a>
                                                </td>
                                            </tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        <% } else { %>
                            <div class="alert alert-info" role="alert">
                                You don't have any assigned courses yet.
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
