package com.company.models.yandex;

import java.util.List;
import lombok.Data;

/** DTO ответа запроса ресурсов корзины в YandexDisk */
@Data
public class TrashResourcesResponse {
    private String path;
    private String type;
    private String name;
    private Embedded _embedded;

    @Data
    public static class Embedded {
        private int total;
        private List<Items> items;

        @Data
        public static class Items {
            private String path;
            private String type;
            private String name;
            private String resource_id;
            private String origin_path;
        }
    }
}
