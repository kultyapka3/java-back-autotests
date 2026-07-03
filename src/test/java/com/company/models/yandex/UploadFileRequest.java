package com.company.models.yandex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO для запроса загрузки файла в YandexDisk */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRequest {
    private String url;
    private byte[] content;
    private String filename;
}
