package com.gdpdemo.GDPSprint1Project.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.Repository.PostsRepository;
import com.gdpdemo.GDPSprint1Project.service.PostsManager;

import net.bytebuddy.asm.Advice.This;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostsInMemoryService implements PostsManager {

	@Autowired
	private PostsRepository postsRepository;

	@Override
	public void save(Posts post) {
		postsRepository.save(post);
	}
	 // Implementation to sort and filter posts based on specified order, comments, attachments, and category
	@Override
	public List<Posts> getAllPostsOrdered(int order, Map<Integer, Integer> comments,
			Map<Integer, Integer> attachments,String category) {
		List<Posts> posts = postsRepository.findAll();
		
		List<Posts> list = new ArrayList<>();
		List<Integer> sorted;
		switch (order) {
		case 1:
			return posts;
		case 2:
			sorted = comments.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.map(Map.Entry::getKey).collect(Collectors.toList());
			for (Integer id : sorted) {
				for (Posts post : posts) {
					if (id == post.getId())
						list.add(post);
				}
			}
			return list;
		case 3:
			sorted = comments.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
					.collect(Collectors.toList());
			for (Integer id : sorted) {
				for (Posts post : posts) {
					if (id == post.getId())
						list.add(post);
				}
			}
			return list;
		case 4:
			sorted = attachments.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					.map(Map.Entry::getKey).collect(Collectors.toList());
			for (Integer id : sorted) {
				for (Posts post : posts) {
					if (id == post.getId())
						list.add(post);
				}
			}
			return list;
		case 5:
			sorted = attachments.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey)
					.collect(Collectors.toList());
			for (Integer id : sorted) {
				for (Posts post : posts) {
					if (id == post.getId())
						list.add(post);
				}
			}
			return list;
		default:
			posts.sort(Comparator.comparing(Posts::getId).reversed());
			return posts;
		}
	}
	 // Method to retrieve paged posts based on category and status
	@Override
	public Page<Posts> getAllPostsOrderedCategory(int order, Map<Integer, Integer> comments,
			Map<Integer, Integer> attachments,String category, String status,Pageable page) {
		 // Implementation to fetch paged posts based on category and status
		Page<Posts> posts= postsRepository.findPostsByCategoryStatus(category,status,page);
		
			return posts;
		}
	 // Method to find a post by its ID
	@Override
	public Posts findById(int id) {
		return postsRepository.findById(id).get();
	}
	// Method to remove a post by its ID
	@Override
	public void remove(int id) {
		postsRepository.deleteById(id);
	}

	public String split(String post_content) {
		String s = " ";
		if (post_content.length() > 150) {
			s = post_content.substring(0, 250);
		}
		return s;
		
	}

	
	  public Page<Posts> getPostByCategory(String category, Pageable pageable) {
	  
	  Page<Posts> posts = this.postsRepository.findPostsByCategory(category,pageable);
	  
	  return posts;
	  
	  }

	public List<Posts> getAllPosts(Integer pageNumber, Integer pageSize){
		
		
		Pageable p = PageRequest.of(pageNumber, pageSize);
		Page<Posts> pagePosts = this.postsRepository.findAll(p);
		List<Posts> allPosts = pagePosts.getContent();
		List<Posts> postDtos = allPosts.stream().collect(Collectors.toList());
		return postDtos;
	}
	@Override
	public Page<Posts> getAllPostsPages(int order, Map<Integer, Integer> comments,
			Map<Integer, Integer> attachments,String category, int year, int month,int id_author,Pageable pageable) {
		Page<Posts> posts = postsRepository.findPostsByCategoryStatusAndAuthor(category, id_author, year, month, pageable);
		/* Page<Posts> posts = this.postsRepository.findAll(pageable); */
		return posts;
	}
	@Override
	public Page<Posts> getAllPostsByFilter(int order, Map<Integer, Integer> comments,
			Map<Integer, Integer> attachments,String category, String status,int year, int month,Pageable pageable) {
		Page<Posts> posts = postsRepository.findPostsByCategoryStatusAndFilter(category, status, year, month, pageable);
		/* Page<Posts> posts = this.postsRepository.findAll(pageable); */
		return posts;
	}

	
}
