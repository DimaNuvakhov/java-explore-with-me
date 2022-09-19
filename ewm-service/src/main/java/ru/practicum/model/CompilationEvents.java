package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "compilations_events")
@Entity
@Getter
@Setter
@ToString
public class CompilationEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "compilation_id", nullable = false)
    private Integer compilationId;

    @Column(name = "event_id", nullable = false)
    private Integer eventId;

    public CompilationEvents(Integer id, Integer compilationId, Integer eventId) {
        this.id = id;
        this.compilationId = compilationId;
        this.eventId = eventId;
    }

    public CompilationEvents() {

    }
}
