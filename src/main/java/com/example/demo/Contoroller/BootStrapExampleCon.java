package com.example.demo.Contoroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BootStrapExampleCon {
	
	@GetMapping("index")
	public String index(){
		return "bootstrapExam";
	}
}
