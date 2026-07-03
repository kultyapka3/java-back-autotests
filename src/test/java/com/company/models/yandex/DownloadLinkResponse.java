package com.company.models.yandex;

import lombok.Data;

/** DTO для ответа на запрос ссылки скачивания */
@Data
public class DownloadLinkResponse {
    private String method;
    private String href;
    private boolean templated;
}
