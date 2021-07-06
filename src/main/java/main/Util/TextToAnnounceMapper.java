package main.Util;

import org.springframework.stereotype.Component;

@Component
public class TextToAnnounceMapper {

    public String textToAnnounce(String text) {
        if (text.length() < 150) {
            return text + "...";
        } else {
            return text.substring(0, 150) + "...";
        }
    }
}
