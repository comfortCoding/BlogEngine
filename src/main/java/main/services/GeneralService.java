package main.services;

import main.Util.*;
import main.api.response.CalendarResponse;
import main.api.response.GlobalSettingsResponse;
import main.api.response.InitResponse;
import main.api.response.TagsResponse;
import main.config.exception.ValidationException;
import main.model.dto.GlobalSettingDTO;
import main.model.dto.InitDTO;
import main.model.dto.TagDTO;
import main.model.GlobalSetting;
import main.model.answer.TagAnswer;
import main.repository.GlobalSettingsRepository;
import main.repository.TagRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GeneralService {
    private final TagRepository tagRepository;
    private final GlobalSettingsRepository settingsRepository;

    private final SettingsToDTOMapper settingsToDTOMapper;
    private final TagToDTOCustomMapper tagToDTOCustomMapper;

    public GeneralService(TagRepository tagRepository,
                          GlobalSettingsRepository settingsRepository,
                          SettingsToDTOMapper settingsToDTOMapper) {
        this.tagRepository = tagRepository;
        this.settingsRepository = settingsRepository;
        this.settingsToDTOMapper = settingsToDTOMapper;
        this.tagToDTOCustomMapper = Mappers.getMapper(TagToDTOCustomMapper.class);
    }

    public InitResponse getBlogConfig(){
        InitDTO dto = new InitDTO();

        //сформируем ответ для фронта
        InitResponse response = new InitResponse();

        response.setTitle(dto.getTitle());
        response.setSubtitle(dto.getSubtitle());
        response.setCopyright(dto.getCopyright());
        response.setCopyrightFrom(dto.getCopyrightFrom());
        response.setEmail(dto.getEmail());
        response.setPhone(dto.getPhone());

        return response;
    }

    public TagsResponse getTags(String query) {

        List<TagAnswer> tags = tagRepository.getTagView(query == null ? "" : query);

        List<TagDTO> tagDTOs = new ArrayList<>();
        tags.forEach(tag -> tagDTOs.add(tagToDTOCustomMapper.tagToDTOCustomMapper(tag)));

        //сформируем ответ для фронта
        TagsResponse response = new TagsResponse();
        response.setTags(tagDTOs);

        return response;
    }

    public CalendarResponse getCalendar(String yearParam) throws ValidationException {

        //сформируем ответ для фронта
        return null;
    }

    public GlobalSettingsResponse getSettings() {

        List<GlobalSetting> globalSettings = settingsRepository.getAllSettings();

        List<GlobalSettingDTO> globalSettingDTOs = new ArrayList<>();

        globalSettings.forEach(setting -> globalSettingDTOs.add(settingsToDTOMapper.convertToDTO(setting)));

        HashMap<String, Boolean> settings = new HashMap<>(Converter.convertSettingsListToMap(globalSettingDTOs));

        //сформируем ответ для фронта
        GlobalSettingsResponse globalSettingsResponse = new GlobalSettingsResponse();
        globalSettingsResponse.setMultiuserMode(settings.get("MULTIUSER_MODE"));
        globalSettingsResponse.setPostPremoderation(settings.get("POST_PREMODERATION"));
        globalSettingsResponse.setStatisticsIsPublic(settings.get("STATISTICS_IS_PUBLIC"));

        return globalSettingsResponse;
    }
}
