import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/prueba")
public class Prueba extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	/*
	 * GET. Solicita un recurso en la URL de la solicitud. Se usa para consultar información.
	 * POST. Envía información al servicio para su procesamiento, creando un nuevo registro.
	 * PUT. Actualiza un registro o recurso existente.
	 * PATCH. Como el PUT, pero permite actualizar solo una fracción del registro.
	 * DELETE. Elimina un registro existente.
	 * HEAD. Obtiene información de un registro.
	 */
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json");
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
