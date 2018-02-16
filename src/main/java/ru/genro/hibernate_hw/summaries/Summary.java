package ru.genro.hibernate_hw.summaries;

import ru.genro.hibernate_hw.users.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "summaries")
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "summary_id")
    private Integer id;

    @Column(name = "skills")
    private String skills;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "creation_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Summary(String skills, Integer experience)
    {
        this.skills = skills;
        this.creationDate = new Date();
        this.experience = experience;
    }

    Summary(){}

    public Integer getId() {
        return id;
    }

    public Integer getExperience() {
        return experience;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        Summary thatSummary = (Summary) that;
        return Objects.equals(id, thatSummary.id)
                && creationDate.equals(thatSummary.creationDate)
                && skills.equals(thatSummary.skills)
                && experience.equals(thatSummary.experience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate);
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, skills='%s', experience=%d years, creationDate='%s'}",
                getClass().getSimpleName(), id, skills, experience, creationDate);
    }
}
