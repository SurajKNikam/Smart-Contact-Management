package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User implements UserDetails{

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public String getPassword() {
//		return password;
//	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

//	public boolean isEnabled() {
//		return enabled;
//	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean isPhoneVerified() {
		return phoneVerified;
	}

	public void setPhoneVerified(boolean phoneVerified) {
		this.phoneVerified = phoneVerified;
	}

	public Providers getProvider() {
		return provider;
	}

	public void setProvider(Providers provider) {
		this.provider = provider;
	}

	public String getProvideUserId() {
		return provideUserId;
	}

	public void setProvideUserId(String provideUserId) {
		this.provideUserId = provideUserId;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Id
	private String userId;
	
	@Column(name="user_name",nullable = false)
	private String name;
	
	@Column(unique = true,nullable = false)
	private String email;
	
	@Getter(value = AccessLevel.NONE)
	private String password;
	
	@Lob
	private String about;
	
    @Lob
	private String profilePic;
	
	private String phoneNumber;
	
	//information
	
	@Getter
	private boolean enabled=true;
	
	private boolean emailVerified=false;
	
	private boolean phoneVerified=false;
	
	
	//self,Google ,facebook,twitter,likedIn,Git
	@Enumerated(value = EnumType.STRING)
	private Providers provider=Providers.SELF;
	
	private String provideUserId;
	
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval = true)
	private List<Contact>contacts=new ArrayList<>();

	@ElementCollection(fetch=FetchType.EAGER)
	private List<String>roleList=new ArrayList<>();
	
	private String emailToken;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// list of users[user,admin]
		//collection of gratted authority--role adminor user
		
		Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
		return roles;
	}

	//for this project 
	//email id is username
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
		
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
		
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
		
	}
	@Override
	public boolean isEnabled() {
	 return this.enabled;
	}
	
	@Override
	public String getPassword() {
		
	 return this.password;
	}
	
}
