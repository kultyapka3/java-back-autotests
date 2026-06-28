package com.company.models.yandex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO для запроса копирования файла в YandexDisk */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyFileRequest {
    private String from;
    private String path;
}
