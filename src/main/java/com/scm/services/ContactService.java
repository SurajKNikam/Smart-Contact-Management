package com.scm.services;

import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

	//save contact
	Contact save(Contact contact);
	
	//update contact
	Contact update(Contact contact);
	
	//get contact
	List<Contact>getAll();
	
	//get contact by id
	Contact getById(String id);
	
	//delete contact
	void delete(String id);
	
	//serach contact
	Page<Contact>searchByName(String nameKeyword,int size,int page,String sortBy,String order);
	
	Page<Contact>searchByEmail(String emailKeyword,int size,int page,String sortBy,String order);
	
	Page<Contact>searchByPhoneNumber(String phoneNumberKeyword,int size,int page,String sortBy,String order);
	
	//get contact by user id
	List<Contact>getByUserId(String userId);
	
	//get by userid
	Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction);
}
