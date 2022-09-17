package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Table(name = "locations")
@Entity
@Getter
@Setter
@ToString
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double lat;

    private double lon;

    public Location(Integer id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public Location() {

    }
}
