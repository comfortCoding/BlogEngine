package main.model;

import lombok.Getter;
import lombok.Setter;
import main.model.enums.ModerationStatus;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
@NamedStoredProcedureQuery(name = "getMyFirstPostEntity",
        procedureName = "p_user_get_first_post", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "user_id", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "now_server_date", type = LocalDateTime.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "time", type = LocalDateTime.class)}
)
@NamedStoredProcedureQuery(name = "getFirstPostEntity",
        procedureName = "p_get_first_post", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "now_server_date", type = LocalDateTime.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "time", type = LocalDateTime.class)}
)
public class Post {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", nullable = false, columnDefinition = "VARCHAR(50) default \"NEW\"")
    private ModerationStatus moderationStatus;

    @ManyToOne
    @JoinColumn(name = "moderator_id", foreignKey = @ForeignKey(name = "FK_moderator_id"))
    private User moderator;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_user_id"))
    private User user;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postCommentList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Where(clause = "is_like = true")
    private List<PostVote> postLikeList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Where(clause = "is_like = false")
    private List<PostVote> postDisLikeList;

    @ManyToMany
    @JoinTable(
            name = "post2tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> postTagList = new ArrayList<>();
}
