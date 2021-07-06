package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagsResponse;
import main.repository.GlobalSettingsRepository;
import main.services.GeneralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final GlobalSettingsRepository settingsRepo;
    private final GeneralService generalService;

    public ApiGeneralController(GlobalSettingsRepository settingsRepo, GeneralService generalService) {
        this.settingsRepo = settingsRepo;
        this.generalService = generalService;
    }

    @GetMapping(value = "/init")
    public static ResponseEntity<InitResponse> getBlogConfig() {
        return ResponseEntity
                .ok(new InitResponse());
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<SettingsResponse> getSettings() {

        boolean multiuserMode = settingsRepo.getSetting("MULTIUSER_MODE");

        boolean postPremoderation = settingsRepo.getSetting("POST_PREMODERATION");

        boolean statisticsIsPublic = settingsRepo.getSetting("STATISTICS_IS_PUBLIC");

        SettingsResponse settingsResponse = new SettingsResponse();

        settingsResponse.setMULTIUSER_MODE(multiuserMode);
        settingsResponse.setPOST_PREMODERATION(postPremoderation);
        settingsResponse.setSTATISTICS_IS_PUBLIC(statisticsIsPublic);

        return ResponseEntity
                .ok(settingsResponse);
    }

    @GetMapping(value = "/tag")
    public ResponseEntity<TagsResponse> getTag(@RequestParam(required = false) String query) {
        return generalService.getTags(query);
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<?> getCalendar() {
        return null;
    }

}
