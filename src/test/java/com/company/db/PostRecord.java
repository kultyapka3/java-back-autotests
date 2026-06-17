package com.company.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO для поста в БД */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRecord {
    private String postTitle;
    private String postContent;
    private String postStatus;
}
