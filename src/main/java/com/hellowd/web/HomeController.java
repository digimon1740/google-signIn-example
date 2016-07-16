/**
 * @author SangHoon, Lee(digimon.1740@gmail.com)
 */
package com.hellowd.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController extends RootController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/", method = RequestMethod.GET)
	public String home() throws Exception {
		return "redirect:index.html";
	}

}
