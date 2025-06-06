import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api")
public class APIRestSevlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String consulta = "select numero from telefonos where contacto = ?";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		String val;
		PrintWriter out = new PrintWriter(resp.getWriter());
		if ((val = req.getParameter("buscar")) != null)
			buscar(val, out);			
		else if ((val = req.getParameter("buscar")) != null)
			listar(out);
	}
		
	static void buscar(String nombre, PrintWriter out) {
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			con = ds.getConnection();
			PreparedStatement statement = con.prepareStatement(consulta);
			statement.setString(1, nombre);
			rs = statement.executeQuery();
			while (rs.next())
				out.println(rs.getString(1));
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	static void listar(PrintWriter out) {
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}

}
