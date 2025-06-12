import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

public class Cliente {

	public static void main(String[] args) {
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		String entrada = "";

		System.out.println("-------------------------------------------");
		System.out.println("       BIENVENIDO A AGENDA CONTACTOS");
		System.out.println("-------------------------------------------");
		System.out.println("ACCIONES:");
		System.out.println(" BUSCAR -> buscar:nombre");
		System.out.println(" MOSTRAR CONTACTOS -> mostrar");
		System.out.println(" BORRAR CONTACTO -> borrar:nombre");
		System.out.println(" AÑADIR CONTACTO -> añadir:nombre:telefono:email");
		System.out.println(" SALIR -> salir");
		System.out.println("");

		try {
			while (true) {
				System.out.print("Introduce un comando: ");
				entrada = teclado.readLine();
				if (entrada == null || entrada.trim().equalsIgnoreCase("salir"))
					break;

				String[] partes = entrada.trim().split(":");
				String comando = partes[0].toLowerCase();
				String respuesta = null;

				switch (comando) {
				case "buscar":
					if (partes.length != 2) {
						System.out.println("Formato incorrecto. Uso: buscar:nombre");
					} else {
						respuesta = request("GET", "http://localhost:8080/servletsapirest/api", "buscar=" + partes[1]);
					}
					break;

				case "mostrar":
					respuesta = request("GET", "http://localhost:8080/servletsapirest/api", "contactos");
					break;

				case "borrar":
					if (partes.length != 2) {
						System.out.println("Formato incorrecto. Uso: borrar:nombre");
					} else {
						respuesta = request("DELETE", "http://localhost:8080/servletsapirest/api",
								"borrar=" + partes[1]);
					}
					break;

				case "añadir":
					if (partes.length != 4) {
						System.out.println("Formato incorrecto. Uso: añadir:nombre:telefono:email");
					} else {
						String query = "nombre=" + partes[1] + "&telefono=" + partes[2] + "&email=" + partes[3];
						respuesta = request("POST", "http://localhost:8080/servletsapirest/api", query);
					}
					break;

				default:
					System.out.println("Comando no reconocido.");
					break;
				}

				if (respuesta != null) {
					System.out.println("Respuesta del servidor:");
					System.out.println(respuesta);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String request(String method, String url, String query) {
		String response = null;
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) new URI(url + "?" + query).toURL().openConnection();
			con.setRequestMethod(method);
			int responseCode = con.getResponseCode();

			if (responseCode >= 200 && responseCode < 300) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				response = in.lines().collect(Collectors.joining("\n"));
			} else {
				System.out.println(responseCode);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				con.disconnect();
		}
		return response;
	}

}
