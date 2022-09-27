package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "comments")
@Entity
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    public Comment(Integer id, String text, Integer userId, Integer eventId, LocalDateTime createdOn) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.eventId = eventId;
        this.createdOn = createdOn;
    }

    public Comment() {

    }
}
