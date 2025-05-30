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

@WebServlet("/modelsbyproductline")
public class ModelsByProductLine extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String consulta = "select * from products where productline = ?";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/json");
		PrintWriter out = new PrintWriter(resp.getWriter());
		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(out);
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/classicmodels");
			con = ds.getConnection();
			PreparedStatement statement = con.prepareStatement(consulta);
			statement.setString(1, "Motorcycles");
			rs = statement.executeQuery();
			generator.writeStartObject();
			while (rs.next()) {
				Product product = new Product(
					rs.getString("productCode"),
					rs.getString("productName"),
					rs.getString("productLine"),
					rs.getString("productScale"),
					rs.getString("productVendor"),
					rs.getString("productDescription"),
					rs.getInt("quantityInStock"),
					rs.getFloat("buyPrice"),
					rs.getFloat("MSRP"));
				generator.writeObject(product);
//				generator.writeStringField("productCode", rs.getString("productCode"));
//				generator.writeStringField("productName", rs.getString("productName"));
//				generator.writeStringField("productLine", rs.getString("productLine"));
//				generator.writeStringField("productScale", rs.getString("productScale"));
//				generator.writeStringField("productVendor", rs.getString("productVendor"));
//				generator.writeStringField("productDescription", rs.getString("productDescription"));
//				generator.writeNumberField("quantityInStock", rs.getInt("quantityInStock"));
//				generator.writeNumberField("buyPrice", rs.getFloat("buyPrice"));
//				generator.writeNumberField("MSRP", rs.getFloat("MSRP"));
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
		doGet(request, response);
	}

}
