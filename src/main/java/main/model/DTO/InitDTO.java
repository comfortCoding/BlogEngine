package main.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.config.Config;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitDTO {
    private String title = Config.TITLE;

    private String subtitle = Config.SUBTITLE;

    private String phone = Config.PHONE;

    private String email = Config.EMAIL;

    private String copyright = Config.COPYRIGHT;

    private String copyrightFrom = Config.COPYRIGHT_FROM;
}
