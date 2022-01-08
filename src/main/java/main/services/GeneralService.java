package main.services;

import main.api.request.ProfileRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.config.Config;
import main.config.exception.UnAuthorizedException;
import main.model.User;
import main.api.dto.ErrorsDTO;
import main.model.enums.Settings;
import main.repository.UserRepository;
import main.util.*;
import main.config.exception.ValidationException;
import main.model.answer.CalendarAnswer;
import main.api.dto.TagDTO;
import main.model.GlobalSetting;
import main.model.answer.TagAnswer;
import main.repository.GlobalSettingsRepository;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static main.config.Config.*;

import org.springframework.beans.factory.annotation.Value;

@Service
public class GeneralService {

    @Value("${javapro.storagepath}")
    private String uploadPath;

    private final TagRepository tagRepository;
    private final GlobalSettingsRepository settingsRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TagToDTOCustomMapper tagToDTOCustomMapper;

    public GeneralService(TagRepository tagRepository,
                          GlobalSettingsRepository settingsRepository,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.tagRepository = tagRepository;
        this.settingsRepository = settingsRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tagToDTOCustomMapper = Mappers.getMapper(TagToDTOCustomMapper.class);
    }

    public InitResponse getBlogConfig() {
        return InitResponse.create();
    }

    public TagsResponse getTags(String query) {

        List<TagAnswer> tags = tagRepository.getTagView(query == null ? "" : query);

        List<TagDTO> tagDTOs = new ArrayList<>();
        tags.forEach(tag -> tagDTOs.add(tagToDTOCustomMapper.tagToDTOCustomMapper(tag)));

        return TagsResponse.create()
                .setTags(tagDTOs);
    }

    public CalendarResponse getCalendar(Integer yearParam) throws ValidationException {

        yearParam = (yearParam == null ? LocalDateTime.now().getYear() : yearParam);

        List<CalendarAnswer> calendar = postRepository.postsByDate(yearParam, LocalDateTime.now());

        List<Byte> postYears = postRepository.getPostYears(LocalDateTime.now());

        return CalendarResponse.create()
                .setYears(postYears)
                .setPosts(Converter.convertCalendarListToMap(calendar));
    }

    public GlobalSettingsResponse getSettings() {

        List<GlobalSetting> globalSettings = settingsRepository.getAllSettings();

        HashMap<String, Boolean> settings = new HashMap<>(Converter.convertSettingsListToMap(globalSettings));

        return GlobalSettingsResponse.create()
                .setMultiuserMode(settings.get(Settings.MULTIUSER_MODE.toString()))
                .setPostPremoderation(settings.get(Settings.POST_PREMODERATION.toString()))
                .setStatisticsIsPublic(settings.get(Settings.STATISTICS_IS_PUBLIC.toString()));
    }

    public StatisticsResponse getAllStatistics() throws UnAuthorizedException {

        User currentUser = getCurrentUser();

        if (!currentUser.isModerator()) {
            throw new UnAuthorizedException(Config.EXCEPTION_NOT_MODERATOR);
        }

        StatisticsResponse response = StatisticsResponse.create()
                .setPostsCount(postRepository.countPosts(LocalDateTime.now()))
                .setViewsCount(postRepository.countViews(LocalDateTime.now()))
                .setLikesCount(postRepository.countLikes(LocalDateTime.now()))
                .setDislikesCount(postRepository.countDislikes(LocalDateTime.now()));

        LocalDateTime firstPublicationDate = postRepository.getFirstPublicationDate(LocalDateTime.now());
        if (firstPublicationDate != null) {
            response.setFirstPublication(DateToSecondConverter.dateToSecond(firstPublicationDate));
        }

        return response;
    }

    public StatisticsResponse getMyStatistics() {

        Integer currentUserID = getCurrentUser().getId();

        StatisticsResponse response = StatisticsResponse.create()
                .setPostsCount(postRepository.countMyPosts(currentUserID, LocalDateTime.now()))
                .setViewsCount(postRepository.countMyViews(currentUserID, LocalDateTime.now()))
                .setLikesCount(postRepository.countMyLikes(currentUserID, LocalDateTime.now()))
                .setDislikesCount(postRepository.countMyDislikes(currentUserID, LocalDateTime.now()));

        LocalDateTime firstPublicationDate = postRepository.getMyFirstPublicationDate(currentUserID, LocalDateTime.now());
        if (firstPublicationDate != null) {
            response.setFirstPublication(DateToSecondConverter.dateToSecond(firstPublicationDate));
        }

        return response;
    }

    public ResultResponse editProfile(String email, Integer removePhoto, MultipartFile file, String name, String password) throws IOException {
        //TODO edit profile icon
        User userInRequest = userRepository.findUserByEmail(email);

        String fileExtension = "";
        String multipartFileName = file.getOriginalFilename();

        if (multipartFileName != null) {
            fileExtension = multipartFileName.substring(multipartFileName.lastIndexOf('.') + 1);
        }

        String fullFileName = STORAGE + genName() + "." + fileExtension;

        String full = uploadPath + fullFileName;

        file.transferTo(Paths.get(full));

        userInRequest.setPhoto(fullFileName);
        userRepository.save(userInRequest);

        return new ResultResponse(true, null);
    }

    public ResultResponse editProfile(ProfileRequest request) {
        //TODO edit profile icon
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

        if (isRemovePhoto) {
            currentUser.setPhoto("");
        } else if (photo != null) {
            currentUser.setPhoto(photo);
        }

        userRepository.save(currentUser);

        return new ResultResponse(true, null);
    }

    public ResultResponse loadImage(MultipartFile file) {
        //TODO load image for post

        return new ResultResponse();
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

    private String genName() {
        String alfabet = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            stringBuilder.append(alfabet.charAt(new Random().nextInt(alfabet.length() - 1)));
        }
        return stringBuilder.toString();
    }
}
