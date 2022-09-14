package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "categories")
@Entity
@Getter
@Setter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {

    }
}
