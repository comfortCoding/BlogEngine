package main.api.response;

import lombok.Data;
import main.config.Config;

@Data
public class InitResponse {

    private String title = Config.TITLE;

    private String subtitle = Config.SUBTITLE;

    private String phone = Config.PHONE;

    private String email = Config.EMAIL;

    private String copyright = Config.COPYRIGHT;

    private String copyrightFrom = Config.COPYRIGHT_FROM;

}
