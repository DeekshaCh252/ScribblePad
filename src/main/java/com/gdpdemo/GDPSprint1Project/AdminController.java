package com.gdpdemo.GDPSprint1Project;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdpdemo.GDPSprint1Project.Repository.AdminRepository;
import com.gdpdemo.GDPSprint1Project.Repository.PostsRepository;
import com.gdpdemo.GDPSprint1Project.service.AdminManager;
//import com.gdpdemo.GDPSprint1Project.service.AdminAuthorManager;
//import com.gdpdemo.GDPSprint1Project.service.AdminManager;
import com.gdpdemo.GDPSprint1Project.service.AdminService;
import com.gdpdemo.GDPSprint1Project.service.AttachmentsManager;
import com.gdpdemo.GDPSprint1Project.service.PostsAuthorsManager;
//import com.gdpdemo.GDPSprint1Project.service.PostsAuthorsManager;
import com.gdpdemo.GDPSprint1Project.service.PostsManager;
import com.gdpdemo.GDPSprint1Project.storage.StorageService;




@Controller
public class AdminController {
    
    // Dependency Injection
    private final AdminService admser;
    private final PostsManager postsManager;
    private final PostsRepository postrepo;
    private AttachmentsManager attachmentsManager;
    private AttachmentsManager commentsManager;
    private PostsAuthorsManager postsAuthorsManager;
    private AdminManager adminManager;
    private Posts posts;
    private AdminRepository admrepo;

    @Autowired
    public AdminController(
            Posts posts, 
            AdminService admser, 
            PostsManager postsManager, 
            PostsRepository postrepo, 
            AttachmentsManager attachmentsManager, 
            AttachmentsManager commentsManager, 
            PostsAuthorsManager postsAuthorsManager, 
            AdminManager adminManager, 
            AdminRepository admrepo) {
        
        // Initializing dependencies through constructor injection
        this.postsManager = postsManager;
        this.admser = admser;
        this.adminManager = adminManager;
        this.commentsManager = commentsManager;
        this.postsAuthorsManager = postsAuthorsManager;
        this.postrepo = postrepo;
        this.attachmentsManager = attachmentsManager;
        this.posts = posts; 
        this.admrepo = admrepo;
    }

    // Mapping to display all posts for admin
    @RequestMapping("/admin")
    public String listUsers(Model model) {
        // Retrieve and display all pending posts for admin
        List<Posts> post = admser.getAllUsers();
        model.addAttribute("admin", post);
        model.addAttribute("admin", adminManager.getAllPostsOrderedStatus(-1, null, null, "PENDING"));
        return "admin";
    }

    // Mapping to display declined posts for admin
    @RequestMapping("/adminDeclined")
    public String listDeclined(Model model) {
        // Retrieve and display all declined posts for admin
        List<Posts> post = admser.getAllUsers();
        model.addAttribute("adminDeclined", post);
        model.addAttribute("admin", adminManager.getAllPostsOrderedStatus(-1, null, null, "DECLINED"));
        return "adminDeclined";
    }

    // Mapping to view a specific post by admin
    @GetMapping("/adminView/{id}")
    public String admin(@PathVariable("id") int id, Model model, HttpSession session) {
        // Retrieve and display a specific post by ID for admin
        // Also encode attachments to base64 for display
        Posts post = postsManager.findById(id);
        if (post.getAttachments() != null && post.getAttachments().length > 0) {
            byte[] encodeBase64 = Base64.getEncoder().encode(post.getAttachments());
            String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
            post.setImage64(base64Encoded);
        }
        model.addAttribute("post", post);
        model.addAttribute("comments", new Comments());
        return "adminView"; 
    }

    // Mapping to approve a post by admin
    @GetMapping("/adminView/approve/{id}/{category}")
    public String approve_Post(@PathVariable("id") int id, @PathVariable("category") String category, Model model) {
        // Approve a post by changing its status to "APPROVED"
        admrepo.deleteByAdminId(id);
        Posts p = postrepo.getById(id);
        p.setStatus("APPROVED");
        model.addAttribute("title", "My Post");
        model.addAttribute("admin", adminManager.getAllPostsOrderedStatus(-1, null, null, "PENDING"));
        return "admin";
    }

    // Mapping to reject a post by admin
    @GetMapping("/adminView/delete/{id}/{category}")
    public String reject_Post(@PathVariable("id") int id, @PathVariable("category") String category, Model model) {
        // Reject a post by changing its status to "DECLINED"
        Posts p = postrepo.getById(id);
        ((Posts) p).setStatus("DECLINED");
        model.addAttribute("title", "My Post");
        model.addAttribute("admin", adminManager.getAllPostsOrderedStatus(-1, null, null, "PENDING"));
        return "admin";
    }
}
