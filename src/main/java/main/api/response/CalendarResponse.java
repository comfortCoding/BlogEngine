package main.api.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CalendarResponse {

    private List<Byte> years;

    private Map<String, Long> posts;
}
