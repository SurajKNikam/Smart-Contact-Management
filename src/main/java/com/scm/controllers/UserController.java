package com.scm.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private Logger logger=LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	
	
	//user dasboard
	@GetMapping(value="/dashboard")
	public String UserDashboard() {
		
		return "user/dashboard";
		
	}
	
	//user profile
	@GetMapping(value="/profile")
	public String UserProfile(Model model,Authentication authentication) {

		String username= Helper.getEmailOfLoggedInUser(authentication);
		logger.info("User logged in",username);
		//get or fetch user from database 
		
		User user=userService.getUserByEmail(username);
		
		System.out.println(user.getName());
		System.out.println(user.getEmail());
		
		System.out.println("User Profile");
		
		model.addAttribute("LoggedInUser",user);
		
		return "user/profile";
		
	}
	
	//user add contact page
	
	//user view contact
	//
}
