package main.model.answer;

import lombok.Data;

@Data
public class CalendarAnswer {

    private String dateTime;

    private Integer posts;

    public CalendarAnswer(String dateTime, Integer posts) {

        this.dateTime = dateTime;

        this.posts = posts;
    }
}
