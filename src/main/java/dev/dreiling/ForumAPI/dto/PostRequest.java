package dev.dreiling.ForumAPI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    private Long postID;
    private String subredditName;
    @NotBlank(message = "Post Name cannot be empty or Null")
    private String postName;
    private String url;
    private String description;

}
