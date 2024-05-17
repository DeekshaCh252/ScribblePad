package com.gdpdemo.GDPSprint1Project.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gdpdemo.GDPSprint1Project.Home;
import com.gdpdemo.GDPSprint1Project.Posts;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {

	@Query("select u from Posts u where u.category = :category")
	public Page<Posts> findPostsByCategory(@Param("category") String category, Pageable pageable);
	
	@Query("select u from Posts u where u.category = :category AND u.status = :status")
	public Page<Posts> findPostsByCategoryStatus(@Param("category") String category,@Param("status") String status,  Pageable pageable);
	
	@Query("SELECT p FROM Posts p WHERE YEAR(p.date) = :year AND MONTH(p.date) = :month AND p.category = :category AND p.status = :status")
	public Page<Posts> findPostsByCategoryStatusAndFilter(@Param("category") String category,@Param("status") String status,@Param("year") int year,@Param("month") int month,  Pageable pageable);
	
	@Query("SELECT p FROM Posts p WHERE YEAR(p.date) = :year AND MONTH(p.date) = :month")
	public List<Posts> findPostsByYearAndMonth(@Param("year") int year,@Param("month") int month);
	
	@Query("select u from Posts u JOIN u.postsAuthors pa on u.id = pa.id_post where YEAR(u.date) = :year AND MONTH(u.date) = :month AND u.category = :category AND pa.id_author = :id_author")
	public Page<Posts> findPostsByCategoryStatusAndAuthor(@Param("category") String category,@Param("id_author") int id_author,@Param("year") int year,@Param("month") int month,Pageable pageable);
}