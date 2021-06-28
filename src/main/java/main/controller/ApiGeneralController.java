package main.controller;

import main.model.GlobalSetting;
import main.repository.GlobalSettingsRepository;
import main.response.BlogConfig;
import org.hibernate.annotations.SQLInsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    @Autowired
    private GlobalSettingsRepository settingsRepo;


    @GetMapping(value = "/init")
    public static ResponseEntity<?> getBlogConfig() {
        BlogConfig config = new BlogConfig();
        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    @GetMapping(value = "/settings")
    public ResponseEntity<?> getSettings() {

        List<GlobalSetting> settings = settingsRepo.getSettings();

        if (settings.size() > 0) {
            return new ResponseEntity<>(settings, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/tag")
    public ResponseEntity<?> getTag(){
        return null;
    }

    @GetMapping(value = "/calendar")
    public ResponseEntity<?> getCalendar(){
        return null;
    }

}
