package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EndpointHit {

    Integer id;

    String app;

    String uri;

    String ip;

    LocalDateTime timestamp;

    public EndpointHit(Integer id, String app, String uri, String ip, LocalDateTime timestamp) {
        this.id = id;
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timestamp = timestamp;
    }
}
