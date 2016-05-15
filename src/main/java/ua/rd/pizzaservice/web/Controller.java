package ua.rd.pizzaservice.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {

	void handleRequest(HttpServletRequest req, HttpServletResponse resp);
}
