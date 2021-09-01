package main.services;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.request.LoginRequest;
import main.api.response.*;
import main.api.request.RegisterRequest;
import main.model.CaptchaCode;
import main.model.User;
import main.model.dto.RegistrationErrorsDTO;
import main.repository.CaptchaRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
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

    private final UserToDTOCustomMapper userToDTOMapper;

    public ApiAuthService(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          CaptchaRepository captchaRepository,
                          PostRepository postRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;

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

        //формируем ответ для фронта
        CaptchaResponse response = new CaptchaResponse();

        response.setImage("data:image/png;base64, " + Base64.getEncoder().encodeToString(image.draw(secretCode)));
        response.setSecret(secretCode);

        return response;
    }

    public RegisterResponse registerUser(RegisterRequest request) {

        boolean isCorrectResult = true;

        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        String captcha = request.getCaptcha();
        String captchaSecret = request.getCaptchaSecret();

        RegistrationErrorsDTO errorsDTO = new RegistrationErrorsDTO();

        if (userRepository.isUserPresent(email) > 0) {
            isCorrectResult = false;
            errorsDTO.setEmailError(EMAIL_ERROR);
        }

        if (password.length() < MIN_PASSWORD_SIZE) {
            isCorrectResult = false;
            errorsDTO.setPasswordError(PASSWORD_ERROR);
        }

        if (!name.matches(NAME_REGEX)) {
            isCorrectResult = false;
            errorsDTO.setNameError(NAME_ERROR);
        }

        if (!captchaSecret.equals(captcha) ||
                captchaRepository.checkCaptcha(captchaSecret) == 0) {
            isCorrectResult = false;
            errorsDTO.setCaptchaError(CAPTCHA_ERROR);
        }

        if (isCorrectResult) {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setRegTime(LocalDateTime.now());

            userRepository.save(newUser);
        }

        RegisterResponse response = new RegisterResponse();
        response.setResult(isCorrectResult);
        response.setErrors(errorsDTO);

        return response;
    }

    public LogoutResponse logoutUser() {

        SecurityContextHolder.getContext().setAuthentication(null);

        LogoutResponse response = new LogoutResponse();

        response.setResult(true);

        return response;
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

    private LoginResponse getLoginResponse(String email) {

        User userDB = userRepository.findUserByEmail(email);

        LoginResponse response = new LoginResponse();

        response.setResult(userDB != null);
        response.setUser(userToDTOMapper.convertToDTO(userDB));
        response.getUser().setModerationCounter(response.getUser().isModeration() ? postRepository.countPostsForModeration(LocalDateTime.now()) : 0);

        return response;
    }
}
