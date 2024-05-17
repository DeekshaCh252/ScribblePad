package com.gdpdemo.GDPSprint1Project;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PostsAuthorsId implements Serializable {

    private static final long serialVersionUID = 1L;

    // Identifies the 'id_post' in the composite key
    @Column(name = "id_post")
    private int id_post;

    // Identifies the 'id_author' in the composite key
    @Column(name = "id_author")
    private int id_author;

    // Default constructor
    public PostsAuthorsId() {
        super();
    }

    // Constructor to set 'id_post' and 'id_author'
    public PostsAuthorsId(int id_post, int id_author) {
        super();
        this.id_post = id_post;
        this.id_author = id_author;
    }

    // Getter for 'id_post'
    public int getId_post() {
        return id_post;
    }

    // Getter for 'id_author'
    public int getId_author() {
        return id_author;
    }

    // Setter for 'id_post'
    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    // Setter for 'id_author'
    public void setId_author(int id_author) {
        this.id_author = id_author;
    }
}
