package dev.dreiling.ForumAPI.service;

import dev.dreiling.ForumAPI.dto.SubredditRequest;
import dev.dreiling.ForumAPI.exceptions.ForumException;
import dev.dreiling.ForumAPI.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditRequest save(SubredditRequest subredditRequest) {

        Subreddit subreddit = subredditRepository.save(subredditMapper.mapRequestToSubreddit(subredditRequest));
        subredditRequest.setId(subreddit.getId());
        return subredditRequest;

    }

    @Transactional(readOnly = true)
    public List<SubredditRequest> getAll() {

        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToRequest).collect(toList());

    }

    public SubredditRequest getSubreddit(Long id) {

        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new ForumException("No Subreddit found with ID: " + id));
        return subredditMapper.mapSubredditToRequest(subreddit);

    }

}
