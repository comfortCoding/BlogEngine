package main.repository;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSetting, Integer> {
/*
    @Query("INSERT INTO GlobalSetting " +
            "(code, " +
            "name, " +
            "value) " +
            "SELECT " +
            "\"MULTIUSER_MODE\", " +
            "\"Многопользовательский режим\", " +
            "\"NO\" " +
            "INSERT INTO blogdb.global_settings " +
            "(code, " +
            "name, " +
            "value) " +
            "VALUES " +
            "(\"POST_PREMODERATION\", " +
            "\"Премодерация постов\", " +
            "\"YES\"); " +
            "INSERT INTO blogdb.global_settings " +
            "(code, " +
            "name, " +
            "value) " +
            "VALUES " +
            "(\"STATISTICS_IS_PUBLIC\", " +
            "\"Показывать всем статистику блога\", " +
            "\"YES\");"
    )
    void init();
*/
    @Query("SELECT " +
            "gs.code, " +
            "CAST(CASE WHEN gs.value = 'YES' THEN 'True' ELSE 'False' END AS boolean) AS value " +
            "FROM GlobalSetting gs")
    List<GlobalSetting> getSettings();
}
