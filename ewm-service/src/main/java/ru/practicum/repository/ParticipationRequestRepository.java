package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    Boolean existsByRequesterAndAndEvent(Integer requesterId, Integer eventId);

    List<ParticipationRequest> findAllByEvent(Integer eventId);

}
