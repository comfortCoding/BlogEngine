package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class CalendarResponse {

    @JsonProperty(value = "years")
    private List<Byte> years;

    @JsonProperty(value = "posts")
    private Map<String, Integer> posts;

    public static CalendarResponse create() {
        return new CalendarResponse();
    }
}
