package main.repository;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting, Integer> {

    @Query("SELECT " +
            "gs " +
            "FROM GlobalSetting gs ")
    List<GlobalSetting> getAllSettings();
}
