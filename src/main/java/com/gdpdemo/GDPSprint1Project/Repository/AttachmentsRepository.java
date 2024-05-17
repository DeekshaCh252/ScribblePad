package com.gdpdemo.GDPSprint1Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gdpdemo.GDPSprint1Project.Attachments;
import com.gdpdemo.GDPSprint1Project.Posts;

@Repository
public interface AttachmentsRepository extends JpaRepository<Attachments, Integer> {
	@Query("select u from Posts u where u.id = :id")
	public Posts getByPostId(int id);
	
	void deleteAllByPostsId(int id);
}
