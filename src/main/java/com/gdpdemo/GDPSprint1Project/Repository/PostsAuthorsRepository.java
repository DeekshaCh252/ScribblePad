package com.gdpdemo.GDPSprint1Project.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gdpdemo.GDPSprint1Project.Posts;
import com.gdpdemo.GDPSprint1Project.PostsAuthors;

@Repository
public interface PostsAuthorsRepository extends JpaRepository<PostsAuthors, Integer> {
	@Modifying
	@Query("delete from PostsAuthors p where p.id_post.id=:id")
	void deletePostsAuthors(@Param("id") int id);
	
	@Query("select u from PostsAuthors u where u.id_author = :id_author")
	public Page<Posts> findPostsByUser(@Param("id_author") int id_author, Pageable pageable);
	
}
