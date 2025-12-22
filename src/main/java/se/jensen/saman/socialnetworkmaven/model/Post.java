package se.jensen.saman.socialnetworkmaven.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 4000)
    private String text;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Post() {

    }

    public Post(String text, LocalDateTime createdAt, User user) {
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }


    public Post(String text, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
