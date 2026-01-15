package se.jensen.saman.socialnetworkmaven.model;

import jakarta.persistence.*;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity

@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private String bio;

    @Column(unique = true)
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @OrderBy ("createdAt DESC")
    private List<Post> posts;


    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserFollow> followingList = new HashSet<>();

    @OneToMany(mappedBy = "followed")
    private Set<UserFollow> followerList = new HashSet<>();


    public User() {

    }


    public User(String username, String password, String role) {

        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void follow(User userThatIsBeingFollowed) {

        User userThatPromptedFollow = this;

        UserFollow followTicket = new UserFollow(userThatPromptedFollow, userThatIsBeingFollowed);

        userThatPromptedFollow.getFollowingList().add(followTicket);

        userThatIsBeingFollowed.getFollowerList().add(followTicket);

    }

    public void unfollow(User userThatIsBeingUnFollowed) {

        User userThatPromptedUnFollow = this;

        userThatPromptedUnFollow.getFollowingList()
                .removeIf(followTicket -> followTicket.getFollowed().equals(userThatIsBeingUnFollowed));

        userThatIsBeingUnFollowed.getFollowerList()
                .removeIf(followTicket -> followTicket.getFollowed().equals(userThatPromptedUnFollow));

    }


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }


    public Set<UserFollow> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(Set<UserFollow> followingList) {
        this.followingList = followingList;
    }

    public Set<UserFollow> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(Set<UserFollow> followerList) {
        this.followerList = followerList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

}
