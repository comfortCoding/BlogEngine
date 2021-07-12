package main.services;

import main.Util.SettingsToDTOMapper;
import main.Util.TagToDTOMapper;
import main.model.DTO.GlobalSettingDTO;
import main.model.DTO.InitDTO;
import main.model.DTO.TagDTO;
import main.model.GlobalSetting;
import main.model.Tag;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static main.config.Config.MAX_WEIGHT;
import static main.config.Config.MIN_WEIGHT;

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

    public List<TagDTO> getTags(String query) {

        LocalDateTime dateTime = LocalDateTime.now();

        List<Tag> tags = tagRepository.getTags();

        List<TagDTO> tagDTOs = new ArrayList<>();
        tags.forEach(tag -> tagDTOs.add(tagToDTOMapper.convertToDTO(tag)));

        double postCount = postRepository.getAllPosts(dateTime).size();

        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagRepository.countTagsByName(tagDTO.getName()) / postCount));

        //подсчитаем коэффициент по наибольшему значению
        tagDTOs.sort((tag1, tag2) -> tag2.getWeight().compareTo(tag1.getWeight()));
        double maxWeight = tagDTOs.get(0).getWeight();

        double k = 1.0 / maxWeight;

        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagDTO.getWeight() / k));

        //корректировка минимального/максимального веса
        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagDTO.getWeight() < MIN_WEIGHT ? MIN_WEIGHT : tagDTO.getWeight()));
        tagDTOs.forEach(tagDTO -> tagDTO.setWeight(tagDTO.getWeight() > MAX_WEIGHT ? MAX_WEIGHT : tagDTO.getWeight()));

        return tagDTOs;
    }

    public List<GlobalSettingDTO> getAllSettings() {

        List<GlobalSetting> globalSettings = settingsRepository.getAllSettings();

        List<GlobalSettingDTO> globalSettingDTOs = new ArrayList<>();

        globalSettings.forEach(setting -> globalSettingDTOs.add(settingsToDTOMapper.convertToDTO(setting)));

        return globalSettingDTOs;
    }


    public InitDTO getInit() {
        return new InitDTO();
    }

}
