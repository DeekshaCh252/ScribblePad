package com.gdpdemo.GDPSprint1Project.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Posts;

@Service
public interface AdminManager {

   void save(Posts post);
    Posts findById(int id);
    List<Posts> getAllPostsOrderedStatus(int order, Map<Integer, Integer> comments,
			Map<Integer, Integer> attachments, String status);
}
