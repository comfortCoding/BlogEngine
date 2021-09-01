package main.controller;

import main.api.request.CommentRequest;
import main.api.response.*;
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
        InitResponse response = generalService.getBlogConfig();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<GlobalSettingsResponse> getSettings() {
        GlobalSettingsResponse response = generalService.getSettings();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/tag")
    public ResponseEntity<TagsResponse> getTag(@RequestParam(required = false) String query) {
        TagsResponse response = generalService.getTags(query);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam(required = false) Integer year) throws ValidationException {
        CalendarResponse response = generalService.getCalendar(year);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/profile/my")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok(null);
    }

}
