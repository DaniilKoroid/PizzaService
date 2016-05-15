package ua.rd.pizzaservice.web.infrastructure;

import ua.rd.pizzaservice.web.Controller;

public interface HandlerMapping {

	Controller getController(String url);
}
