package data.controller;

import data.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/video")
@CrossOrigin
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    private ResponseEntity<Boolean> newSession(@RequestBody Map<String,String> data, HttpServletRequest request) {
        return new ResponseEntity<>(videoService.newSession(data.get("busRoute"),request),HttpStatus.OK);
    }
}
