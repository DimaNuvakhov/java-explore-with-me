package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "users")
@Entity
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    public User(Integer id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User() {

    }
}
