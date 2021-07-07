package main.Util;

import main.model.DTO.SettingsDTO;
import main.model.GlobalSetting;
import org.mapstruct.Mapper;

@Mapper
public interface SettingsToDTOCustomMapper {

    default SettingsDTO settingsToDTOCustomMapper(GlobalSetting setting){
        SettingsDTO settingsDTO = new SettingsDTO();
        //TODO map
        return settingsDTO;
    }
}
