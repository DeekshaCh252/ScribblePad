
package com.gdpdemo.GDPSprint1Project;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.gdpdemo.GDPSprint1Project.Repository.HomeRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsAuthorsRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsRepository;
import com.gdpdemo.GDPSprint1Project.service.AttachmentsManager;
import com.gdpdemo.GDPSprint1Project.service.PostsAuthorsManager;
import com.gdpdemo.GDPSprint1Project.service.PostsManager;
import com.gdpdemo.GDPSprint1Project.storage.StorageService;
import com.gdpdemo.GDPSprint1Project.service.CommentsManager;
@Controller
public class PostController {
	private final CommentsManager commentsManager;
	private final PostsManager postsManager;
	private final PostsAuthorsManager postsAuthorsManager;
	private final AttachmentsManager attachmentsManager;
	private final StorageService storageService;
	@Autowired
	private HomeRepository homeRepository;
	@Autowired
	private PostsAuthorsRepository postsAuthorsRepository;
	@Autowired
	private PostsRepository postsRepository;
	@Autowired
	public PostController(PostsManager postsManager, PostsAuthorsManager postsAuthorsManager,
			CommentsManager commentsManager, AttachmentsManager attachmentsManager, StorageService storageService ) {
		this.postsManager = postsManager;
		this.postsAuthorsManager = postsAuthorsManager;
		this.commentsManager = commentsManager;
		this.attachmentsManager = attachmentsManager;
		this.storageService = storageService;
	}
	 // Get mapping to display a post by its ID
	@GetMapping("/post/{id}")
	public String post(@PathVariable("id") int id, Model model, HttpSession session) {
		String email = (String) session.getAttribute("email");
		System.out.println(email);
		boolean disableForm = true;
		if(email.contains("nwmissouri.edu")) {
			disableForm = false;
		}
		Posts post = postsManager.findById(id);
		if (post.getAttachments() != null && post.getAttachments().length > 0) {
	        byte[] encodeBase64 = Base64.getEncoder().encode(post.getAttachments());
	        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
	        post.setImage64(base64Encoded);
	    }
		System.out.println(post.getPost_content());
		model.addAttribute("disableForm", disableForm);
		model.addAttribute("post", post);
		model.addAttribute("comments", new Comments());
		return "post";
	}
	/*
	 * @GetMapping("/adminView/{id}") public String postview(@PathVariable("id") int
	 * id, Model model, HttpSession session) { Posts post =
	 * postsManager.findById(id); System.out.println(post.getPost_content());
	 * model.addAttribute("post", post); model.addAttribute("comments", new
	 * Comments()); return "post"; }
	 */
	// edit post
	 // Get mapping to edit a post by its ID
	@GetMapping("/post/edit/{id}")
	public String editPost(Model model, @PathVariable int id) {
		Posts post = postsManager.findById(id);
		if (post.getAttachments() != null && post.getAttachments().length > 0) {
	        byte[] encodeBase64 = Base64.getEncoder().encode(post.getAttachments());
	        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
	        post.setImage64(base64Encoded);
	    }
		post.setDate(LocalDate.now());
		post.setStatus("PENDING");
		model.addAttribute("post", post);
		model.addAttribute("attachments", post.getAttachments());
		model.addAttribute("PostId", id);
		return "edit-post";
	}
	 // Post mapping to update a post by its ID
	@PostMapping("/post/update/{id}")
	public String updatePost(@PathVariable int id, @RequestBody @Valid @ModelAttribute("posts") Posts postss,
			@RequestParam("file") MultipartFile file,BindingResult br, BindingResult bindingResultForId,
			HttpSession session) throws IOException {
		if (br.hasErrors() || bindingResultForId.hasErrors()) {
			return "edit-post";
		}
		postss.setDate(LocalDate.now());
		postss.setAttachments(file.getBytes());
		postss.setContent(splitString(postss.getPost_content()));
		postss.setStatus("PENDING");
		postsManager.save(postss);
		// postsAuthorsManager.save(postss.getId(), home.getId());
		storageService.store(file);
		attachmentsManager.save(file, postss.getId());
		return "redirect:/post/" + id;
	}
	 // Method to split a string for post content display
	public String splitString(String post_content) {
		String s = "";
		if (post_content.length() > 150) {
			s = post_content.substring(0, 150) + "....";
		}
		return s;
	}
	 // Get mapping to delete a post by its ID and category
	@GetMapping("/post/delete/{id}/{category}")
	public String deletePost(@PathVariable("id") int id, @PathVariable("category") String category, Model model) {
		Posts post = postsManager.findById(id);
		
		attachmentsManager.removeByPostId(id);
		commentsManager.removeByPostId(id);
		postsAuthorsManager.removeByPost(id);
		postsManager.remove(id);
		
		model.addAttribute("title", "My Post");
		model.addAttribute("posts", postsManager.getAllPostsOrdered(-1, null, null, category));
		return "myposts";
	}
	 // Get mapping to retrieve all posts with pagination
	@GetMapping("/posts1")
	public ResponseEntity<List<Posts>> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = "10", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "1", required = false) Integer pageSize
			){
		List<Posts> allPost = this.postsManager.getAllPosts(pageNumber, pageSize);
		return new ResponseEntity<List<Posts>>(allPost,HttpStatus.OK);
	}
	 // Get mapping to display posts with pagination and filter by year and month
	@GetMapping("/posts/{page}")
	public String showPosts(HttpSession session,Model model,@RequestParam(name= "year", required = false, defaultValue = "2023") int year,
	        @RequestParam(name="month", required = false, defaultValue = "1") int month,@PathVariable("page") Integer page) {
		Pageable pageable = PageRequest.of(page, 5);
		  Integer id = (int) session.getAttribute("id"); 
		  System.out.println(id);
		 
		Page<Posts> posts = this.postsAuthorsManager.getAllPostsByAuthors(id,"library",year,month,pageable);
		model.addAttribute("title", "My Post");
		model.addAttribute("posts", posts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", posts.getTotalPages());
		return "posts";
	}
}

