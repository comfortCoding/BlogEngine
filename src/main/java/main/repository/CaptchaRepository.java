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

    @Query("SELECT COUNT(cc.id) " +
            "FROM CaptchaCode cc " +
            "WHERE cc.secretCode = :secret ")
    Byte checkCaptcha(@Param(value = "secret") String secret);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM CaptchaCode cc " +
            "WHERE TIMESTAMPDIFF(HOUR, cc.time, :now ) > :expiresIn")
    void deleteExpiredCaptcha(@Param(value = "now") LocalDateTime now,
                              @Param(value = "expiresIn") Byte expirationLimit);
}
