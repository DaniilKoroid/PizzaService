package ua.rd.pizzaservice.web.infrastructure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ua.rd.pizzaservice.web.Controller;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private HandlerMapping handlerMapping;
	
	private ConfigurableApplicationContext repContext;
	private ConfigurableApplicationContext appContext;
	private ConfigurableApplicationContext webContext;
	
	@Override
	public void init() throws ServletException {
		String contextConfigLocations = this.getServletContext().getInitParameter("contextConfigLocation");
		String[] contextConfigs = contextConfigLocations.split(" ");
		String webContextLocation = getInitParameter("contextConfigLocation");
		repContext = new ClassPathXmlApplicationContext(contextConfigs[0]);
		appContext = new ClassPathXmlApplicationContext(new String[] { contextConfigs[1] }, repContext);
		webContext = new ClassPathXmlApplicationContext(new String[] { webContextLocation }, appContext);
		handlerMapping = new SpringUrlHandlerMapping(webContext);
	}

	
	
	@Override
	public void destroy() {
		webContext.close();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURI();
		String controllerName = getControllerName(url);
		Controller controller = handlerMapping.getController(controllerName);
		
		if (controller != null) {
			controller.handleRequest(req, resp);
		}
		
	}
	
	private String getControllerName(String url) {
		String str = url.substring(url.lastIndexOf("/"));
		return str;
	}

}
