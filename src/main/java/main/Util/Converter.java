package main.Util;


import main.model.DTO.GlobalSettingDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Converter {

    public static Map<String, Boolean> convertSettingsListToMap(List<GlobalSettingDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(GlobalSettingDTO::getCode, GlobalSettingDTO::getBoolValue));
    }
}
