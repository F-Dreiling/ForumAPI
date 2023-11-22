package dev.dreiling.ForumAPI.controller;

import dev.dreiling.ForumAPI.dto.VoteRequest;
import dev.dreiling.ForumAPI.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteRequest voteRequest) {
        voteService.vote(voteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}