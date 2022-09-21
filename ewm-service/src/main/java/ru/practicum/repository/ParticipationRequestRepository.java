package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    Boolean existsByRequesterAndAndEvent(Integer requesterId, Integer eventId);

    List<ParticipationRequest> findAllByEvent(Integer eventId);

    List<ParticipationRequest> findAllByEventAndStatusIs(Integer eventId, String status);

    List<ParticipationRequest> findAllByRequester(Integer requesterId);
}
