package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "compilations")
@Entity
@Getter
@Setter
@ToString
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean pinned;

    private String title;

    public Compilation(Integer id, Boolean pinned, String title) {
        this.id = id;
        this.pinned = pinned;
        this.title = title;
    }

    public Compilation() {

    }
}
