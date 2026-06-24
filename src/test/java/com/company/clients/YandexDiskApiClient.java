package com.company.clients;

import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import com.company.config.Config;

/** Клиент для работы с Yandex Disk API */
public class YandexDiskApiClient {
    private final RequestSpecification spec;

    public YandexDiskApiClient() {
        this.spec =
                new RequestSpecBuilder()
                        .setBaseUri(Config.getYandexDiskUrl())
                        .addHeader("Authorization", "OAuth " + Config.getYandexDiskAuthToken())
                        .setContentType(ContentType.JSON)
                        .addFilter(new AllureRestAssured())
                        .build();
    }

    /** Получает информацию о диске */
    @Step("Получение информации о диске")
    public Response getDiskInfo() {
        return RestAssured.given(spec).get("/v1/disk/");
    }

    /** Пытается получить информацию о диске без токена */
    @Step("Получение информации о диске без авторизации")
    public Response getDiskInfoUnauthorized() {
        // Создаем спецификацию, не содержащую заголовка Authorization
        RequestSpecification noAuthSpec =
                new RequestSpecBuilder()
                        .setBaseUri(Config.getYandexDiskUrl())
                        .setContentType(ContentType.JSON)
                        .addFilter(new AllureRestAssured())
                        .build();

        return RestAssured.given(noAuthSpec).get("/v1/disk/");
    }
}
