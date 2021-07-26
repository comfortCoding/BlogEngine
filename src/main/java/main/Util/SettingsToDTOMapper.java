package main.Util;

import main.model.dto.GlobalSettingDTO;
import main.model.GlobalSetting;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SettingsToDTOMapper {

    private final ModelMapper modelMapper;

    public SettingsToDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(GlobalSetting.class, GlobalSettingDTO.class);
    }

    public GlobalSettingDTO convertToDTO(GlobalSetting setting) {
        return modelMapper.map(setting, GlobalSettingDTO.class);
    }
}
