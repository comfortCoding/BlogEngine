package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT " +
            "u.id AS user," +
            "u.name AS name, " +
            "u.photo AS photo, " +
            "u.email AS email, " +
            "false AS moderation, " +
            "0 AS moderationCount," +
            "false AS settings " +
            "FROM User u ")
    User checkUser();

}
