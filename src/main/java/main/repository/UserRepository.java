package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.email = :email ")
    User findUserByEmail(@Param(value = "email") String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.email = :email AND u.isModerator = 1 ")
    User findModeratorByEmail(@Param(value = "email") String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.code = :code ")
    User findUserByCode(@Param(value = "code") String code);

}
