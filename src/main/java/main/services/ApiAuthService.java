package main.services;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.response.CaptchaResponse;
import main.api.response.CheckResponse;
import main.api.request.RegisterRequest;
import main.api.response.RegisterResponse;
import main.model.CaptchaCode;
import main.model.User;
import main.model.dto.RegistrationErrorsDTO;
import main.repository.CaptchaRepository;
import main.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

import static main.config.Config.*;

@Service
public class ApiAuthService {

    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;

    public ApiAuthService(UserRepository userRepository,
                          CaptchaRepository captchaRepository) {
        this.userRepository = userRepository;
        this.captchaRepository = captchaRepository;
    }

    public CheckResponse checkUser() {

        CheckResponse response = new CheckResponse();
        response.setResult(false);

        return response;
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
            newUser.setPassword(password);
            newUser.setRegTime(LocalDateTime.now());

            userRepository.save(newUser);
        }

        //Сгенерируем ответ для фронта
        RegisterResponse response = new RegisterResponse();
        response.setResult(isCorrectResult);
        response.setErrors(errorsDTO);

        return response;
    }
}
