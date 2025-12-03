package com.scm.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Contact {

	@Id
	private String id;
	
	private String name;
	
	private String email;
	
	private String phoneNumber;
	
	private String address;
	
	private String picture;
	
	@Lob
	private String description;
	
	private boolean favourite=false;
	
	private String linkedIn;
	
	private String websiteLink;
	
//	private List<String>socialLinks=new ArrayList<>();
	
	@ManyToOne
	@JsonIgnoreProperties("contacts")
	private User user;
	
	@OneToMany(mappedBy = "contact",cascade = CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval = true)
	private List<SocialLink>links=new ArrayList<SocialLink>();
	
	private String cloudinaryImagepublicId;
}
