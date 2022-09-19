package ru.practicum.exception;

public class CompilationEventsNotFoundException extends RuntimeException {
    public CompilationEventsNotFoundException(String message) {
        super(message);
    }
}
