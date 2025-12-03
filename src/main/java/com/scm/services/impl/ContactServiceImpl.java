package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.repositories.UserRepo;
import com.scm.services.ContactService;

import jakarta.transaction.Transactional;

@Service
public class ContactServiceImpl implements ContactService{

	@Autowired
	private ContactRepo contactRepo;
	
	@Override
	public Contact save(Contact contact) {

		 if (contact.getId() == null) {
		        contact.setId(UUID.randomUUID().toString());
		    }
		    return contactRepo.save(contact);
	}

	@Override
	public Contact update(Contact contact) {
		 Contact existing = contactRepo.findById(contact.getId())
		            .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + contact.getId()));

		    // update fields
		    existing.setName(contact.getName());
		    existing.setEmail(contact.getEmail());
		    existing.setPhoneNumber(contact.getPhoneNumber());
		    existing.setAddress(contact.getAddress());
		    existing.setDescription(contact.getDescription());
		    existing.setFavourite(contact.isFavourite());
		    existing.setWebsiteLink(contact.getWebsiteLink());
		    existing.setLinkedIn(contact.getLinkedIn());
		    existing.setPicture(contact.getPicture());
		    existing.setCloudinaryImagepublicId(contact.getCloudinaryImagepublicId());

		    return contactRepo.save(existing);
	}

	@Override
	public List<Contact> getAll() {
		return contactRepo.findAll();
	}

	@Override
	public Contact getById(String id) {
		return contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not found with id "+id));
	}

//	@Override
//	public void delete(String id) {
//	    Contact contact = contactRepo.findById(id)
//	        .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id " + id));
//	    
//	    // remove child entities first (optional but clean)
//	    contact.getLinks().clear();
//	    
//	    // delete the contact entity
//	    contactRepo.delete(contact);
//	}
	
	@Override
	@Transactional
	public void delete(String id) {
	    Contact contact = contactRepo.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));

	    User user = contact.getUser();
	    if (user != null) {
	        // remove contact from user's list to avoid orphan issue
	        user.getContacts().remove(contact);
	    }

	    // clear linked social links
	    contact.getLinks().clear();

	    contactRepo.delete(contact);
	}



	

	@Override
	public List<Contact> getByUserId(String userId) {
      return contactRepo.findByUserId(userId);
	}

	@Override
	public Page<Contact> getByUser(User user,int page,int size,String sortBy,String direction) {
		
		Sort sort=direction.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		
		
		var pageable=PageRequest.of(page, size,sort);
		
		return contactRepo.findByUser(user,pageable);
		
	}

	@Override
	public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order) {
		// TODO Auto-generated method stub
		Sort sort=order.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
		var pageable=PageRequest.of(page, size,sort);
		return contactRepo.findByNameContaining(nameKeyword, pageable);
	}

	@Override
	public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order) {
		// TODO Auto-generated method stub
		Sort sort=order.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
		var pageable=PageRequest.of(page, size,sort);
		return contactRepo.findByEmailContaining(emailKeyword, pageable);
	}

	@Override
	public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
			String order) {
		// TODO Auto-generated method stub
		Sort sort=order.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
		var pageable=PageRequest.of(page, size,sort);
		return contactRepo.findByPhoneNumberContaining(phoneNumberKeyword, pageable);
	}

	

}
