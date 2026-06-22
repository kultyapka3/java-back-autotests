package com.company.models.wp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO запроса комментария в WordPress */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Integer post;
    private String author_name;
    private String author_email;
    private String content;
    private String status;
}
