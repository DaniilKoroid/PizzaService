package ua.rd.pizzaservice.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ua.rd.pizzaservice.domain.Pizza;
import ua.rd.pizzaservice.domain.Pizza.PizzaType;
import ua.rd.pizzaservice.service.PizzaService;

@Controller
public class PizzaCrudController {

	@Autowired
	private PizzaService pizzaService;
	
	@RequestMapping(value = "/")
	public String redirectToPizzas() {
		return "redirect:/app/pizzas";
	}
	
	@RequestMapping(value = "/pizzas", method = RequestMethod.GET)
	public String listPizzas(Model model) {
		List<Pizza> pizzas = pizzaService.getAllPizzas();
		model.addAttribute("pizzas", pizzas);
		return "/pizzas/assortiment";
	}

	@RequestMapping(value = "/pizzas", method = RequestMethod.POST)
	public String createPizza(@ModelAttribute("pizzaForm") Pizza pizza) {
		pizzaService.update(pizza);
		return "redirect:/app/pizzas";
	}
	
	@RequestMapping("/pizzas/create")
	public String createPizza(Model model) {
		model.addAttribute("pizzaForm", new Pizza());
		model.addAttribute("create", false);
		model.addAttribute("pizzaTypes", PizzaType.values());
		return "/pizzas/pizza";
	}
	
	@RequestMapping("/pizzas/update/{id}")
	public String updatePizza(@PathVariable("id") Integer id, Model model) {
		Pizza pizza = pizzaService.getPizzaByID(id);
		model.addAttribute("pizzaForm", pizza);
		model.addAttribute("pizzaTypes", PizzaType.values());
		return "/pizzas/pizza";
	}
	
	@RequestMapping("/pizzas/delete/{id}")
	public String deletePizza(@PathVariable("id") Integer id) {
		Pizza pizza = pizzaService.getPizzaByID(id);
		pizzaService.delete(pizza);
		return "redirect:/app/pizzas";
	}
	
}
