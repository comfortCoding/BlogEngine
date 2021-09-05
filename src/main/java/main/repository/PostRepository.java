package main.repository;

import main.model.Post;
import main.model.answer.CalendarAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

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
            "WHERE p.isActive = 0 AND p.user.id = :userID "
    )
    Page<Post> getInactivePosts(@Param("userID") Integer userID,
                                @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.user.id = :userID "
    )
    Page<Post> getPendingPosts(@Param("userID") Integer userID,
                               @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.moderator.id IS NULL "
    )
    Page<Post> getNewPosts(@Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.moderator.id = :userID "
    )
    Page<Post> getAcceptedPostsForModer(@Param("userID") Integer userID,
                                        @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'DECLINED' AND p.moderator.id = :userID "
    )
    Page<Post> getDeclinedPostsForModer(@Param("userID") Integer userID,
                                        @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'DECLINED' AND p.user.id = :userID "
    )
    Page<Post> getDeclinedPosts(@Param("userID") Integer userID,
                                @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.user.id = :userID "
    )
    Page<Post> getPublishedPosts(@Param("userID") Integer userID,
                                 @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.time <= :nowDate "
    )
    Page<Post> getPostsForModeration(@Param("nowDate") LocalDateTime nowDate,
                                     @Param("pageable") Pageable pageable);

    @Query("SELECT " +
            "COUNT(p) " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'NEW' AND p.moderator IS NULL AND p.time <= :nowDate "
    )
    Integer countPostsForModeration(@Param("nowDate") LocalDateTime nowDate);

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
            "new main.model.answer.CalendarAnswer(CAST(p.dateTime AS string), p.posts) " +
            "FROM PostsByDateView p " +
            "WHERE YEAR(p.dateTime) = :year " +
            "AND p.dateTime <= :nowDate " +
            "GROUP BY p.dateTime "
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

    @Query("SELECT COUNT(p) " +
            "FROM Post p " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' "
    )
    Integer countPosts(@Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT SUM(p.viewCount) " +
            "FROM Post p " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' "
    )
    Integer countViews(@Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT COUNT(pv.id) " +
            "FROM Post p " +
            "INNER JOIN PostVote pv " +
            "ON p.id = pv.post.id " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND pv.isLike = 1 "
    )
    Integer countLikes(@Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT COUNT(pv.id) " +
            "FROM Post p " +
            "INNER JOIN PostVote pv " +
            "ON p.id = pv.post.id " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND pv.isLike = 0 "
    )
    Integer countDislikes(@Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT COUNT(p) " +
            "FROM Post p " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.user.id = :userID "
    )
    Integer countMyPosts(@Param("userID") Integer userID,
                         @Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT SUM(p.viewCount) " +
            "FROM Post p " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.user.id = :userID "
    )
    Integer countMyViews(@Param("userID") Integer userID,
                         @Param("nowDate") LocalDateTime nowDate);


    @Query("SELECT " +
            "COUNT(pv.id) " +
            "FROM Post p " +
            "INNER JOIN PostVote pv " +
            "ON p.id = pv.post.id " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND pv.isLike = 1 " +
            "AND pv.user.id = :userID "
    )
    Integer countMyLikes(@Param("userID") Integer userID,
                         @Param("nowDate") LocalDateTime nowDate);

    @Query("SELECT " +
            "COUNT(pv.id) " +
            "FROM Post p " +
            "INNER JOIN PostVote pv " +
            "ON p.id = pv.post.id " +
            "WHERE p.isActive = 1 " +
            "AND p.time <= :nowDate " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND pv.isLike = 0 " +
            "AND pv.user.id = :userID "
    )
    Integer countMyDislikes(@Param("userID") Integer userID,
                            @Param("nowDate") LocalDateTime nowDate);


    @Procedure(name = "getFirstPostEntity")
    LocalDateTime getFirstPublicationDate(@Param("now_server_date") LocalDateTime nowDate);

    @Procedure(name = "getMyFirstPostEntity")
    LocalDateTime getMyFirstPublicationDate(@Param("user_id") Integer userID,
                                            @Param("now_server_date") LocalDateTime nowDate);

}
