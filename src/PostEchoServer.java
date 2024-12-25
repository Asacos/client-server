import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class PostEchoServer {

	private static final int HTTP_PORT = 9090;
	private static Logger logger = Logger.getLogger(PostEchoServer.class.getName()); 
	
	public static void main(String[] args) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress("localhost", HTTP_PORT), 0);
			
			server.createContext("/", new IndexHandler());
			server.start();
			logger.info(" Server started on port " + HTTP_PORT);
		} catch (IOException e) {
			logger.severe("Error during request prepare");
			logger.severe(e.getMessage());
		}
	}
	
	static class IndexHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			logger.info("IndexHandler process");

			StringBuilder response = new StringBuilder();
			response.append("<!DOCTYPE html><html>");
			
			response.append("</head>");
			response.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
			response.append("<meta name=\"description\" content=\"Post echo page\">");
			response.append("<title>POST эхо-страница</title>");
			response.append("</head>");

			response.append("<body>");
			response.append("<h1>Страница отладки POST запроса</h1>");
			
			if(exchange.getRequestMethod().equalsIgnoreCase("POST")) {
				response.append("<p>Получен POST запрос.</p>");
				response.append("<p>Параметры запроса:</p>");

				InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(),"UTF-8");
	            BufferedReader br = new BufferedReader(isr);
	            String query = br.readLine();
	            query = URLDecoder.decode(query, "UTF-8");
	            String[] parameters = query.split("&");
				
	            if (parameters != null) {
	            	response.append("<ul>");
	            	
	            	for (String s : parameters) {
	            		response.append("<li>" + s);
					}
	            	
	            	response.append("</ul>");
	            }
	            
			} else {
				response.append("<p>Получен не POST запрос.</p>");
				response.append("<p>Получен запрос типа: " + exchange.getRequestMethod() + "</p>");
			}
			
			response.append("<button onclick=\"window.history.back();\">Go Back</button>");
			
			response.append("</body>");
			response.append("</html>");
			
			exchange.sendResponseHeaders(200, response.toString().getBytes().length);
		    OutputStream os = exchange.getResponseBody();
		    os.write(response.toString().getBytes());
		    os.close();
		}
	}
	
}
