package registration.auca.student;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/add")
public class FirstClass extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(FirstClass.class);

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int student_id = Integer.parseInt(req.getParameter("studentId"));
        String first_name = req.getParameter("firstName");
        String last_name = req.getParameter("lastName");

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        Connection con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/auca_db", "MusaNestor", "N");

            String query = "INSERT INTO students (student_id, first_name, last_name) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, student_id);
            ps.setString(2, first_name);
            ps.setString(3, last_name);

            int result = ps.executeUpdate();

            out.println("<html><body>");
            if (result > 0) {
                out.println("<h2>Student added successfully!</h2>");
                logger.info("Student added: ID=" + student_id + ", First Name=" + first_name + ", Last Name=" + last_name);
            } else {
                out.println("<h2>Error adding student!</h2>");
                logger.error("Error adding student: ID=" + student_id);
            }
            out.println("Your information: " + student_id + " " + first_name + " " + last_name);
            out.println("</body></html>");
        } catch (Exception e) {
            logger.error("Exception occurred: " + e.getMessage(), e);
            out.println("<html><body>");
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
            out.println("</body></html>");
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception during connection close: " + e.getMessage(), e);
            }
        }
    }
}
