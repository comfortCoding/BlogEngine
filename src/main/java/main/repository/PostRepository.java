package main.repository;

import main.model.Post;
import main.model.answer.CalendarAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    @Query("SELECT " +
            "COUNT(p.id) " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate "
    )
    Integer countAllPosts(@Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate "
    )
    Page<Post> getAllPostsTimeSort(@Param("nowDate") LocalDateTime nowDate,
                                   @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "LEFT JOIN PostComment pc " +
            "ON p.id = pc.post.id " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate " +
            "GROUP BY p.id " +
            "ORDER BY p.postCommentList.size DESC, p.time DESC ")
    Page<Post> getAllPostsCommentSort(@Param("nowDate") LocalDateTime nowDate,
                                      @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "LEFT JOIN PostVote pv " +
            "ON p.id = pv.post.id " +
            "AND pv.isLike = 1 " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv.id) DESC ")
    Page<Post> getAllPostsLikesSort(@Param("nowDate") LocalDateTime nowDate,
                                    @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE LOWER(CONCAT(p.title, p.text)) LIKE %:searchText% " +
            "AND p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate "
    )
    Page<Post> searchPostsByText(@Param("searchText") String searchText,
                                 @Param("nowDate") LocalDateTime nowDate,
                                 @Param("pageable") Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.id = :postID"
    )
    Post getPostByID(@Param("postID") Integer postID);

    @Query("SELECT " +
            "new main.model.answer.CalendarAnswer(p.time, COUNT(p.id)) " +
            "FROM Post p " +
            "WHERE YEAR(p.time) = :year " +
            "AND p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate " +
            "GROUP BY p.time "
    )
    List<CalendarAnswer> postsByDate(@Param("year") Integer year,
                                     @Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT " +
            "YEAR(p.time) AS year " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate " +
            "GROUP BY YEAR(p.time) " +
            "ORDER BY YEAR(p.time) "
    )
    List<Byte> getPostYears(@Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time BETWEEN :searchDateFrom AND :searchDateTo "
    )
    Page<Post> getAllPostsByDate(@Param("searchDateFrom") LocalDateTime searchDateFrom,
                                 @Param("searchDateTo") LocalDateTime searchDateTo,
                                 @Param("pageable") Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN PostToTag ptt " +
                "ON p.id = ptt.id.post.id " +
            "LEFT JOIN Tag t " +
                "ON t.id = ptt.id.tag.id " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= :nowDate " +
                "AND t.name = :searchTag " +
            "GROUP BY p.id "
    )
    Page<Post> getAllPostsByTag(@Param("searchTag") String searchTag,
                                @Param("nowDate") LocalDateTime nowDate,
                                @Param("pageable") Pageable pageable);

}
