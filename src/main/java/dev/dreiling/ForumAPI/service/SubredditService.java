package dev.dreiling.ForumAPI.service;

import dev.dreiling.ForumAPI.dto.SubredditRequest;
import dev.dreiling.ForumAPI.model.Subreddit;
import dev.dreiling.ForumAPI.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;

    @Transactional
    public SubredditRequest save(SubredditRequest subredditRequest) {

        Subreddit subreddit = subredditRepository.save(mapSubredditRequest(subredditRequest));
        subredditRequest.setId(subreddit.getId());
        return subredditRequest;

    }

    private Subreddit mapSubredditRequest(SubredditRequest subredditRequest) {

        return Subreddit.builder().name(subredditRequest.getName()).description(subredditRequest.getDescription()).build();

    }

    @Transactional(readOnly = true)
    public List<SubredditRequest> getAll() {

        return subredditRepository.findAll().stream().map(this::mapToRequest).collect(toList());

    }

    private SubredditRequest mapToRequest(Subreddit subreddit) {

        return SubredditRequest.builder().name(subreddit.getName()).id(subreddit.getId()).numberOfPosts(subreddit.getPosts().size()).build();

    }



}
