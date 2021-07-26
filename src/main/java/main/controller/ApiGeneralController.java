package main.controller;

import main.api.response.InitResponse;
import main.api.response.GlobalSettingsResponse;
import main.api.response.TagsResponse;
import main.config.exception.ValidationException;
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
        return generalService.getBlogConfig();
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<GlobalSettingsResponse> getSettings() {
        return generalService.getSettings();
    }

    @GetMapping(value = "/tag")
    public ResponseEntity<TagsResponse> getTag(@RequestParam(required = false) String query) {
        return generalService.getTags(query);
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<?> getCalendar(@RequestParam(required = false) String year) throws ValidationException {
        return generalService.getCalendar(year);
    }

}
