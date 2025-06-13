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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api")
public class APIRestSevlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static String consulta = "select numero from telefonos where contacto = ?";
	private static String sqlListar = "select c.nombre, c.email, t.numero from contactos c left join telefonos t on t.contacto = c.nombre order by c.nombre";
	private static String sqlAñadirContactos = "insert ignore into contactos (nombre,email) values (?,?)";
	private static String sqlAñadirTelefonos = "insert into telefonos (numero,contacto) values (?,?)";
	private static String sqlBorrarTelefonos = "DELETE FROM telefonos WHERE contacto = ?";
	private static String sqlBorrarContacto = "DELETE FROM contactos WHERE nombre = ?";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		String val;
		PrintWriter out = new PrintWriter(resp.getWriter());
		if ((val = req.getParameter("buscar")) != null) {
			buscar(val, out);
			// Devuelve 200 al cliente indicando que todo a salido correctamente
			resp.setStatus(HttpServletResponse.SC_OK);
		} else {
			if ((val = req.getParameter("contactos")) != null) {
				listar(out);
				resp.setStatus(HttpServletResponse.SC_OK);
			}
		}
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
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			con = ds.getConnection();
			PreparedStatement statement = con.prepareStatement(sqlListar);
			rs = statement.executeQuery();
			while (rs.next()) {
				out.println(rs.getString("nombre"));
				out.println(rs.getString("email"));
				out.println(rs.getString("numero"));
			}
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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nombre = request.getParameter("nombre");
		String telefono = request.getParameter("telefono");
		String email = request.getParameter("email");

		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;

		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			conn = ds.getConnection();
			conn.setAutoCommit(false);

			ps1 = conn.prepareStatement(sqlAñadirContactos);
			ps1.setString(1, nombre);
			ps1.setString(2, email);
			ps1.executeUpdate();

			ps2 = conn.prepareStatement(sqlAñadirTelefonos);
			ps2.setString(1, telefono);
			ps2.setString(2, nombre);
			int filas = ps2.executeUpdate();

			conn.commit();
			// Devuelve un estado CREATED 201(indica que se a creado un nuevo recurso) y un
			// mensaje indicando que todo salió bien.
			response.setStatus(HttpServletResponse.SC_CREATED);
			response.getWriter().write("Se ha añadido " + filas + " contacto  correctamente");

		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps2 != null)
				try {
					ps2.close();
				} catch (SQLException ignored) {
				}
			if (ps1 != null)
				try {
					ps1.close();
				} catch (SQLException ignored) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignored) {
				}
		}

	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nombre = request.getParameter("borrar");

		if (nombre == null || nombre.trim().isEmpty()) {
			// Establece codigo 400 con un mensaje (solicitud mal formada/parametros invalidos)
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nombre del contacto no proporcionado");
			return;
		}

		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;

		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/contactos");
			conn = ds.getConnection();
			conn.setAutoCommit(false);

			ps1 = conn.prepareStatement(sqlBorrarTelefonos);
			ps1.setString(1, nombre);
			ps1.executeUpdate();

			ps2 = conn.prepareStatement(sqlBorrarContacto);
			ps2.setString(1, nombre);
			int filas = ps2.executeUpdate();

			conn.commit();

			if (filas > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("Contacto '" + nombre + "' eliminado correctamente.");
			} else {
				// Devuelve 404 al cliente(el servidor no encontro el recurso)
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter().write("No se encontró el contacto: " + nombre);
			}

		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps2 != null)
					ps2.close();
			} catch (SQLException ignored) {
			}
			try {
				if (ps1 != null)
					ps1.close();
			} catch (SQLException ignored) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException ignored) {
			}
		}

	}

}