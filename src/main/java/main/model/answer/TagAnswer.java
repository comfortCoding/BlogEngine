package main.model.answer;

import lombok.Data;

@Data
public class TagAnswer {

    private String name;

    private Double weight;

    public TagAnswer(String name, Double weight) {
        this.name = name;
        this.weight = weight;
    }
}
