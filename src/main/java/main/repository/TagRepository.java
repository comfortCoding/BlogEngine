package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT " +
            "t " +
            "FROM Tag t ")
    List<Tag> getTags();

    @Query("SELECT " +
            "COUNT(ptt.id.post.id) " +
            "FROM Tag t " +
            "LEFT JOIN PostToTag ptt " +
            "ON t.id = ptt.id.tag.id " +
            "WHERE t.name = :tagName ")
    Integer countTagsByName(String tagName);
}
