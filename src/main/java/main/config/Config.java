package main.config;

public class Config {
    public static final String TITLE = "DevPub";
    public static final String SUBTITLE = "Рассказы разработчиков";
    public static final String PHONE = "+7 903 666-44-55";
    public static final String EMAIL = "mail@mail.ru";
    public static final String COPYRIGHT = "Дмитрий Сергеев";
    public static final String COPYRIGHT_FROM = "2005";

    public static final String TIME_ZONE = "Europe/Moscow";

    public static final Integer POST_ANNOTATION_SIZE = 150;

    public static final Byte MIN_PASSWORD_SIZE = 6;

    public static final Byte CAPTCHA_EXPIRES_AFTER_HOURS = 1;

    public static final String EMAIL_ERROR = "Этот e-mail уже зарегистрирован";
    public static final String NAME_ERROR = "Имя указано неверно";
    public static final String PASSWORD_ERROR = "Пароль короче 6-ти символов";
    public static final String CAPTCHA_ERROR = "Код с картинки введён неверно";

    public static final String NAME_REGEX = "[a-zA-Zа-яА-Я]+";
}
