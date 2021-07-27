package main.Util;


import main.model.answer.CalendarAnswer;
import main.model.dto.GlobalSettingDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter {

    public static Map<String, Boolean> convertSettingsListToMap(List<GlobalSettingDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(GlobalSettingDTO::getCode, GlobalSettingDTO::getBoolValue));
    }

    public static Map<String, Long> convertCalendarListToMap(List<CalendarAnswer> list) {
        return list.stream()
                .collect(Collectors.toMap(CalendarAnswer::getDateTime, CalendarAnswer::getPosts));
    }
}
