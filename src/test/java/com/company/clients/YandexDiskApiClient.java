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
        return RestAssured.given(spec).get(Config.getYandexDiskPrefix());
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

        return RestAssured.given(noAuthSpec).get(Config.getYandexDiskPrefix());
    }

    /** Создает папку */
    @Step("Создание папки с названием = {path}")
    public Response createFolder(String path) {
        return RestAssured.given(spec)
                .queryParam("path", path)
                .put(Config.getYandexDiskPrefix() + "resources");
    }

    /** Перемещает папку в корзину */
    @Step("Перемещение в корзину папки с названием = {path}")
    public Response deleteFolderToTrash(String path) {
        return RestAssured.given(spec)
                .queryParam("path", path)
                .delete(Config.getYandexDiskPrefix() + "resources");
    }

    /** Восстанавливает папку из корзины */
    @Step("Восстановление из корзины папки с путем = {path}")
    public Response restoreFolderFromTrash(String path) {
        return RestAssured.given(spec)
                .queryParam("path", path)
                .put(Config.getYandexDiskPrefix() + "trash/resources/restore");
    }

    /** Удаляет папку */
    @Step("Полное удаление папки c путем = {path}")
    public Response deleteFolderPermanently(String path) {
        return RestAssured.given(spec)
                .queryParam("path", path)
                .queryParam("permanently", "true")
                .delete(Config.getYandexDiskPrefix() + "resources");
    }

    /** Получает информацию о ресурсах в корзине */
    @Step("Получение информации о ресурсах в корзине")
    public Response getTrashItems() {
        return RestAssured.given(spec).get(Config.getYandexDiskPrefix() + "trash/resources");
    }
}
