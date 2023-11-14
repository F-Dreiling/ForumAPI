package dev.dreiling.ForumAPI.repository;

import dev.dreiling.ForumAPI.model.Post;
import dev.dreiling.ForumAPI.model.Subreddit;
import dev.dreiling.ForumAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
