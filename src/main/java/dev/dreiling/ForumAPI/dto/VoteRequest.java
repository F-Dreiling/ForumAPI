package dev.dreiling.ForumAPI.dto;

import dev.dreiling.ForumAPI.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {
    private VoteType voteType;
    private Long postId;
}