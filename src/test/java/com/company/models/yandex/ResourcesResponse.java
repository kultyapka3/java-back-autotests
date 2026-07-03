package com.company.models.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/** DTO ответа запроса ресурсов корзины в YandexDisk */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourcesResponse {
    private String path;
    private String type;
    private String name;
    private String mime_type;
    private String media_type;
    private Embedded _embedded;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        private int total;
        private List<Items> items;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Items {
            private String path;
            private String type;
            private String name;
            private String resource_id;
        }
    }
}
