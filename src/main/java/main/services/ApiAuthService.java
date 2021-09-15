package main.services;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.request.LoginRequest;
import main.api.request.NewPasswordRequest;
import main.api.response.*;
import main.api.request.RegisterRequest;
import main.model.CaptchaCode;
import main.model.User;
import main.model.dto.ErrorsDTO;
import main.repository.CaptchaRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import main.services.MailService.ApiEmailService;
import main.util.GenerateHash;
import main.util.UserToDTOCustomMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

import static main.config.Config.*;

@Service
public class ApiAuthService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApiEmailService apiEmailService;

    private final UserToDTOCustomMapper userToDTOMapper;

    public ApiAuthService(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          CaptchaRepository captchaRepository,
                          PostRepository postRepository,
                          PasswordEncoder passwordEncoder,
                          ApiEmailService apiEmailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
        this.apiEmailService = apiEmailService;

        this.userToDTOMapper = Mappers.getMapper(UserToDTOCustomMapper.class);
    }

    public LoginResponse checkUser(Principal principal) {
        if (principal == null) {
            return new LoginResponse();
        }
        return getLoginResponse(principal.getName());
    }

    public CaptchaResponse getCaptcha() {

        Cage image = new GCage();

        String secretCode = image.getTokenGenerator().next();
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(0, 90000));

        CaptchaCode newCaptcha = new CaptchaCode();
        newCaptcha.setCode(code);
        newCaptcha.setSecretCode(secretCode);
        newCaptcha.setTime(LocalDateTime.now());
        captchaRepository.save(newCaptcha);

        captchaRepository.deleteExpiredCaptcha(LocalDateTime.now(), CAPTCHA_EXPIRES_AFTER_HOURS);

        CaptchaResponse response = new CaptchaResponse();

        response.setImage("data:image/png;base64, " + Base64.getEncoder().encodeToString(image.draw(secretCode)));
        response.setSecret(secretCode);

        return response;
    }

    public ResultResponse registerUser(RegisterRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        String captcha = request.getCaptcha();
        String captchaSecret = request.getCaptchaSecret();

        ErrorsDTO errorsDTO = new ErrorsDTO();

        if (userRepository.findUserByEmail(email) != null) {
            errorsDTO.setEmailError(EMAIL_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (password.length() < MIN_PASSWORD_SIZE) {

            errorsDTO.setPasswordError(PASSWORD_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (!name.matches(NAME_REGEX)) {

            errorsDTO.setNameError(NAME_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (!captchaSecret.equals(captcha) ||
                captchaRepository.checkSecretCode(captchaSecret) == null) {

            errorsDTO.setCaptchaError(CAPTCHA_ERROR);

            return new ResultResponse(false, errorsDTO);
        }

        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRegTime(LocalDateTime.now());

        userRepository.save(newUser);

        return new ResultResponse(true, null);
    }

    public ResultResponse logoutUser() {

        SecurityContextHolder.getContext().setAuthentication(null);

        return new ResultResponse(true, null);
    }


    public LoginResponse loginUser(LoginRequest request) {
        Authentication authentication
                = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return getLoginResponse(user.getUsername());
    }


    public ResultResponse restorePassword(String email) {

        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            return new ResultResponse(false, null);
        }

        String hash = new GenerateHash().generateHash();

        String messageBody = CHANGE_PASSWORD_TEXT + " <a href='" + SERVER + "/login/change-password/" + hash + "/'>" +
                CHANGE_PASSWORD_LINK + "</a>.";

        user.setCode(hash);
        userRepository.save(user);

        apiEmailService.sendMail(CHANGE_PASSWORD, messageBody, email);

        return new ResultResponse(true, null);
    }

    public ResultResponse saveNewPassword(NewPasswordRequest request) {

        User user = userRepository.findUserByCode(request.getCode());

        ErrorsDTO errorsDTO = new ErrorsDTO();
        if (user == null) {

            errorsDTO.setCodeError(PASSWORD_LINK_EXPIRED_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (request.getPassword().length() < MIN_PASSWORD_SIZE) {

            errorsDTO.setPasswordError(PASSWORD_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        if (captchaRepository.checkCode(request.getCaptcha()) == null
                && captchaRepository.checkSecretCode(request.getCaptchaSecret()) == null) {

            errorsDTO.setCaptchaError(CAPTCHA_ERROR);
            return new ResultResponse(false, errorsDTO);
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return new ResultResponse(true, null);
    }

    private LoginResponse getLoginResponse(String email) {

        User userDB = userRepository.findUserByEmail(email);

        LoginResponse response = new LoginResponse();

        response.setResult(userDB != null);
        response.setUser(userToDTOMapper.convertToDTO(userDB));
        response.getUser().setModerationCounter(response.getUser().isModeration() ? postRepository.countPostsForModeration(LocalDateTime.now()) : 0);

        return response;
    }
}
