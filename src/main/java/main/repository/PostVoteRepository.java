package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

    @Query("SELECT " +
            "COUNT(pv.id) AS likes " +
            "FROM PostVote pv " +
            "WHERE pv.isLike = 1 " +
            "AND pv.post.id = :postID ")
    Integer countLikesByPost(Integer postID);

    @Query("SELECT " +
            "COUNT(pv.id) AS likes " +
            "FROM PostVote pv " +
            "WHERE pv.isLike = 0 " +
            "AND pv.post.id = :postID ")
    Integer countDisLikesByPost(Integer postID);
}
