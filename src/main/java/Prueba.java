import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

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

@WebServlet("/prueba")
public class Prueba extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String consulta = "select * from products where productline = ?";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.setContentType("text/json");
		PrintWriter out = new PrintWriter(response.getWriter());
		out.println("respuesta a GET");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getWriter());
		out.println("respuesta a POST");
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Enumeration<String> parametros = request.getParameterNames();
		PrintWriter out = new PrintWriter(response.getWriter());
		out.println("respuesta a PUT: ");
		while (parametros.hasMoreElements()) {
			String param = parametros.nextElement();
			String valor = (String) request.getParameter(param);
			out.println(param + "= " + valor);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = new PrintWriter(response.getWriter());
		out.println("respuesta a DELETE");
	}

}
