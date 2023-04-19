package com.example.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.shop.models.Person;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByLogin(String login);

    @Query(value = "select * from person where login  = 'admin' and role = 'ROLE_ADMIN'", nativeQuery = true)
    Optional<Person> findDefaultAdmin();
}
