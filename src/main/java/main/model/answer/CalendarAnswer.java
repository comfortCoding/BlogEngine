package main.model.answer;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CalendarAnswer {

    private String dateTime;

    private Long posts;

    public CalendarAnswer(LocalDateTime dateTime, Long posts) {

        this.dateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.posts = posts;
    }
}
