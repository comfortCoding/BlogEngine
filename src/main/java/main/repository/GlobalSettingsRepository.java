package main.repository;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting, Integer> {

    @Query("SELECT " +
            "CAST(CASE WHEN gs.value = 'YES' THEN 'True' ELSE 'False' END AS boolean) AS value " +
            "FROM GlobalSetting gs " +
            "WHERE gs.code LIKE :settingToFind ")
    boolean getSetting(String settingToFind);
}
