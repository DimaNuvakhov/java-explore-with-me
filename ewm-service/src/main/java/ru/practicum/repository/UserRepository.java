package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAll(Pageable pageable);


}
