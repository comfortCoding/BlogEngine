package main.response;

import lombok.Data;

@Data
public class BlogConfig {
    private String title = "DevPub";
    private String subtitle = "Рассказы разработчиков";
    private String phone = "+7 903 666-44-55";
    private String email = "mail@mail.ru";
    private String copyright = "Дмитрий Сергеев";
    private String copyrightFrom = "2005";
}
