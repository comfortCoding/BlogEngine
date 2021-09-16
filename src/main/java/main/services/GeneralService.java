package main.services;

import main.api.request.ProfileRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.config.Config;
import main.config.exception.UnAuthorizedException;
import main.model.User;
import main.model.dto.ErrorsDTO;
import main.model.enums.Settings;
import main.repository.UserRepository;
import main.util.*;
import main.config.exception.ValidationException;
import main.model.answer.CalendarAnswer;
import main.model.dto.GlobalSettingDTO;
import main.model.dto.TagDTO;
import main.model.GlobalSetting;
import main.model.answer.TagAnswer;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static main.config.Config.*;

@Service
public class GeneralService {
    private final TagRepository tagRepository;
    private final GlobalSettingsRepository settingsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final SettingsToDTOMapper settingsToDTOMapper;
    private final TagToDTOCustomMapper tagToDTOCustomMapper;

    public GeneralService(TagRepository tagRepository,
                          GlobalSettingsRepository settingsRepository,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                          SettingsToDTOMapper settingsToDTOMapper) {
        this.tagRepository = tagRepository;
        this.settingsRepository = settingsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.settingsToDTOMapper = settingsToDTOMapper;
        this.tagToDTOCustomMapper = Mappers.getMapper(TagToDTOCustomMapper.class);
    }

    public InitResponse getBlogConfig() {
        return new InitResponse();
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

        GlobalSettingsResponse globalSettingsResponse = new GlobalSettingsResponse();
        globalSettingsResponse.setMultiuserMode(settings.get(Settings.MULTIUSER_MODE.toString()));
        globalSettingsResponse.setPostPremoderation(settings.get(Settings.POST_PREMODERATION.toString()));
        globalSettingsResponse.setStatisticsIsPublic(settings.get(Settings.STATISTICS_IS_PUBLIC.toString()));

        return globalSettingsResponse;
    }

    public StatisticsResponse getAllStatistics() throws UnAuthorizedException {

        User currentUser = getCurrentUser();

        if (!currentUser.isModerator()) {
            throw new UnAuthorizedException(Config.EXCEPTION_NOT_MODERATOR);
        }

        StatisticsResponse response = new StatisticsResponse();

        response.setPostsCount(postRepository.countPosts(LocalDateTime.now()));
        response.setViewsCount(postRepository.countViews(LocalDateTime.now()));
        response.setLikesCount(postRepository.countLikes(LocalDateTime.now()));
        response.setDislikesCount(postRepository.countDislikes(LocalDateTime.now()));

        LocalDateTime firstPublicationDate = postRepository.getFirstPublicationDate(LocalDateTime.now());
        if (firstPublicationDate != null) {
            response.setFirstPublication(new DateToSecondConverter().dateToSecond(firstPublicationDate));
        }

        return response;
    }

    public StatisticsResponse getMyStatistics() {

        Integer currentUserID = getCurrentUser().getId();

        StatisticsResponse response = new StatisticsResponse();

        response.setPostsCount(postRepository.countMyPosts(currentUserID, LocalDateTime.now()));
        response.setViewsCount(postRepository.countMyViews(currentUserID, LocalDateTime.now()));
        response.setLikesCount(postRepository.countMyLikes(currentUserID, LocalDateTime.now()));
        response.setDislikesCount(postRepository.countMyDislikes(currentUserID, LocalDateTime.now()));

        LocalDateTime firstPublicationDate = postRepository.getMyFirstPublicationDate(currentUserID, LocalDateTime.now());
        if (firstPublicationDate != null) {
            response.setFirstPublication(new DateToSecondConverter().dateToSecond(firstPublicationDate));
        }

        return response;
    }

    public ResultResponse saveImage() {

        return new ResultResponse();
    }

    public ResultResponse editMyProfile(ProfileRequest request) {

        String name = request.getName();
        String email = request.getEmail();
        User userInRequest = userRepository.findUserByEmail(email);
        String password = request.getPassword();
        String photo = String.valueOf(request.getPhoto());
        boolean isRemovePhoto = request.getRemovePhoto() == 1;

        User currentUser = getCurrentUser();

        ErrorsDTO errorsDTO = new ErrorsDTO();

        if (email != null && userInRequest != null && !userInRequest.getEmail().equals(currentUser.getEmail())) {
            errorsDTO.setEmailError(EMAIL_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (password != null && password.length() < MIN_PASSWORD_SIZE) {

            errorsDTO.setPasswordError(PASSWORD_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (name != null && !name.matches(NAME_REGEX)) {
            errorsDTO.setNameError(NAME_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (name != null) {
            currentUser.setName(name);
        }

        if (email != null && userInRequest == null) {
            currentUser.setEmail(email);

            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (password != null) {
            currentUser.setPassword(passwordEncoder.encode(password));
        }

        if (!isRemovePhoto && photo != null) {
            currentUser.setPhoto(photo);
        } else if (isRemovePhoto) {
            currentUser.setPhoto("");
        }

        userRepository.save(currentUser);

        return new ResultResponse(true, null);
    }

    public void setSettings(SettingsRequest request) {
        settingsRepository.updateSetting(Settings.MULTIUSER_MODE.toString(), request.isMultiuserMode() ? YES : NO);
        settingsRepository.updateSetting(Settings.POST_PREMODERATION.toString(), request.isPostPremoderation() ? YES : NO);
        settingsRepository.updateSetting(Settings.STATISTICS_IS_PUBLIC.toString(), request.isStatisticsIsPublic() ? YES : NO);
    }

    private User getCurrentUser() {
        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findUserByEmail(currentUserEmail);
    }
}
