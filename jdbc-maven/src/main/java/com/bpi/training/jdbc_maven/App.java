package com.bpi.training.jdbc_maven;
import java.sql.*;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    // ---- SQL (Prepared Statements) ----
    private static final String CREATE_STUDENTS_TABLE = """
        CREATE TABLE IF NOT EXISTS students (
            id     SERIAL PRIMARY KEY,
            name   VARCHAR(200) NOT NULL,
            age    INTEGER NOT NULL CHECK (age >= 0),
            email  VARCHAR(320) NOT NULL UNIQUE
        )
        """;

    private static final String CREATE_COURSES_TABLE = """
        CREATE TABLE IF NOT EXISTS courses (
            id          SERIAL PRIMARY KEY,
            student_id  INTEGER NOT NULL REFERENCES students(id) ON DELETE CASCADE,
            course_name VARCHAR(200) NOT NULL,
            grade       VARCHAR(16) NOT NULL
        )
        """;

    private static final String INSERT_STUDENT =
        "INSERT INTO students(name, age, email) VALUES (?, ?, ?)";

    private static final String INSERT_COURSE =
        "INSERT INTO courses(student_id, course_name, grade) VALUES (?, ?, ?)";

    private static final String CHECK_STUDENT_EXISTS =
        "SELECT 1 FROM students WHERE id = ?";

    private static final String SELECT_STUDENTS =
        "SELECT id, name, age, email FROM students ORDER BY id";

    private static final String SELECT_COURSES_JOIN = """
        SELECT c.id, s.name AS student_name, c.course_name, c.grade
        FROM courses c
        JOIN students s ON s.id = c.student_id
        ORDER BY c.id
        """;

    public static void main(String[] args) {

        // Optional: create tables on first run. Comment out if you already created schema.
        ensureSchema();

        runMenu(); // rerunnable console menu
    }

    // ---------- Infra ----------





private static void ensureSchema() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(CREATE_STUDENTS_TABLE);
            st.execute(CREATE_COURSES_TABLE);
        } catch (SQLException e) {
            LOGGER.error("Failed to ensure schema.", e);
            throw new RuntimeException("Schema initialization failed.", e);}
        }


    // ---------- Menu ----------
    private static void runMenu() {
        try (Scanner sc = new Scanner(System.in)) {
            boolean done = false;
            while (!done) {
                printMenu();
                System.out.print("Choose an option: ");
                String option = sc.nextLine().trim();

                switch (option) {
                    case "1" -> addStudent(sc);
                    case "2" -> addCourse(sc);
                    case "3" -> displayAllStudents();
                    case "4" -> displayAllCourses();
                    case "0" -> {
                        System.out.println("Exiting Application. Goodbye!");
                        done = true;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
                System.out.println();
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== STUDENT COURSE MANAGEMENT =====");
        System.out.println("1. Add Student");
        System.out.println("2. Add Course");
        System.out.println("3. Show Students");
        System.out.println("4. Show Courses");
        System.out.println("0. Exit");
    }

    // ---------- Operations ----------

private static void addStudent(Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        Integer age = null;
        while (age == null) {
            System.out.print("Enter age: ");
            String s = sc.nextLine().trim();
            try {
                int parsed = Integer.parseInt(s);
                if (parsed < 0) System.out.println("Age must be greater than 0.");
                else age = parsed;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(INSERT_STUDENT)) {
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, email);
                ps.executeUpdate();
                conn.commit();
                System.out.printf("Student added successfully\n");
                displayAllStudents();
            } catch (SQLException e) {
                conn.rollback();
                if (isUniqueViolation(e)) {
                    System.out.println("Error: Email already exists.");
                } else if (isCheckViolation(e)) {
                    System.out.println("Error: Age must be >= 0.");
                } else {
                    System.out.println("Failed to add student. See logs.");
                }
                LOGGER.warn("Insert student failed", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error("DB error adding student.", e);
            System.out.println("Database error occurred.");
        }
    }



private static void addCourse(Scanner sc) {
        Integer studentId = null;
        while (studentId == null) {
            System.out.print("Enter student ID : ");
            String s = sc.nextLine().trim();
            try {
                studentId = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }

        System.out.print("Enter course name: ");
        String courseName = sc.nextLine().trim();

        System.out.print("Enter grade: ");
        String grade = sc.nextLine().trim();

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement chk = conn.prepareStatement(CHECK_STUDENT_EXISTS)) {
                    chk.setInt(1, studentId);
                    try (ResultSet rs = chk.executeQuery()) {
                        if (!rs.next()) {
                            System.out.println("No student found with that ID.");
                            conn.rollback();
                            return;
                        }
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(INSERT_COURSE)) {
                    ps.setInt(1, studentId);
                    ps.setString(2, courseName);
                    ps.setString(3, grade);
                    ps.executeUpdate();
                    conn.commit();
                    System.out.print("Course added successfully");
                }
            } catch (SQLException e) {
                conn.rollback();
                if (isForeignKeyViolation(e)) {
                    System.out.println("Error: Student ID does not exist.");
                } else {
                    System.out.println("Failed to add course. See logs.");
                }
                LOGGER.warn("Insert course failed", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            LOGGER.error("DB error adding course.", e);
            System.out.println("Database error occurred.");
        }
    }



private static void displayAllStudents() {
	System.out.printf("\nID | Name | Age | Email");
	System.out.printf("\n--------------------------------");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_STUDENTS);
             ResultSet rs = ps.executeQuery()) {
            int count = 0;
            while (rs.next()) {
                System.out.printf("\n%d | %s | %d | %s",
                    rs.getInt("id"), rs.getString("name"),
                    rs.getInt("age"), rs.getString("email"));
                count++;
            }
            if (count == 0) System.out.println("(no students yet)");
        } catch (SQLException e) {
            LOGGER.error("Error fetching students", e);
            System.out.println("Failed to load students.");
        }
    }



private static void displayAllCourses() {
	System.out.printf("\nID | Course Name | Grade | Student Name");
	System.out.printf("\n--------------------------------");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_COURSES_JOIN);
             ResultSet rs = ps.executeQuery()) {
            int count = 0;
            while (rs.next()) {
                System.out.printf("\n%d | %s | %s | %s",
                    rs.getInt("id"),
                    rs.getString("course_name"),
                    rs.getString("grade"),
                    rs.getString("student_name"));
                count++;
            }
            if (count == 0) System.out.println("(no courses yet)");
        } catch (SQLException e) {
            LOGGER.error("Error fetching courses", e);
            System.out.println("Failed to load courses.");
        }
    }


    // ---- Postgres-specific helpers ----
    // SQLState classes: https://www.postgresql.org/docs/current/errcodes-appendix.html
    private static boolean isUniqueViolation(SQLException e) {
        return "23505".equals(e.getSQLState()); // unique_violation
    }
    private static boolean isForeignKeyViolation(SQLException e) {
        return "23503".equals(e.getSQLState()); // foreign_key_violation
    }
    private static boolean isCheckViolation(SQLException e) {
        return "23514".equals(e.getSQLState()); // check_violation
    }
}
