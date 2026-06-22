package com.company.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO для комментария в БД */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRecord {
    private int commentId;
    private int commentPostId;
    private String commentAuthor;
    private String commentAuthorEmail;
    private String commentContent;
    private String commentApproved;
}
