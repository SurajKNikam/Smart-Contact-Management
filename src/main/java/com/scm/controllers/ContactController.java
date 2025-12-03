package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {
    
	private Logger logger=LoggerFactory.getLogger(ContactController.class);
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private  ContactService contactService;
	
	@Autowired
	private UserService userService;
	
	//add contact page
	@RequestMapping("/add")
	public String addContactView(Model model) {
		
		ContactForm contactForm = new ContactForm();
//		contactForm.setName("Suraj Nikam");
		contactForm.setFavourite(true);
		model.addAttribute("contactForm",contactForm);
		return "user/add_contact";
	}
	
	
	@PostMapping("/add")
	public String saveContact(@Valid @ModelAttribute ContactForm contactForm,BindingResult result,Authentication authentication,HttpSession session) {
		//processs the form data
		String username=Helper.getEmailOfLoggedInUser(authentication);
		
		//validate form
		if(result.hasErrors()) {
			session.setAttribute("message",Message.builder()
					.content("Plesae correct the following errors")
					.type(MessageType.red).build());
			return "user/add_contact";
		}
		
		//form-------->contact
		User user = userService.getUserByEmail(username);
		//2.process conatct picture
		
		//image process
		//
		
		
		logger.info("File information:{}",contactForm.getContactImage().getOriginalFilename());
		
		String filename=UUID.randomUUID().toString();
		String fileURL=imageService.uploadImage(contactForm.getContactImage(),filename);
		
		Contact contact=new Contact();
		contact.setName(contactForm.getName());
		contact.setFavourite(contactForm.isFavourite());
		contact.setEmail(contactForm.getEmail());
		contact.setPhoneNumber(contactForm.getPhoneNumber());
		contact.setAddress(contactForm.getAddress());
		contact.setDescription(contactForm.getDescription());
		contact.setUser(user);
		contact.setLinkedIn(contactForm.getLinkedIn());
		contact.setWebsiteLink(contactForm.getWebsiteLink());
		contact.setPicture(fileURL);
		contact.setCloudinaryImagepublicId(filename);
		contactService.save(contact);
		System.out.println(contactForm);
		//3set the contact picture url
		
		//4.set message to be displayed on the 
		
		
		
		session.setAttribute("message",Message.builder()
				.content("You have successfull added a new contact")
				.type(MessageType.green).build());
		
		System.out.println("hello....**");
		return "redirect:/user/contacts/add";
		
	}
	
	//view contacts
	@RequestMapping
	public String viewContacts(@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "size",defaultValue = "1") int size,
			@RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
			@RequestParam(value = "direction",defaultValue = "asc")String direction,
			Model model,Authentication authentication,HttpSession session) {
		//load all contacts
		String username=Helper.getEmailOfLoggedInUser(authentication);
		
		User user=userService.getUserByEmail(username);
		
	Page<Contact> pageContact=contactService.getByUser(user,page,size,sortBy,direction);
	model.addAttribute("currentPage", page);

	model.addAttribute("pageContact",pageContact);
	
	 Object message = session.getAttribute("message");
	    if (message != null) {
	        model.addAttribute("message", message);
	        session.removeAttribute("message");
	    }
		
		return "user/contacts";
		
	}
	
	//search handler
	
	@GetMapping("/search")
	public String searchHandler(
			@RequestParam("field") String field,
			@RequestParam("keyword")String value,
			@RequestParam(value="size",defaultValue ="3" )int size,
			@RequestParam(value = "page",defaultValue = "0")int page,
			@RequestParam(value = "sortBy",defaultValue = "name") String sortBy,
			@RequestParam(value = "direction",defaultValue = "asc") String direction,
			Model model
			) {
		
		logger.info("field {} keyword {}",field,value);
		
    Page<Contact>pageContact=Page.empty();
    value=value.trim();
   if(field.equalsIgnoreCase("name")) {
	   pageContact= contactService.searchByName(value,size, page,sortBy, direction);
   }
   else if(field.equalsIgnoreCase("email")) {
	   pageContact= contactService.searchByEmail(value,size, page,sortBy, direction);

   }
   else if(field.equalsIgnoreCase("phoneNumber")) {
	   pageContact= contactService.searchByPhoneNumber(value,size, page,sortBy, direction);

   }else {
	   pageContact=Page.empty();
   }
   
   logger.info("pageContact {}",pageContact);
   
   model.addAttribute("pageContact",pageContact);
   model.addAttribute("currentPage", page);
   model.addAttribute("field", field);      
   model.addAttribute("keyword", value);


		return "user/search";
		
		
	}
	
	//delete contacts
	@GetMapping("/delete/{contactId}")
	public String DeleteContacts(@PathVariable String contactId,HttpSession session) {
		
		 contactService.delete(contactId);
		    session.setAttribute("message",
		        Message.builder()
		               .content("Contact deleted successfully")
		               .type(MessageType.green)
		               .build());
		return "redirect:/user/contacts?page=0";
		
	}
	
	//update contact  form view
	//1.post to save
	@GetMapping("/view/{contactId}")
	public String updateContactFormView(@PathVariable String contactId,Model model) {
		
		var contact=contactService.getById(contactId);
		
		ContactForm contactForm =new ContactForm();
		contactForm.setId(contact.getId());
		contactForm.setName(contact.getName());
		contactForm.setEmail(contact.getEmail());
		contactForm.setPhoneNumber(contact.getPhoneNumber());
		contactForm.setAddress(contact.getAddress());
		contactForm.setDescription(contact.getDescription());
		contactForm.setFavourite(contact.isFavourite());
		contactForm.setWebsiteLink(contact.getWebsiteLink());
		contactForm.setLinkedIn(contact.getLinkedIn());
		contactForm.setPicture(contact.getPicture());
		
		model.addAttribute("contactForm",contactForm);
		
		return "user/update_contact_view";
	}
	//2.get to get the form
	
	@PostMapping("/view/{contactId}")
	public String updateContact(@PathVariable String contactId,
	                            @Valid @ModelAttribute ContactForm contactForm,
	                            BindingResult result,
	                            HttpSession session) {

	    if (result.hasErrors()) {
	        return "user/update_contact_view"; // stay on form if validation fails
	    }

	    // fetch existing entity
	    Contact contact = contactService.getById(contactId); // must fetch existing
	    if (contact == null) {
	        session.setAttribute("message", "Contact not found");
	        return "redirect:/user/contacts";
	    }

	    // update only fields
	    contact.setName(contactForm.getName());
	    contact.setEmail(contactForm.getEmail());
	    contact.setPhoneNumber(contactForm.getPhoneNumber());
	    contact.setAddress(contactForm.getAddress());
	    contact.setDescription(contactForm.getDescription());
	    contact.setFavourite(contactForm.isFavourite());
	    contact.setWebsiteLink(contactForm.getWebsiteLink());
	    contact.setLinkedIn(contactForm.getLinkedIn());

	    // handle image if uploaded
	    if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
	        String filename = UUID.randomUUID().toString();
	        String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);
	        contact.setPicture(fileURL);
	        contact.setCloudinaryImagepublicId(filename);
	    }

	    contactService.save(contact); // ✅ save the existing entity, do NOT create a new one

	    session.setAttribute("message", "Contact updated successfully");
	    return "redirect:/user/contacts";
	}

}
