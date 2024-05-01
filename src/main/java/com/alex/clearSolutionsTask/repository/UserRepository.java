package com.alex.clearSolutionsTask.repository;

import com.alex.clearSolutionsTask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByDateOfBirthIsBetween(LocalDate from, LocalDate to);

    boolean existsByEmail(String email);
}
