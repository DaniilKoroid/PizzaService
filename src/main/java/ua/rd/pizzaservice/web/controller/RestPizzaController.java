package ua.rd.pizzaservice.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ua.rd.pizzaservice.domain.pizza.Pizza;
import ua.rd.pizzaservice.service.pizza.PizzaService;

@RestController
@RequestMapping("/pizza")
public class RestPizzaController {

	private final PizzaService pizzaService;

	@Autowired
	public RestPizzaController(PizzaService pizzaService) {
		this.pizzaService = pizzaService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.FOUND)
	public List<Pizza> listPizzas() {
		return pizzaService.getAllPizzas();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pizza> find(@PathVariable Integer id) {
		Pizza pizza = pizzaService.getPizzaByID(id);
		if (pizza == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(pizza, HttpStatus.FOUND);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Pizza> update(@RequestBody Pizza pizza) {
		Pizza savedPizza = pizzaService.update(pizza);
		return new ResponseEntity<>(savedPizza, HttpStatus.CREATED);
	}
	
}
