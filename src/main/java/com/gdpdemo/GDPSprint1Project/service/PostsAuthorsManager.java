package com.gdpdemo.GDPSprint1Project.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.PostsAuthors;

@Service
public interface PostsAuthorsManager {

	void save(int id_post, int id_author);

	List<PostsAuthors> getAllPostsAuthors();

	Page<Posts> getAllPostsByAuthors(int id_author,String category,int year, int month, Pageable pageable);

	void removeByPost(int id_post);

}
