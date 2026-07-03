package com.company.models.yandex;

import lombok.Data;

/** DTO для ответа запроса получения ссылки для загрузки файла в YandexDisk */
@Data
public class UploadLinkResponse {
    private String method;
    private String href;
    private boolean templated;
    private String operation_id;
}
