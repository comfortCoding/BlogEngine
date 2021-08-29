package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CalendarResponse {

    @JsonProperty(value = "years")
    private List<Byte> years;

    @JsonProperty(value = "posts")
    private Map<String, Long> posts;
}
