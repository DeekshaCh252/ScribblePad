package com.gdpdemo.GDPSprint1Project.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Comments;

@Service
public interface CommentsManager {

	void save(Comments comment);

	Comments findById(int id);

	List<Comments> getAllComments();

	void remove(int id);

	void removeByPostId(int id);

	Map<Integer, Integer> getCommentsWithAmount();
}
