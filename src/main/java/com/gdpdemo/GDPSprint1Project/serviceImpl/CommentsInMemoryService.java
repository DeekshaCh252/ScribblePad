package com.gdpdemo.GDPSprint1Project.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdpdemo.GDPSprint1Project.Comments;
import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.Repository.CommentsRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsRepository;
import com.gdpdemo.GDPSprint1Project.service.CommentsManager;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@DependsOn("postsInMemoryService")// Ensures dependency on another service
public class CommentsInMemoryService implements CommentsManager {

	private final List<Comments> commentsList;
	private final CommentsRepository commentsRepository;
	private final PostsRepository postsRepository;

	@Autowired
	public CommentsInMemoryService(CommentsRepository commentsRepository, List<Comments> commentsList,
			PostsRepository postsRepository) {
		this.commentsRepository = commentsRepository;
		this.commentsList = commentsList;
		this.postsRepository = postsRepository;
	}
	 // Save a comment related to a post
	@Override
	public void save(Comments comment) {
		comment.setPosts(postsRepository.findById(comment.getId_post()).get());
		commentsRepository.save(comment);
	}

	@Override
	public Comments findById(int id) {
		return commentsRepository.findById(id).get();
	}

	@Override
	public List<Comments> getAllComments() {
		return commentsRepository.findAll();
	}

	@Override
	public void remove(int id) {
		commentsRepository.deleteById(id);
	}

	@Override
	public void removeByPostId(int id) {
		commentsRepository.deleteAllByPostsId(id);
	}
	  // Method to retrieve comments count per post
	@Override
	public Map<Integer, Integer> getCommentsWithAmount() {
		List<Integer> integerList = new ArrayList<Integer>();
		for (Comments comments : getAllComments()) {
			integerList.add(comments.getId_post());
		}
		for (Posts post : postsRepository.findAll()) {
			integerList.add(post.getId());
		}
		Map<Integer, Integer> countMap = new HashMap<>();

		for (Integer item : integerList) {

			if (countMap.containsKey(item))
				countMap.put(item, countMap.get(item) + 1);
			else
				countMap.put(item, 1);
		}
		return countMap;
	}
	 // Method to save all comments
	public void saveAll() {
		commentsRepository.saveAll(commentsList);
	}
	 // Method to associate comments with their respective posts
	public void savePosts() {

		for (Comments c : commentsList) {
			c.setPosts(postsRepository.findById(c.getId_post()).get());
		}

	}
	 // Method executed after bean initialization to save posts and comments
	@PostConstruct
	public void init() {
		savePosts();
		saveAll();
	}
}
