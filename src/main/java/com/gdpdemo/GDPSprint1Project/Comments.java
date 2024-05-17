package com.gdpdemo.GDPSprint1Project;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Unique identifier for the comment

    private String username; // User name of the commenter

    private int id_post; // ID of the associated post

    @NotNull(message = "Content of the comment is required")
    @Size(min = 1, message = "Content of the comment should start with at least one character")
    @Column
    private String comment_content; // Content of the comment

    @ManyToOne
    private Posts posts; // Associated post entity

    // Constructors

    public Comments() {
        super();
    }

    public Comments(int id, String username, int id_post, String comment_content, Posts posts) {
        super();
        this.id = id;
        this.username = username;
        this.id_post = id_post;
        this.comment_content = comment_content;
        this.posts = posts;
    }

    // Getters and setters for the attributes

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getId_post() {
        return id_post;
    }

    public String getComment_content() {
        return comment_content;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }
}
