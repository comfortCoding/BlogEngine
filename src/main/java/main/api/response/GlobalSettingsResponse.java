package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GlobalSettingsResponse {

    @JsonProperty(value = "MULTIUSER_MODE")
    private boolean multiuserMode;

    @JsonProperty(value = "POST_PREMODERATION")
    private boolean postPremoderation;

    @JsonProperty(value = "STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;

    public static GlobalSettingsResponse create() {
        return new GlobalSettingsResponse();
    }
}
