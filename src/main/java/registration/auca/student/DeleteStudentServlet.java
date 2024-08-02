package registration.auca.student;

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

@WebServlet("/delete")
public class DeleteStudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int student_id = Integer.parseInt(req.getParameter("studentId"));

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        Connection con = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/auca_db", "MusaNestor", "N");

            String query = "DELETE FROM students WHERE student_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, student_id);

            int result = ps.executeUpdate();

            out.println("<html><body>");
            if (result > 0) {
                out.println("<h2>Student deleted successfully!</h2>");
            } else {
                out.println("<h2>Error deleting student!</h2>");
            }
            out.println("</body></html>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body>");
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
            out.println("</body></html>");
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
