package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "events")
@Entity
@Getter
@Setter
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    private Boolean paid;

    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    private String state;

    private String title;

}
