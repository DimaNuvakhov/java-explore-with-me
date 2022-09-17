package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {

    private double lat;

    private double lon;

    public LocationDto(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
