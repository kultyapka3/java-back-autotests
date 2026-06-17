package com.company.models.wp;

import lombok.Data;

/** DTO ответа поста в WordPress */
@Data
public class PostResponse {
    private int id;
    private RenderedField title;
    private RenderedField content;
    private String status;
    private String slug;
    private String link;

    public String getTitleText() {
        return title != null ? title.getRendered() : null;
    }

    public String getContentText() {
        if (content == null) return null;
        return content.getRaw() != null ? content.getRaw() : content.getRendered();
    }
}
