package main.model.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_postsbydate")
public class PostsByDateView {

    @Id
    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "posts")
    private Integer posts;

}
