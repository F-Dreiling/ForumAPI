package dev.dreiling.ForumAPI.mapper;

import dev.dreiling.ForumAPI.dto.SubredditRequest;
import dev.dreiling.ForumAPI.model.Subreddit;
import dev.dreiling.ForumAPI.model.Post;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditRequest mapSubredditToRequest(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapRequestToSubreddit(SubredditRequest subredditRequest);

}
