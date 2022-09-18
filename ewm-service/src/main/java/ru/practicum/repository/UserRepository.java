package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findAll(Pageable pageable);

    @Query(value = "select * from users where id=?1", nativeQuery = true)
    public User findUser(Integer id);

}
