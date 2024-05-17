package com.gdpdemo.GDPSprint1Project;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "admin")
public class Admin {
	
	@Id
	@Column(nullable = false)
	@GeneratedValue(generator = "adm", strategy = GenerationType.IDENTITY)
	@SequenceGenerator(name = "adm")
	private int id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_post")
	Posts posts;
	
	// Default Constructor
	public Admin() {
		super();
	}

	// Constructor with parameters
	public Admin(int id, Posts posts) {
		this.id = id;
		this.posts = posts;
	}

	// Getters and Setters for Admin properties
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Posts getPosts() {
		return posts;
	}

	public void setPosts(Posts posts) {
		this.posts = posts;
	}

}
