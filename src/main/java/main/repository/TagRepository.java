package main.repository;

import main.model.Tag;
import main.model.answer.TagAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT " +
            "t " +
            "FROM Tag t ")
    List<Tag> getTags();

    @Query("SELECT " +
                "t.name " +
            "FROM Tag t " +
            "LEFT JOIN PostToTag ptt " +
                "ON t.id = ptt.id.tag.id " +
            "LEFT JOIN Post p " +
                "ON p.id = ptt.id.post.id " +
            "WHERE p.id = :postID " +
            "GROUP BY t.name ")
    List<String> getTagNamesByPostID(@Param("postID") Integer postID);

    @Query("SELECT " +
                "COUNT(ptt.id.post.id) " +
            "FROM Tag t " +
            "LEFT JOIN PostToTag ptt " +
                "ON t.id = ptt.id.tag.id " +
            "WHERE t.name = :tagName ")
    Integer countTagsByName(@Param("tagName") String tagName);

    @Query("SELECT " +
                "new main.model.answer.TagAnswer(t.name, t.weight) " +
            "FROM TagView t " +
            "WHERE LOWER(COALESCE(t.name,''))     LIKE %:query% ")
    List<TagAnswer> getTagView(@Param("query") String query);
}
