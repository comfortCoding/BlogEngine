package main.util;


import main.model.GlobalSetting;
import main.model.answer.CalendarAnswer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter {

    public static Map<String, Boolean> convertSettingsListToMap(List<GlobalSetting> list) {
        return list.stream()
                .collect(Collectors.toMap(GlobalSetting::getCode, GlobalSetting::getValue));
    }

    public static Map<String, Integer> convertCalendarListToMap(List<CalendarAnswer> list) {
        return list.stream()
                .collect(Collectors.toMap(CalendarAnswer::getDateTime, CalendarAnswer::getPosts));
    }
}
