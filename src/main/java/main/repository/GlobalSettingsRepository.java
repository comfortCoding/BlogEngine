package main.repository;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting, Integer> {

    @Query("SELECT " +
            "gs " +
            "FROM GlobalSetting gs ")
    List<GlobalSetting> getAllSettings();

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE GlobalSetting " +
            "SET value = :newValue " +
            "WHERE code = :settingCode ")
    void updateSetting(@Param("settingCode") String settingCode, @Param("newValue") String newValue);

}
