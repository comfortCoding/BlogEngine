package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Integer> {

    @Query("SELECT pv " +
            "FROM PostVote pv " +
            "WHERE pv.user.id = :userID " +
            "AND pv.post.id = :postID ")
    PostVote getVote(@Param("userID") Integer userID,
                     @Param("postID") Integer postID);

}
