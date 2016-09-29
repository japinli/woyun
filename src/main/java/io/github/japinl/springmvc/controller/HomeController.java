package io.github.japinl.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("greeting", "Welcome to Home");
		model.addAttribute("tagline", "The one and only amazing Spring Test");
		
		return "index";
	}
	
	@RequestMapping("/welcome")
	public String welcome(Model model) {
		model.addAttribute("greeting", "Welcome to Spring Test");
		model.addAttribute("tagline", "The one and only amazing Spring Test");
		
		return "welcome";
	}
}
