package fr.projectfactory.manager.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@RequestMapping(method = RequestMethod.GET, path = {
		"/",
		"/account/**" })
	public String home() {
		return "index";
	}

}
