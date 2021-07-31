package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

    @Query("SELECT COUNT(cc.id) " +
            "FROM CaptchaCode cc " +
            "WHERE cc.secretCode = :secret ")
    Byte checkCaptcha(@Param(value = "secret") String secret);
}
