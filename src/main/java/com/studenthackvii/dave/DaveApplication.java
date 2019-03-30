package com.studenthackvii.dave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class DaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaveApplication.class, args);
	}

}

@Controller
class C {

	@RequestMapping("/")
	public String hellowWorld(Model m) {

		m.addAttribute("text", "hello world");
		return "index.html";
	}

}
