package com.gdpdemo.GDPSprint1Project.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.PostsAuthors;
import com.gdpdemo.GDPSprint1Project.PostsAuthorsId;
import com.gdpdemo.GDPSprint1Project.Repository.HomeRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsAuthorsRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsRepository;
import com.gdpdemo.GDPSprint1Project.service.PostsAuthorsManager;

import javax.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PostsAuthorsInMemoryService implements PostsAuthorsManager {

	@Autowired
	private PostsAuthorsRepository postsAuthorsRepository;

	@Autowired
	private PostsRepository postsRepository;

	@Autowired
	private HomeRepository homeRepository;

	// Method to save the relationship between a post and an author
	@Override
	public void save(int id_post, int id_author) {
	    // Create PostsAuthorsId using post and author IDs
	    PostsAuthorsId postsAuthorsId = new PostsAuthorsId(id_post, id_author);
	    // Create PostsAuthors object with respective post and author entities
	    PostsAuthors postsAuthors = new PostsAuthors(postsAuthorsId, postsRepository.findById(id_post).get(),
	            homeRepository.findById(id_author).get());
	    // Save the relationship
	    postsAuthorsRepository.save(postsAuthors);
	}
	// Method to remove post-author relationships by post ID
	@Override
	public void removeByPost(int id_post) {
		postsAuthorsRepository.deletePostsAuthors(id_post);
	}
	// Method to retrieve all post-author relationships
	@Override
	public List<PostsAuthors> getAllPostsAuthors() {
		return postsAuthorsRepository.findAll();
	}
	// Method to retrieve posts by authors based on category, year, and month with pagination
	@Override
	public Page<Posts> getAllPostsByAuthors(int id_author,String category,int year, int month,Pageable pageable) {
		Set<Posts> postsSet = new HashSet<>();
	    // Iterate through PostsAuthors to find posts associated with the author and meeting category, year, and month criteria

		for (PostsAuthors pa : postsAuthorsRepository.findAll()) {
			if (pa.getId_author().getId() == id_author) {
				System.out.println(pa.getId_post().getId());
				Posts post = pa.getId_post();
				System.out.println(pa.getId_post().getCategory());
				if(pa.getId_post().getCategory().contains(category)) {
					LocalDate postDate = post.getDate();
					 if (postDate != null) {
					if (postDate.getYear() == year && postDate.getMonthValue() == month) {
	                    System.out.println(pa.getId_post().getId());
	                    postsSet.add(post);
	                }
					 }
				}
			}
		}
		List<Posts> postsList = new ArrayList<>(postsSet);
	    // Create a Page object containing posts meeting the criteria with pagination

		Page<Posts> pageOfPosts = new PageImpl<>(postsList, pageable, postsList.size());
		return pageOfPosts;
	}

}



