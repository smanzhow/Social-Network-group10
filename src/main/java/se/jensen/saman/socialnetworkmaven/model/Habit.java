package se.jensen.saman.socialnetworkmaven.model;


import jakarta.persistence.Entity;
import jakarta.persistence.*;



import java.util.List;

@Entity
@Table(name = "habit")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "habit_name", nullable = false)
    private String habitName;

    @Column(length = 800)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HabitLog> habitLogList;



    public Habit (){

    }

    public Habit(String habitName, String description) {
        this.habitName = habitName;
        this.description = description;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<HabitLog> getHabitLogList() {
        return habitLogList;
    }

    public void setHabitLogList(List<HabitLog> habitLogList) {
        this.habitLogList = habitLogList;
    }
}
