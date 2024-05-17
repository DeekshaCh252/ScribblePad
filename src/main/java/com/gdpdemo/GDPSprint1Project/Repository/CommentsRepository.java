package com.gdpdemo.GDPSprint1Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gdpdemo.GDPSprint1Project.Comments;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Integer> {
	void deleteAllByPostsId(int id);
}
