package main.util;

import org.springframework.stereotype.Component;

import static main.config.Config.POST_ANNOTATION_SIZE;

@Component
public class TextToAnnounceMapper {

    public String textToAnnounce(String text) {
        if (text.length() < POST_ANNOTATION_SIZE) {
            return text + "...";
        } else {
            return text.substring(0, POST_ANNOTATION_SIZE) + "...";
        }
    }
}
