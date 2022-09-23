package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "participation_requests")
@Entity
@Getter
@Setter
@ToString
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime created;

    @Column(name = "event_id", nullable = false)
    private Integer event;

    @Column(name = "requester_id", nullable = false)
    private Integer requester;

    private String status;

    public ParticipationRequest(Integer id, LocalDateTime created, Integer event, Integer requester, String status) {
        this.id = id;
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }

    public ParticipationRequest() {

    }
}
