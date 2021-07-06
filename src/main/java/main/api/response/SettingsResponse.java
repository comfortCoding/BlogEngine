package main.api.response;

import lombok.Data;

@Data
public class SettingsResponse {

    private boolean MULTIUSER_MODE;

    private boolean POST_PREMODERATION;

    private boolean STATISTICS_IS_PUBLIC;

}
