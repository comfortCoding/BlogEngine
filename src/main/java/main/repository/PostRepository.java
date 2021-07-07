package main.repository;

import main.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= :nowDate ")
    List<Post> getAllPosts(LocalDateTime nowDate);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= :nowDate ")
    List<Post> getAllPosts(LocalDateTime nowDate, Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE LOWER(CONCAT(p.title, p.text)) LIKE %:searchText% " +
            "AND p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= :nowDate " )
    List<Post> searchPostsByText(String searchText, LocalDateTime nowDate);
}
