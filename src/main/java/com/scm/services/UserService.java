package com.scm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.scm.entities.User;

@Repository
public interface UserService {

	User saveUser(User user);
	
	Optional<User> getUserById(String id);
	
	Optional<User> updateUser(User user);
	
	void deleteUser(String id);
	
	boolean isUserExists(String userId);
	
	boolean isUserExistByEmail(String email);
	
	List<User>getAllUsers();
	
	User getUserByEmail(String email);
	
}
