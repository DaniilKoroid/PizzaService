package ua.rd.pizzaservice.web;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.service.pizza.PizzaService;

@org.springframework.stereotype.Controller
public class GetPizzaController implements Controller {

	@Autowired
	private PizzaService pizzaService;
	
	
	@Override
	public void handleRequest(HttpServletRequest req, HttpServletResponse resp) {
		try (PrintWriter out = resp.getWriter()) {
			Pizza pizza = pizzaService.getPizzaByID(2);
			out.println(pizza);
			out.println("Hello from Dispatcher");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		// TODO Auto-generated method stub

	}

}
