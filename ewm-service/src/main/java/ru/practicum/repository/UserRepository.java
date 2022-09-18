package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE id IN (?1)", nativeQuery = true)
    List<User> findUsers(List<Integer> ids);
}
