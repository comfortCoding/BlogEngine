package main.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static main.config.Config.TIME_ZONE;

@Component
public class DateToSecondConverter {

    public Long dateToSecond(LocalDateTime date) {

        return date.atZone(ZoneId.of(TIME_ZONE)).toEpochSecond();
    }
}
