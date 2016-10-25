package io.github.japinl.woyun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping(value = "/view-test")
	public ModelAndView viewTest() {
		
		return new ModelAndView();
	}
}
