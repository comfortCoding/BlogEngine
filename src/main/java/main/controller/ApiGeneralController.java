package main.controller;

import main.api.response.InitResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagsResponse;
import main.services.GeneralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final GeneralService generalService;

    public ApiGeneralController(GeneralService generalService) {
        this.generalService = generalService;
    }

    @GetMapping(value = "/init")
    public ResponseEntity<InitResponse> getBlogConfig() {
        return generalService.getInit();
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<SettingsResponse> getSettings() {
        return generalService.getAllSettings();
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
