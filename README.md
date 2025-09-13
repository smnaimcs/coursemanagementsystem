## Project Documentation

# Course Management System

## 1. Project Overview

The Course Management System is a web-based application developed using Java Servlets and JSP with Bootstrap for the frontend. It provides a platform for managing online courses with three distinct user roles: Administrators, Teachers, and Students.

### 1.1 Key Features
- **Role-based Access Control**: Three user types with different privileges
- **Course Management**: Admin can create courses and assign teachers
- **Student Enrollment**: Students can register for available courses
- **Teacher Dashboard**: Teachers can view their courses and enrolled students
- **User Authentication**: Secure login system with session management

### 1.2 Technology Stack
- **Backend**: Java Servlets, JSP
- **Frontend**: HTML, CSS, Bootstrap 5, JavaScript
- **Database**: MySQL
- **Server**: Apache Tomcat 9
- **Build Tool**: Maven

## 2. System Architecture

### 2.1 MVC Architecture Pattern
The application follows the Model-View-Controller pattern:

```
Model      -> Java Beans (User, Course) and DAO classes
View       -> JSP pages with Bootstrap
Controller -> Servlet classes
```

### 2.2 Project Structure
```
CourseManagementSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/coursemanagement/
│       │       ├── controller/     # Servlets
│       │       ├── model/          # Entity classes
│       │       └── dao/            # Data Access Objects
│       ├── webapp/
│       │   ├── WEB-INF/
│       │   │   ├── web.xml         # Deployment descriptor
│       │   │   └── views/          # JSP pages
│       │   ├── css/                # Stylesheets
│       │   └── js/                 # JavaScript files
│       └── resources/
│           └── database.properties # DB configuration
└── pom.xml                         # Maven configuration
```

## 3. Database Design

### 3.1 Entity-Relationship Diagram
```
+----------+     +----------+     +-------------+
|  users   |     | courses  |     | enrollments |
+----------+     +----------+     +-------------+
| id PK    |<-+  | id PK    |     | id PK       |
| username |  |  | title    |     | student_id FK -> users.id
| password |  |  | desc     |     | course_id FK -> courses.id
| role     |  |  | teacher_id FK -> users.id | enrollment_date |
| name     |  |  +----------+     +-------------+
| email    |  |
+----------+  |
```

### 3.2 Database Schema
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'teacher', 'student') NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

CREATE TABLE courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    teacher_id INT,
    FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE enrollments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    course_id INT,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    UNIQUE KEY unique_enrollment (student_id, course_id)
);
```

## 4. Functional Requirements Implementation

### 4.1 R-1: Three User Types
**Implementation**: The system supports three user roles stored in the `users` table with role validation in each servlet.

### 4.2 R-2: User Authentication
**Implementation**: 
- `LoginServlet` handles authentication
- Session management tracks logged-in users
- Direct database user creation as specified

### 4.3 R-3: Admin Course Management
**Implementation**:
- `AdminServlet` handles course creation
- Course assignment to teachers via dropdown selection
- Form validation in `addCourse.jsp`

### 4.4 R-4: Student Course Registration
**Implementation**:
- `StudentServlet` manages course enrollment
- Prevents duplicate enrollments
- Shows available courses excluding already enrolled ones

### 4.5 R-5: Teacher Student Viewing
**Implementation**:
- `TeacherServlet` displays assigned courses
- View students functionality per course
- Access control to ensure teachers only see their courses

## 5. Class Documentation

### 5.1 Model Classes

#### User.java
```java
/**
 * Represents a system user with authentication details
 * @Entity mapping to 'users' table
 */
public class User {
    private int id;             // Primary key
    private String username;    // Unique identifier
    private String password;    // Authentication credential
    private String role;        // Role: admin, teacher, or student
    private String name;        // Full name
    private String email;       // Contact email
    // Getters and setters
}
```

#### Course.java
```java
/**
 * Represents a course offered in the system
 * @Entity mapping to 'courses' table
 */
public class Course {
    private int id;             // Primary key
    private String title;       // Course title
    private String description; // Course description
    private int teacherId;      // Foreign key to users table
    private String teacherName; // Derived field for display
    // Getters and setters
}
```

### 5.2 DAO Classes

#### UserDAO.java
```java
/**
 * Data Access Object for user-related database operations
 * Handles authentication, user retrieval, and role-based queries
 */
public class UserDAO {
    public User authenticateUser(String username, String password) {
        // Validates user credentials against database
    }
    
    public List<User> getAllTeachers() {
        // Retrieves all users with teacher role
    }
    // Additional methods...
}
```

#### CourseDAO.java
```java
/**
 * Data Access Object for course-related database operations
 * Handles CRUD operations for courses
 */
public class CourseDAO {
    public boolean addCourse(Course course) {
        // Inserts new course into database
    }
    
    public List<Course> getCoursesByTeacherId(int teacherId) {
        // Retrieves courses assigned to specific teacher
    }
    // Additional methods...
}
```

#### EnrollmentDAO.java
```java
/**
 * Data Access Object for enrollment-related database operations
 * Manages student course registrations
 */
public class EnrollmentDAO {
    public boolean enrollStudent(int studentId, int courseId) {
        // Registers student for a course
    }
    
    public List<Course> getEnrolledCourses(int studentId) {
        // Retrieves courses a student is enrolled in
    }
    // Additional methods...
}
```

### 5.3 Servlet Classes

#### LoginServlet.java
```java
/**
 * Handles user authentication and session management
 * Redirects users to appropriate dashboards based on role
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    // doGet(): Displays login form
    // doPost(): Processes login credentials
}
```

#### AdminServlet.java
```java
/**
 * Handles administrative functionalities
 * Course creation and teacher assignment
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    // doGet(): Shows dashboard and add course form
    // doPost(): Processes new course creation
}
```

#### TeacherServlet.java
```java
/**
 * Handles teacher-specific functionalities
 * Viewing assigned courses and enrolled students
 */
@WebServlet("/teacher/*")
public class TeacherServlet extends HttpServlet {
    // doGet(): Shows teacher dashboard and student lists
}
```

#### StudentServlet.java
```java
/**
 * Handles student-specific functionalities
 * Course registration and viewing enrolled courses
 */
@WebServlet("/student/*")
public class StudentServlet extends HttpServlet {
    // doGet(): Shows student dashboard and course registration
    // doPost(): Processes course registration
}
```

## 6. Installation and Deployment Guide

### 6.1 Prerequisites
- Java JDK 11 or higher
- Apache Tomcat 9.x
- MySQL Server 8.x
- Maven 3.6+

### 6.2 Setup Steps

1. **Database Setup**:
   ```sql
   CREATE DATABASE course_management;
   USE course_management;
   -- Execute schema from section 3.2
   ```

2. **Database Configuration**:
   ```properties
   # Update database.properties
   db.url=jdbc:mysql://localhost:3306/course_management
   db.user=root
   db.password=your_password
   ```

3. **Build Application**:
   ```bash
   mvn clean package
   ```

4. **Deploy to Tomcat**:
   ```bash
   cp target/CourseManagementSystem.war $TOMCAT_HOME/webapps/
   ```

5. **Start Tomcat**:
   ```bash
   $TOMCAT_HOME/bin/startup.sh
   ```

6. **Access Application**:
   ```
   http://localhost:8080/CourseManagementSystem/login
   ```

### 6.3 Test Data Setup
```sql
-- Admin user
INSERT INTO users (username, password, role, name, email) VALUES
('admin', 'admin123', 'admin', 'System Admin', 'admin@school.edu');

-- Teacher users
INSERT INTO users (username, password, role, name, email) VALUES
('teacher1', 'teacher123', 'teacher', 'John Smith', 'john@school.edu'),
('teacher2', 'teacher123', 'teacher', 'Sarah Johnson', 'sarah@school.edu');

-- Student users  
INSERT INTO users (username, password, role, name, email) VALUES
('student1', 'student123', 'student', 'Alice Brown', 'alice@student.edu'),
('student2', 'student123', 'student', 'Bob Wilson', 'bob@student.edu');
```

## 7. User Manual

### 7.1 Administrator Guide

**Login**: Use admin credentials (admin/admin123)

**Features**:
1. **Dashboard**: View system statistics
2. **Add Course**: 
   - Click "Add New Course" 
   - Fill course details
   - Select teacher from dropdown
   - Submit to create course

### 7.2 Teacher Guide

**Login**: Use teacher credentials (teacher1/teacher123)

**Features**:
1. **View Courses**: See assigned courses
2. **View Students**: Click "View Students" to see enrolled students per course

### 7.3 Student Guide

**Login**: Use student credentials (student1/student123)

**Features**:
1. **View Enrolled Courses**: See current course registrations
2. **Register for Courses**: 
   - Click "Register for a Course"
   - Select from available courses
   - Click "Register" button

## 8. Security Considerations

### 8.1 Authentication
- Password validation against database
- Session-based authentication
- Automatic logout after 30 minutes of inactivity

### 8.2 Authorization
- Role-based access control
- Servlet-level authorization checks
- Prevention of privilege escalation

### 8.3 Session Management
- HttpSession for user state management
- Session invalidation on logout
- Prevention of session fixation attacks

## 9. Error Handling

### 9.1 Database Errors
- SQLException handling in DAO classes
- Connection pool management
- Graceful error messages

### 9.2 User Input Validation
- Form validation in JSP pages
- Server-side validation in servlets
- SQL injection prevention using PreparedStatement

### 9.3 Session Errors
- Session timeout handling
- Invalid session redirection to login
- User-friendly error messages

## 10. Limitations and Future Enhancements

### 10.1 Current Limitations
- No password encryption (stored in plain text)
- Limited input validation
- Basic error handling
- No email notifications
- No file upload capability

### 10.2 Proposed Enhancements
1. **Security Improvements**:
   - Password encryption using BCrypt
   - HTTPS enforcement
   - CSRF protection

2. **Functionality Additions**:
   - Course materials upload/download
   - Assignment submission system
   - Grade management
   - Email notifications

3. **User Experience**:
   - Responsive design improvements
   - Advanced filtering and search
   - Bulk operations
   - Export functionality

## 11. Testing Methodology

### 11.1 Test Cases Executed

**Authentication Tests**:
- ✓ Valid login credentials
- ✓ Invalid login credentials
- ✓ Session timeout
- ✓ Logout functionality

**Authorization Tests**:
- ✓ Role-based access control
- ✓ URL manipulation prevention
- ✓ Cross-role access attempts

**Functionality Tests**:
- ✓ Course creation by admin
- ✓ Course assignment to teachers
- ✓ Student course registration
- ✓ Duplicate enrollment prevention
- ✓ Teacher student viewing

### 11.2 Browser Compatibility
- ✓ Chrome 90+
- ✓ Firefox 88+
- ✓ Safari 14+
- ✓ Edge 90+

## 12. Conclusion

The Course Management System successfully implements all required functionalities using Java Servlets and JSP technology. The system provides a solid foundation for online course management with clear separation of concerns through the MVC architecture. While the current implementation meets all specified requirements, there are several areas identified for future enhancement to improve security, functionality, and user experience.

---

**Appendix A: Sample Screenshots**  
*[Include screenshots of login page, admin dashboard, teacher view, etc.]*

**Appendix B: Database Dump**  
*[Include complete database schema and sample data]*

**Appendix C: Source Code**  
*[Reference to complete source code repository]*
