package servletexamples;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleAddProgramme extends HttpServlet {
    private Connection conn;
    private PreparedStatement pstmt;
    private String host = "jdbc:derby://localhost:1527/collegedb";
    private String user = "nbuser";
    private String password = "nbuser";

    // Initialize variables
    @Override
    public void init() throws ServletException {
        initializeJdbc();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Obtain parameters from the client
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String faculty = request.getParameter("faculty");

        try {
            if (code.length() == 0 || name.length() == 0) {
                out.println("Programme code and name are required.");
            } else {
                storeProgramme(code, name, faculty);
                out.println("New programme " + code + " " + name + " from the faculty "
                        + faculty + " has been added to the database.");
            }
        } catch (Exception ex) {
            out.println("ERROR: " + ex.getMessage());
        } finally {
            out.close();
        }
    }

    private void initializeJdbc() {
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(host, user, password);
            pstmt = conn.prepareStatement("INSERT INTO Programme VALUES(?, ?, ?)");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void storeProgramme(String code, String name, String faculty) throws SQLException {
        pstmt.setString(1, code);
        pstmt.setString(2, name);
        pstmt.setString(3, faculty);
        pstmt.executeUpdate();
    }
}