package com.gdpdemo.GDPSprint1Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.PostsAuthors;

@Service
public interface AdminAuthorManager {

    void save(int id_post, int id_author);

    List<PostsAuthors> getAllPostsAuthors();

    List<Posts> getAllPostsByAuthors(int id_author);

}
