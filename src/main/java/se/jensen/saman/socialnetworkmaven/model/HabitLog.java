package se.jensen.saman.socialnetworkmaven.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;


    @Entity

    @Table(name = "habitlog")
    public class HabitLog {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "log_date_time", nullable = false)
        private LocalDateTime logDateTime;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "habit_id", nullable = false)
        private Habit habit;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
       this.id = id;
    }

    public LocalDateTime getLogDateTime() {
        return logDateTime;
    }

    public void setLogDateTime(LocalDateTime logDateTime) {
        this.logDateTime = logDateTime;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }
}
