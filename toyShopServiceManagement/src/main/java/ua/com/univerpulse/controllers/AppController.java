package ua.com.univerpulse.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Danny Briskin (sql.coach.kiev@gmail.com)
 */
@RestController
@Log4j2
public class AppController {

    @PostMapping("/secret")
    @CrossOrigin
    public String secretService() {
        return "A secret message";
    }

    @GetMapping("/secret2")
    @CrossOrigin
    public String secretS2ervice() {
        return "A secret2  message";
    }
}