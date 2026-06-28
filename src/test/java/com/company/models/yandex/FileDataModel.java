package com.company.models.yandex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO для тестового файла */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDataModel {
    private String name;
    private byte[] content;
}
