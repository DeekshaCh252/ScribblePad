package com.gdpdemo.GDPSprint1Project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gdpdemo.GDPSprint1Project.Admin;
import com.gdpdemo.GDPSprint1Project.Posts;

public interface AdminRepository extends JpaRepository<Posts, Integer> {
	

	@Query("select u from Posts u where u.status = :status")
	public List<Posts> findPostsByStatus(@Param("status") String status);
	@Query("select u from Admin u where u.id = :id")
	public void deleteByAdminId(@Param("id") int id);
}
