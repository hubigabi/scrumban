package pl.utp.scrumban.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String commentText;
    private LocalDateTime localDateTime;
    private Long taskId;
    private Long userId;
    private String userName;
}