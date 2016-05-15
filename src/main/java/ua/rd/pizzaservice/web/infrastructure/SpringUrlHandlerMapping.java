package ua.rd.pizzaservice.web.infrastructure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import ua.rd.pizzaservice.web.Controller;
import ua.rd.pizzaservice.web.GetPizzaController;

public class SpringUrlHandlerMapping implements HandlerMapping {

	private ApplicationContext webContext;
	private Map<String, Controller> mapping = new HashMap<>();
	
	public SpringUrlHandlerMapping(ApplicationContext context) {
		this.webContext = context;
		mapping.put("/hello", webContext.getBean(GetPizzaController.class));
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
