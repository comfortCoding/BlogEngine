package main.services;

import main.Util.SettingsToDTOMapper;
import main.Util.TagToDTOMapper;
import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagsResponse;
import main.model.DTO.GlobalSettingDTO;
import main.model.DTO.TagDTO;
import main.model.GlobalSetting;
import main.model.Tag;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeneralService {
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final GlobalSettingsRepository settingsRepository;

    private final TagToDTOMapper tagToDTOMapper;
    private final SettingsToDTOMapper settingsToDTOMapper;

    public GeneralService(TagRepository tagRepository,
                          PostRepository postRepository,
                          GlobalSettingsRepository settingsRepository,
                          TagToDTOMapper tagToDTOMapper,
                          SettingsToDTOMapper settingsToDTOMapper) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.settingsRepository = settingsRepository;
        this.tagToDTOMapper = tagToDTOMapper;
        this.settingsToDTOMapper = settingsToDTOMapper;
    }

    public ResponseEntity<TagsResponse> getTags(String query) {
        List<Tag> tags = tagRepository.getTags();

        List<TagDTO> tagDTOs = new ArrayList<>();
        tags.forEach(tag -> tagDTOs.add(tagToDTOMapper.convertToDTO(tag)));

        double postCount = postRepository.getAllPosts(LocalDateTime.now()).size();

        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagRepository.countTagsByName(tagDTO.getName()) / postCount));

        //подсчитаем коэффициент по наибольшему значению
        tagDTOs.sort((tag1, tag2) -> tag2.getWeight().compareTo(tag1.getWeight()));
        double k = 1.0 / tagDTOs.get(0).getWeight();

        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagDTO.getWeight() / k));

        TagsResponse response = new TagsResponse();
        response.setTags(tagDTOs);

        return ResponseEntity
                .ok(response);
    }

    public ResponseEntity<SettingsResponse> getAllSettings() {

        List<GlobalSetting> globalSettings = settingsRepository.getAllSettings();

        List<GlobalSettingDTO> globalSettingDTOs = new ArrayList<>();

        globalSettings.forEach(setting -> globalSettingDTOs.add(settingsToDTOMapper.convertToDTO(setting)));

        HashMap<String, Boolean> settings = new HashMap<>(convertListToMap(globalSettingDTOs));

        SettingsResponse settingsResponse = new SettingsResponse();
        settingsResponse.setMultiuserMode(settings.get("MULTIUSER_MODE"));
        settingsResponse.setPostPremoderation(settings.get("POST_PREMODERATION"));
        settingsResponse.setStatisticsIsPublic(settings.get("STATISTICS_IS_PUBLIC"));

        return ResponseEntity
                .ok(settingsResponse);
    }


    public ResponseEntity<InitResponse> getInit() {
        return ResponseEntity
                .ok(new InitResponse());
    }

    private Map<String, Boolean> convertListToMap(List<GlobalSettingDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(GlobalSettingDTO::getCode, GlobalSettingDTO::getBoolValue));
    }
}
