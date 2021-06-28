package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {
    @Query("SELECT " +
            "cc.secretCode AS secret, " +
            "cc.code AS image " +
            "FROM CaptchaCode cc")
    CaptchaCode returnCaptcha();

}
