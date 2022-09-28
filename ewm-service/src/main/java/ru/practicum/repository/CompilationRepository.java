package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query(value = "SELECT * FROM compilations WHERE pinned = ?1", nativeQuery = true)
    Page<Compilation> findAllWhenPinnedIs(Boolean pinned, Pageable pageable);

}
