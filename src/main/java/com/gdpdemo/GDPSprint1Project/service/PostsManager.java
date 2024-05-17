package com.gdpdemo.GDPSprint1Project.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Posts;

@Service
public interface PostsManager {

	void save(Posts post);

	Posts findById(int id);

	List<Posts> getAllPostsOrdered(int order, Map<Integer, Integer> comments, Map<Integer, Integer> attachments,String category);
	
//	Page<Posts> getAllPostPages(int order, Map<Integer, Integer> comments, Map<Integer, Integer> attachments,String category,Pageable pageable);
	
	List<Posts> getAllPosts(Integer pageNumber, Integer pageSize);
	
	void remove(int id);

	String split(String post_content);

	Page<Posts> getPostByCategory(String category,Pageable pageable); 

	Page<Posts> getAllPostsPages(int order, Map<Integer, Integer> comments, Map<Integer, Integer> attachments,
			String category, int year, int month,int id_author,Pageable pageable);
	


	Page<Posts> getAllPostsOrderedCategory(int order, Map<Integer, Integer> comments, Map<Integer, Integer> attachments,
			String category, String status, Pageable page);

	Page<Posts> getAllPostsByFilter(int order, Map<Integer, Integer> comments, Map<Integer, Integer> attachments,
			String category, String status, int year, int month, Pageable pageable);

	
}

