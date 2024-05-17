package com.gdpdemo.GDPSprint1Project;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Component
public class Posts {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
 // Validation for post content length
    @Length(min = 1, message = "Content of the Post should be start at least one character")
    @Column
    @Lob
    private String post_content;

    @Column
    private String content;
 // validation for category
    @Size(min = 1, message = "category required")
    @Column
    private String category;

    @Column
    private LocalDate date;
// validation for title
    @Size(min = 1, message = "title is required")
    @Column
    private String title;

    @Column
    private String status;
    
    private String image64;
    // Relationship annotations for postsAuthors, attachments, and comments
    @OneToMany(mappedBy = "id_post", cascade = { CascadeType.MERGE })
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<PostsAuthors> postsAuthors;

    @Lob
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] attachments;

    @OneToMany(mappedBy = "posts", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Comments> comments;
    // Constructor with parameters to initialize the object
    public Posts(int id,
                 @Length(min = 1, message = "Content of the Post should be start at least one character") String post_content,
                 @Size(min = 1, message = "category required") String category,
                 @Size(min = 1, message = "title is required") String title,
                 List<PostsAuthors> postsAuthors,
                 byte[] attachments, String content, String status) {
        super();
        this.id = id;
        this.post_content = post_content;
        this.category = category;
        this.title = title;
        this.date = LocalDate.now();
        this.postsAuthors = postsAuthors;
        this.attachments = attachments;
        this.content = content;
        this.status = status;
    }

	/*
	 * public String getContent() { return content; }*/

	public Posts() {
		super();
	}

//	public boolean isAccepted() {
//		return isAccepted;
//	}
//
//	public void setAccepted(boolean isAccepted) {
//		this.isAccepted = isAccepted;
//	}

	public int getId() {
		return id;
	}

	public String getPost_content() {
		return post_content;
	}

	public String getCategory() {
		return category;
	}

	public byte[] getAttachments() {
		return attachments;
	}

	public void setAttachments(byte[] attachments) {
		this.attachments = attachments;
	}

	public List<PostsAuthors> getPostsAuthors() {
		return postsAuthors;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPost_content(String post_content) {
		this.post_content = post_content;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPostsAuthors(List<PostsAuthors> postsAuthors) {
		this.postsAuthors = postsAuthors;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImage64() {
		return image64;
	}

	public void setImage64(String image64) {
		this.image64 = image64;
	}}





	

	

	

