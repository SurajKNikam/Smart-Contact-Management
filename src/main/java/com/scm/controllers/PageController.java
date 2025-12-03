package com.scm.controllers;

import javax.naming.Binding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/home")
	public String home(Model model) {
		
		
		//sending to view
		model.addAttribute("name", "Sunshine");
		model.addAttribute("GithubRepo","https://github.com/SurajKNikam");
		model.addAttribute("YT","SurajNikam");
		System.out.println("HOme page handler");
		return "home";
	}

	@GetMapping("/about")
	public String about() {
		
		return "about";
	}
	
	@GetMapping("/services")
	public String Services() {
		
		return "Services";
	}
	
	@GetMapping("/contact")
	public String Contact() {
		
		return "contact";
	}
	
	//this is registration controller view page
	@GetMapping("/login")
	public String Login() {
		
		return "login";
	}
	
	//process registration
	@GetMapping("/register")
	public String register(Model model) {
		UserForm userform=new UserForm();
		userform.setName("Suraj");
		userform.setEmail("suraj@gmail.com");
//	
		model.addAttribute("userform",userform);
		//we can insert default data also
	
	
		return "register";
	}
	
	
	//processing register
	@PostMapping("/do-register")
	public String ProcessRegister(@Valid @ModelAttribute UserForm userform, BindingResult rBindingResult,  HttpSession session,Model model ) {
		System.out.println("registreing..");
		//fetch form data
		//userform
		
		
		System.out.println(userform);
		
		//validate data
		if(rBindingResult.hasErrors()) {
			model.addAttribute("userform", userform);
			return "register";
		}
		
		//save data
		//userservice
//		User user=User.builder()
//				.name(userform.getName())
//				.email(userform.getEmail())
//				.password(userform.getPassword())
//				.about(userform.getAbout())
//				.phoneNumber(userform.getPhoneNumber())
//				.profilePic("https://api.dicebear.com/9.x/identicon/svg?seed="+userform.getName())
//				.build();
		User user = new User();
		user.setName(userform.getName());
		user.setEmail(userform.getEmail());
		user.setPassword(userform.getPassword());
		user.setAbout(userform.getAbout());
		user.setPhoneNumber(userform.getPhoneNumber());
		user.setEnabled(false);
		user.setProfilePic("https://api.dicebear.com/9.x/identicon/svg?seed="+userform.getName());
		User savedUser = userService.saveUser(user);
	   
	   System.out.println("User saved");
		
		//message =resgistration successful
	   //add message
	   Message message=Message.builder().content("Registration succesful").type(MessageType.green).build();
	   session.setAttribute("message",message);
		//redirect login
		return "redirect:/register";
	}
	
	
	//MESSAGE TO SEND ON EMAIL
	@Autowired
	private org.springframework.mail.javamail.JavaMailSender mailSender;

	@PostMapping("/contact")
	public String handleContactForm(
	        @RequestParam("name") String name,
	        @RequestParam("email") String email,
	        @RequestParam("message") String message,
	        HttpSession session) {

	    try {
	        // Create email content
	        org.springframework.mail.SimpleMailMessage mailMessage = new org.springframework.mail.SimpleMailMessage();
	        mailMessage.setTo("your_email@gmail.com"); // change this to your own email
	        mailMessage.setSubject("New Contact Message from " + name);
	        mailMessage.setText(
	                "Name: " + name + "\n" +
	                "Email: " + email + "\n\n" +
	                "Message:\n" + message
	        );

	        // Send the email
	        mailSender.send(mailMessage);

	        session.setAttribute("message",
	                Message.builder()
	                        .content("✅ Message sent successfully! We'll reach out soon.")
	                        .type(MessageType.green)
	                        .build());
	    } catch (Exception e) {
	        session.setAttribute("message",
	                Message.builder()
	                        .content("❌ Failed to send message. Please try again later.")
	                        .type(MessageType.red)
	                        .build());
	        e.printStackTrace();
	    }

	    return "redirect:/contact";
	}

	
}


