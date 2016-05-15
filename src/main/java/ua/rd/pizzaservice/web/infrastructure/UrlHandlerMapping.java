package ua.rd.pizzaservice.web.infrastructure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.rd.pizzaservice.web.Controller;
import ua.rd.pizzaservice.web.HelloController;

public class UrlHandlerMapping implements HandlerMapping {

	private static Map<String, Controller> mapping = new HashMap<>();
	
	static {
		mapping.put("/hello", new HelloController());
	}
	
	@Override
	public Controller getController(String url) {
		Controller result = null;
		if (mapping.containsKey(url)) {
			result = mapping.get(url);
		} else {
			result = new Controller() {

				@Override
				public void handleRequest(HttpServletRequest req, HttpServletResponse resp) {
					try (PrintWriter out = resp.getWriter()) {
						out.println("Default");
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
			};
		}
		return result;
	}

}
