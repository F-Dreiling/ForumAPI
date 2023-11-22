package dev.dreiling.ForumAPI.controller;

import dev.dreiling.ForumAPI.dto.SubredditRequest;
import dev.dreiling.ForumAPI.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditRequest> createSubreddit(@RequestBody SubredditRequest subredditRequest) {

        return ResponseEntity.status( HttpStatus.CREATED).body(subredditService.save(subredditRequest) );

    }

    @GetMapping
    public ResponseEntity<List<SubredditRequest>> getAllSubreddit() {

        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditRequest> getSubreddit(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(subredditService.getSubreddit(id));

    }

}
