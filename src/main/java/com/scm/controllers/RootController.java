package com.scm.controllers;

import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

	private Logger logger=org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public void addLoggedInUserInformation(Model model,Authentication authentication) {
		
		if(authentication==null) {
			return;
		}
		
		String username= Helper.getEmailOfLoggedInUser(authentication);
		logger.info("User logged in:{}",username);
		//get or fetch user from database 
		User user=userService.getUserByEmail(username);
		
		
			System.out.println(user.getName());
			System.out.println(user.getEmail());
			System.out.println("User with adding every page to model Profile");
			model.addAttribute("LoggedInUser",user);
		
		
		
	}
}
