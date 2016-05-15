package ua.rd.pizzaservice.web;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloController implements Controller {

	@Override
	public void handleRequest(HttpServletRequest req, HttpServletResponse resp) {
		try (PrintWriter out = resp.getWriter()) {
			out.println("Hello from Dispatcher");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
