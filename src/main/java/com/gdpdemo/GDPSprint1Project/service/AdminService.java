
package com.gdpdemo.GDPSprint1Project.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Admin;
//import com.gdpdemo.GDPSprint1Project.AdminPostsDTO;
import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.Repository.AdminRepository;

@Service
public class AdminService {
//List<Admin> getAllUsers();

private final AdminRepository adminRepository;
public List<Posts> posts; 
public AdminService(AdminRepository adminRepository) {
    this.adminRepository = adminRepository;
}

 void save(Posts post) {
	adminRepository.save(post);
}
public List<Posts> getAllUsers() {
	// TODO Auto-generated method stub
	return adminRepository.findAll();
}
 public Optional<Posts> findById(int id) {
	return adminRepository.findById(id);
}
 public List<Posts> getDeclinedUsers() {
		// TODO Auto-generated method stub
		return adminRepository.findPostsByStatus("DECLINED");
	}
}
