package dev.dreiling.ForumAPI.service;

import dev.dreiling.ForumAPI.dto.VoteRequest;
import dev.dreiling.ForumAPI.exceptions.PostNotFoundException;
import dev.dreiling.ForumAPI.exceptions.ForumException;
import dev.dreiling.ForumAPI.model.Post;
import dev.dreiling.ForumAPI.model.Vote;
import dev.dreiling.ForumAPI.repository.PostRepository;
import dev.dreiling.ForumAPI.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static dev.dreiling.ForumAPI.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteRequest voteRequest) {
        Post post = postRepository.findById(voteRequest.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteRequest.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteRequest.getVoteType())) {
            throw new ForumException("You have already "
                    + voteRequest.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteRequest.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteRequest, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteRequest voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}