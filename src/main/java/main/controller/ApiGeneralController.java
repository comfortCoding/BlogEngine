package main.controller;

import main.api.response.InitResponse;
import main.api.response.GlobalSettingsResponse;
import main.api.response.TagsResponse;
import main.model.DTO.GlobalSettingDTO;
import main.model.DTO.InitDTO;
import main.model.DTO.TagDTO;
import main.Util.Converter;
import main.services.GeneralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final GeneralService generalService;

    public ApiGeneralController(GeneralService generalService) {
        this.generalService = generalService;
    }

    @GetMapping(value = "/init")
    public ResponseEntity<InitResponse> getBlogConfig() {
        InitDTO dto = generalService.getInit();

        InitResponse response = new InitResponse();

        response.setTitle(dto.getTitle());
        response.setSubtitle(dto.getSubtitle());
        response.setCopyright(dto.getCopyright());
        response.setCopyrightFrom(dto.getCopyrightFrom());
        response.setEmail(dto.getEmail());
        response.setPhone(dto.getPhone());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<GlobalSettingsResponse> getSettings() {
        List<GlobalSettingDTO> globalSettingDTOs = generalService.getAllSettings();

        HashMap<String, Boolean> settings = new HashMap<>(Converter.convertSettingsListToMap(globalSettingDTOs));

        GlobalSettingsResponse globalSettingsResponse = new GlobalSettingsResponse();
        globalSettingsResponse.setMultiuserMode(settings.get("MULTIUSER_MODE"));
        globalSettingsResponse.setPostPremoderation(settings.get("POST_PREMODERATION"));
        globalSettingsResponse.setStatisticsIsPublic(settings.get("STATISTICS_IS_PUBLIC"));

        return ResponseEntity
                .ok(globalSettingsResponse);
    }

    @GetMapping(value = "/tag")
    public ResponseEntity<TagsResponse> getTag(@RequestParam(required = false) String query) {

        List<TagDTO> tagDTOs = generalService.getTags(query);

        TagsResponse response = new TagsResponse();
        response.setTags(tagDTOs);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<?> getCalendar() {
        return null;
    }

}
