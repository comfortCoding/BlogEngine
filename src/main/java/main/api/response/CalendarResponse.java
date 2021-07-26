package main.api.response;

import lombok.Data;
import main.model.dto.CalendarDTO;

import java.util.List;

@Data
public class CalendarResponse {

    List<Integer> years;

    CalendarDTO posts;
}
