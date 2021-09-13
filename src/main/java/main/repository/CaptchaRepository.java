package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

    @Query("SELECT cc.secretCode " +
            "FROM CaptchaCode cc " +
            "WHERE cc.secretCode = :secret ")
    String checkSecretCode(@Param(value = "secret") String secret);

    @Query("SELECT cc.code " +
            "FROM CaptchaCode cc " +
            "WHERE cc.code = :captcha ")
    String checkCode(@Param(value = "captcha") String captcha);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM CaptchaCode cc " +
            "WHERE TIMESTAMPDIFF(HOUR, cc.time, :now ) > :expiresIn")
    void deleteExpiredCaptcha(@Param(value = "now") LocalDateTime now,
                              @Param(value = "expiresIn") Byte expirationLimit);
}
