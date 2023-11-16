package dev.dreiling.ForumAPI.service;

import dev.dreiling.ForumAPI.dto.CommentsRequest;
import dev.dreiling.ForumAPI.exceptions.PostNotFoundException;
import dev.dreiling.ForumAPI.exceptions.ForumException;
import dev.dreiling.ForumAPI.mapper.CommentMapper;
import dev.dreiling.ForumAPI.model.Comment;
import dev.dreiling.ForumAPI.model.NotificationEmail;
import dev.dreiling.ForumAPI.model.Post;
import dev.dreiling.ForumAPI.model.User;
import dev.dreiling.ForumAPI.repository.CommentRepository;
import dev.dreiling.ForumAPI.repository.PostRepository;
import dev.dreiling.ForumAPI.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentsService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsRequest commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.mapToComment(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        try {
            mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
        } catch (Exception e) {
            throw new ForumException(e.getMessage());
        }
    }

    public List<CommentsRequest> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToRequest).toList();
    }

    public List<CommentsRequest> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToRequest)
                .toList();
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new ForumException("Comments contains unacceptable language");
        }
        return false;
    }
}