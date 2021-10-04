package main.controller;

import main.api.request.ProfileRequest;
import main.api.request.SettingsRequest;
import main.api.response.*;
import main.config.exception.UnAuthorizedException;
import main.config.exception.ValidationException;
import main.services.GeneralService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping(value = "/profile/my")
    public ResponseEntity<ResultResponse> editProfile(@RequestBody ProfileRequest request) {
        ResultResponse response = generalService.editProfile(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/profile/my", consumes = "multipart/form-data")
    public ResponseEntity<ResultResponse> editProfile(@RequestParam(value = "email") String email,
                                                      @RequestParam(value = "removePhoto") Integer removePhoto,
                                                      @RequestParam(value = "photo") MultipartFile file,
                                                      @RequestParam(value = "name") String name,
                                                      @RequestParam(value = "password", required = false) String password) throws IOException {
        ResultResponse response = generalService.editProfile(email, removePhoto, file, name, password);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    public ResponseEntity<ResultResponse> loadImage(@RequestParam MultipartFile file){
        ResultResponse response = generalService.loadImage(file);
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/statistics/all")
    public ResponseEntity<StatisticsResponse> getAllStatistics() throws UnAuthorizedException {
        StatisticsResponse response = generalService.getAllStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/statistics/my")
    public ResponseEntity<StatisticsResponse> getMyStatistics() {
        StatisticsResponse response = generalService.getMyStatistics();
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/settings")
    public void setSettings(@RequestBody SettingsRequest request) {
        generalService.setSettings(request);
    }
}
