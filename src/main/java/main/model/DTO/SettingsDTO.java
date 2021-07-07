package main.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingsDTO {

    @JsonProperty(value = "MULTIUSER_MODE")
    private boolean multiuserMode;

    @JsonProperty(value = "POST_PREMODERATION")
    private boolean postPremoderation;

    @JsonProperty(value = "STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;
}
