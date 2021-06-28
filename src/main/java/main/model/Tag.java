package main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="tags")
public class Tag {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;
}
