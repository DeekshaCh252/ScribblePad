package com.gdpdemo.GDPSprint1Project.serviceImpl;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Home;
import com.gdpdemo.GDPSprint1Project.Repository.HomeRepository;
import com.gdpdemo.GDPSprint1Project.security.SecurityConfig;
import com.gdpdemo.GDPSprint1Project.service.DemoService;

//Service class implementation for the DemoService
@Service
public class DemoServiceImp implements DemoService {

// Instance variables
	@Autowired
	HomeRepository homeRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

// Method to save a user
	@Override
	public void save(Home user) {
		// Encoding the user's password before saving
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		user.setRePassword(hashedPassword);
		// Saving the user in the repository
		Home saved = homeRepository.save(user);
	}

// Method to verify password match
	@Override
	public boolean retrieve(Home user, String password) {
		// Comparing entered password with the stored encrypted password
		return passwordEncoder.matches(password, user.getPassword());
	}

}
