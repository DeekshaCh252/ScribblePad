package com.gdpdemo.GDPSprint1Project.serviceImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.Repository.AdminRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsRepository;
import com.gdpdemo.GDPSprint1Project.service.AdminManager;
@Service
@Transactional
public class AdminInMemoryService   implements AdminManager{
	@Autowired	
	private AdminRepository admRepository;	
@Autowired
private PostsRepository postsRepository;
// Method to save a post, not implemented here
	@Override
	public void save(Posts post) {
		// TODO Auto-generated method stub
		
	}
	// Method to find a post by its ID, not implemented here
	@Override
	public Posts findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	 // Method to get all posts ordered by specified criteria
	@Override
	public List<Posts> getAllPostsOrderedStatus(int order, Map<Integer, Integer> comments,
			Map<Integer, Integer> attachments, String status) {
		List<Posts> posts = admRepository.findPostsByStatus(status);
		List<Posts> list = new ArrayList<>();
		List<Integer> sorted;
		switch (order) {
		case 1:
			return posts;
		  case 2:
              // Sorting by comments in descending order
              // Extracting post IDs based on comments count and arranging posts accordingly
              // Other cases follow a similar pattern
              sorted = comments.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                      .map(Map.Entry::getKey).collect(Collectors.toList());
              // Filtering and arranging posts based on the sorted IDs
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
			 // Cases 3 to 5 follow a similar pattern, sorting posts based on different criteria
            // Default case returns posts sorted by ID in descending order
		default:
			posts.sort(Comparator.comparing(Posts::getId).reversed());
			return posts;
		}
	}


}

