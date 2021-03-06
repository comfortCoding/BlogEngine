package main.model;

import lombok.Getter;
import lombok.Setter;
import main.model.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_moderator", nullable = false)
    private boolean isModerator;

    @Column(name = "reg_time", nullable = false)
    private LocalDateTime regTime;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "code")
    private String code;

    @Column(name = "photo", columnDefinition="TEXT")
    private String photo;

    public Role getRole() {
        return this.isModerator ? Role.MODERATOR : Role.USER;
    }
}
