package main.services;

import main.api.request.SettingsRequest;
import main.api.response.*;
import main.model.User;
import main.model.enums.Settings;
import main.repository.UserRepository;
import main.util.*;
import main.config.exception.ValidationException;
import main.model.answer.CalendarAnswer;
import main.model.dto.GlobalSettingDTO;
import main.model.dto.InitDTO;
import main.model.dto.TagDTO;
import main.model.GlobalSetting;
import main.model.answer.TagAnswer;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static main.config.Config.NO;
import static main.config.Config.YES;

@Service
public class GeneralService {
    private final TagRepository tagRepository;
    private final GlobalSettingsRepository settingsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final SettingsToDTOMapper settingsToDTOMapper;
    private final TagToDTOCustomMapper tagToDTOCustomMapper;

    public GeneralService(TagRepository tagRepository,
                          GlobalSettingsRepository settingsRepository,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          SettingsToDTOMapper settingsToDTOMapper) {
        this.tagRepository = tagRepository;
        this.settingsRepository = settingsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.settingsToDTOMapper = settingsToDTOMapper;
        this.tagToDTOCustomMapper = Mappers.getMapper(TagToDTOCustomMapper.class);
    }

    public InitResponse getBlogConfig() {
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

        TagsResponse response = new TagsResponse();
        response.setTags(tagDTOs);

        return response;
    }

    public CalendarResponse getCalendar(Integer yearParam) throws ValidationException {

        yearParam = (yearParam == null ? LocalDateTime.now().getYear() : yearParam);

        List<CalendarAnswer> calendar = postRepository.postsByDate(yearParam, LocalDateTime.now());

        List<Byte> postYears = postRepository.getPostYears(LocalDateTime.now());

        //сформируем ответ для фронта
        CalendarResponse response = new CalendarResponse();
        response.setYears(postYears);
        response.setPosts(Converter.convertCalendarListToMap(calendar));

        return response;
    }

    public GlobalSettingsResponse getSettings() {

        List<GlobalSetting> globalSettings = settingsRepository.getAllSettings();

        List<GlobalSettingDTO> globalSettingDTOs = new ArrayList<>();

        globalSettings.forEach(setting -> globalSettingDTOs.add(settingsToDTOMapper.convertToDTO(setting)));

        HashMap<String, Boolean> settings = new HashMap<>(Converter.convertSettingsListToMap(globalSettingDTOs));

        //сформируем ответ для фронта
        GlobalSettingsResponse globalSettingsResponse = new GlobalSettingsResponse();
        globalSettingsResponse.setMultiuserMode(settings.get(Settings.MULTIUSER_MODE.toString()));
        globalSettingsResponse.setPostPremoderation(settings.get(Settings.POST_PREMODERATION.toString()));
        globalSettingsResponse.setStatisticsIsPublic(settings.get(Settings.STATISTICS_IS_PUBLIC.toString()));

        return globalSettingsResponse;
    }

    public StatisticsResponse getAllStatistics() {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        StatisticsResponse response = new StatisticsResponse();

        response.setPostsCount(postRepository.countPosts(LocalDateTime.now()));
        response.setViewsCount(postRepository.countViews(LocalDateTime.now()));
        response.setLikesCount(postRepository.countLikes(LocalDateTime.now()));
        response.setDislikesCount(postRepository.countDislikes(LocalDateTime.now()));

        LocalDateTime localDateTime = postRepository.getFirstPublicationDate(LocalDateTime.now());
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());

        response.setFirstPublication(zdt.toInstant().toEpochMilli() / 1000);

        return response;
    }

    public StatisticsResponse getMyStatistics() {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Integer currentUserID = userRepository.findUserByEmail(currentUserEmail).getId();

        StatisticsResponse response = new StatisticsResponse();

        response.setPostsCount(postRepository.countMyPosts(currentUserID, LocalDateTime.now()));
        response.setViewsCount(postRepository.countMyViews(currentUserID, LocalDateTime.now()));
        response.setLikesCount(postRepository.countMyLikes(currentUserID, LocalDateTime.now()));
        response.setDislikesCount(postRepository.countMyDislikes(currentUserID, LocalDateTime.now()));

        LocalDateTime localDateTime = postRepository.getMyFirstPublicationDate(currentUserID, LocalDateTime.now());
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());

        response.setFirstPublication(zdt.toInstant().toEpochMilli() / 1000);

        return response;
    }

    public void setSettings(SettingsRequest request) {
        settingsRepository.updateSetting(Settings.MULTIUSER_MODE.toString(), request.isMultiuserMode() ? YES : NO );
        settingsRepository.updateSetting(Settings.POST_PREMODERATION.toString(), request.isPostPremoderation() ? YES : NO);
        settingsRepository.updateSetting(Settings.STATISTICS_IS_PUBLIC.toString(), request.isStatisticsIsPublic() ? YES : NO);
    }
}
