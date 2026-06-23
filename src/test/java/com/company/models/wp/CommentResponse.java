package com.company.models.wp;

import lombok.Data;

/** DTO ответа комментария в WordPress */
@Data
public class CommentResponse {
    private int id;
    private Integer post;
    private String author_name;
    private String author_email;
    private RenderedField content;
    private String status;
}
